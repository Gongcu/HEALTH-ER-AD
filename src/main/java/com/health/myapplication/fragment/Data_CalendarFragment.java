package com.health.myapplication.fragment;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.health.myapplication.DbHelper.DbHelper_date;
import com.health.myapplication.DbHelper.DbHelper_date_sub;
import com.health.myapplication.R;
import com.health.myapplication.adapter.RecyclerAdapter_day;
import com.health.myapplication.calendar.OneDayDecorator;
import com.health.myapplication.calendar.SaturdayDecorator;
import com.health.myapplication.calendar.SundayDecorator;
import com.health.myapplication.data.DateContract;
import com.health.myapplication.data.NoteContract;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class Data_CalendarFragment extends Fragment {
    private MaterialCalendarView materialCalendarView;
    private static final String ARG_PARAM1 = "date_param1";
    private String date;
    private Bundle bundle;
    private TextView textView,textView2;
    private TextView nameTextView;
    private TextView setTextView;
    private TextView repTextView;
    private RecyclerAdapter_day adapter;
    private RecyclerView recyclerView;
    private SQLiteDatabase mDb, nDb;
    private DbHelper_date dbHelperDate;
    private DbHelper_date_sub dbHelperNote;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
    private ArrayList<NoteContract> list;
    private static final ArrayList<NoteContract> EMPTY_LIST=new ArrayList<>();


    public Data_CalendarFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(String param1) {
        Data_CalendarFragment fragment = new Data_CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data__calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        dbHelperDate = new DbHelper_date(getActivity());
        dbHelperNote = new DbHelper_date_sub(getActivity());
        mDb = dbHelperDate.getWritableDatabase();
        nDb = dbHelperNote.getWritableDatabase();

        textView = view.findViewById(R.id.textView6);
        textView2 = view.findViewById(R.id.textView7);
        nameTextView = view.findViewById(R.id.exerciseNameTextView);
        setTextView = view.findViewById(R.id.setTextView);
        repTextView = view. findViewById(R.id.repTextView);
        recyclerView = view.findViewById(R.id.recyclerView);
        materialCalendarView = view.findViewById(R.id.calendarView);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1))
                .setMaximumDate(CalendarDay.from(2030, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new OneDayDecorator());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Date d = date.getDate();
                String strDate =sdf.format(d);
                String text=sdfDay.format(d);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(d);

                textView.setText(text);
                textView2.setText(dayToString(calendar.get(Calendar.DAY_OF_WEEK)));
                setRecyclerView(strDate);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @SuppressLint("WrongConstant")
    public void setRecyclerView(String date){
        long id=0;
        Cursor c = mDb.rawQuery("select * from dateTable where "+ DateContract.DateContractEntry.COLUMN_DATE+"='"+date+"'",null);
        if(c.getCount()==0) {
            adapter = new RecyclerAdapter_day(getActivity(),EMPTY_LIST);
            recyclerView.setAdapter(adapter);
            return;
        }
        c.moveToFirst();
        id = c.getLong(c.getColumnIndex(DateContract.DateContractEntry._ID));
        c.close();
        list = new ArrayList<>();
        Cursor c2 = nDb.rawQuery("select * from "+ NoteContract.NoteDataEntry.TABLE_NAME+" where "+ NoteContract.NoteDataEntry.COLUMN_KEY+"="+id,null);
        if(c2.getCount()==0) {
            adapter = new RecyclerAdapter_day(getActivity(),EMPTY_LIST);
            recyclerView.setAdapter(adapter);
            return;
        }
        c2.moveToFirst();
        do {
            String name = c2.getString(c2.getColumnIndex(NoteContract.NoteDataEntry.COLUMN_EXERCISE_NAME));
            int set = c2.getInt(c2.getColumnIndex(NoteContract.NoteDataEntry.COLUMN_SETTIME));
            int rep = c2.getInt(c2.getColumnIndex(NoteContract.NoteDataEntry.COLUMN_REP));
            long current_id = c2.getLong(c2.getColumnIndex(NoteContract.NoteDataEntry._ID));
            NoteContract data = new NoteContract(name,set,rep);
            data.setId(current_id);
            list.add(data);
        } while (c2.moveToNext());
        c2.close();
        adapter = new RecyclerAdapter_day(getActivity(), list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

    }

    public String dayToString(int dayNum){
        String day=null;
        switch(dayNum){
            case 1:
                day = "Sun";
                break ;
            case 2:
                day = "Mon";
                break ;
            case 3:
                day = "Tue";
                break ;
            case 4:
                day = "Wed";
                break ;
            case 5:
                day = "Thu";
                break ;
            case 6:
                day = "Fri";
                break ;
            case 7:
                day = "Sat";
                break ;

        }
        return day;
    }

    public void update(){
        Date d = new Date();
        String str =sdf.format(d);
        String text=sdfDay.format(d);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        textView.setText(text);
        textView2.setText(dayToString(calendar.get(Calendar.DAY_OF_WEEK)));
        Date date = new Date();
        String strDate = sdf.format(date);
        setRecyclerView(strDate);
    }
}
