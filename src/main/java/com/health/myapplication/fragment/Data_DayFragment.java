package com.health.myapplication.fragment;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.health.myapplication.db.DbHelper_date;
import com.health.myapplication.db.DbHelper_date_sub;
import com.health.myapplication.R;
import com.health.myapplication.adapter.RecyclerAdapter_day;
import com.health.myapplication.data.DateContract;
import com.health.myapplication.data.NoteContract;
import com.health.myapplication.dialog.TrainingDataDialog;
import com.health.myapplication.listener.DataListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class Data_DayFragment extends Fragment {

    private TrainingDataDialog dialog;
    private RecyclerView recyclerView;
    private RecyclerAdapter_day adapter;
    private Button button;
    // SQL DB의 레퍼런스
    private SQLiteDatabase mDb, nDb;
    private DbHelper_date dbHelperDate;
    private DbHelper_date_sub dbHelperNote;
    private TextView textView;
    private String DATE;

    private ArrayList<NoteContract> list;
    private long ID;
    private String date="";


    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public Data_DayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Date date = new Date();
        this.date = sdf.format(date);

        return inflater.inflate(R.layout.fragment_data__day, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //textView= view.findViewById(R.id.textView);
        recyclerView = view.findViewById(R.id.recycler_view);
        dbHelperDate = new DbHelper_date(getActivity());
        mDb = dbHelperDate.getWritableDatabase();
        dbHelperNote = new DbHelper_date_sub(getActivity());
        nDb = dbHelperNote.getWritableDatabase();
        setList();
        adapter = new RecyclerAdapter_day(getActivity(), list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        button = view.findViewById(R.id.imageAddButton);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick (View view){
                dialog = new TrainingDataDialog(getActivity());
                dialog.setDialogListener(new DataListener() {  // DialogListener 를 구현 추상 클래스이므로 구현 필수 -> dialog의 값을 전달 받음
                    @Override
                    public void onPositiveClicked(String time, String name, int set, int rep, float weight) {
                        //setResult(time, name, set,rep);
                        if(addToDate(time,name,set,rep, weight)) {
                            NoteContract data = new NoteContract(name, set, rep, weight);
                            data.setId(ID);
                            adapter.addItem(data);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                dialog.show();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    public boolean addToDate(String date, String name, int set, int rep, float weight) {
        ContentValues cv = new ContentValues();

        if(dateChecker(date)){  //함수에 의해 커서 체크 안해도 됨
            Cursor c = mDb.rawQuery("select * from "+ DateContract.DateContractEntry.TABLE_NAME+" where "+
                    DateContract.DateContractEntry.COLUMN_DATE+"='"+date+"'",null);
            c.moveToLast();
            long parent_id = c.getLong(c.getColumnIndex(DateContract.DateContractEntry._ID));
            cv.clear();
            cv.put(NoteContract.NoteDataEntry.COLUMN_KEY, parent_id);
            cv.put(NoteContract.NoteDataEntry.COLUMN_EXERCISE_NAME, name);
            cv.put(NoteContract.NoteDataEntry.COLUMN_SETTIME, set);
            cv.put(NoteContract.NoteDataEntry.COLUMN_REP, rep);
            cv.put(NoteContract.NoteDataEntry.COLUMN_WEIGHT, weight);
            nDb.insert(NoteContract.NoteDataEntry.TABLE_NAME, null, cv);
            c = nDb.rawQuery("select * from " + NoteContract.NoteDataEntry.TABLE_NAME, null);
            if(c.getCount()>0) {
                c.moveToLast();
                ID = c.getLong(c.getColumnIndex(NoteContract.NoteDataEntry._ID));
            }
            c.close();
            return true;
        }else{
            cv.clear();
            cv.put(DateContract.DateContractEntry.COLUMN_DATE, date);
            mDb.insert(DateContract.DateContractEntry.TABLE_NAME, null, cv);

            Cursor c = mDb.rawQuery("select * from "+ DateContract.DateContractEntry.TABLE_NAME,null);
            c.moveToLast();
            long parent_id = c.getLong(c.getColumnIndex(DateContract.DateContractEntry._ID));

            cv.clear();
            cv.put(NoteContract.NoteDataEntry.COLUMN_KEY, parent_id); //부모키(가장 최신 삽입 날짜)
            cv.put(NoteContract.NoteDataEntry.COLUMN_EXERCISE_NAME, name);
            cv.put(NoteContract.NoteDataEntry.COLUMN_SETTIME, set);
            cv.put(NoteContract.NoteDataEntry.COLUMN_REP, rep);
            cv.put(NoteContract.NoteDataEntry.COLUMN_WEIGHT, weight);
            nDb.insert(NoteContract.NoteDataEntry.TABLE_NAME, null, cv);

            return true;
        }
    }

    private boolean dateChecker(String date){
        Cursor c = mDb.rawQuery("select * from "+DateContract.DateContractEntry.TABLE_NAME+" where "+
                DateContract.DateContractEntry.COLUMN_DATE+"='"+date+"'",null);
        if(c.getCount()>0)
            return true;
        else
            return false;
    }

    public void setList(){
        if(list!=null)
            list.clear();
        list = new ArrayList<>();
        Cursor c = mDb.rawQuery("select * from "+DateContract.DateContractEntry.TABLE_NAME+" where "+
                DateContract.DateContractEntry.COLUMN_DATE+"='"+this.date+"'",null);
        if(c.getCount()!=0) {
            c.moveToFirst();
            long id=c.getLong(c.getColumnIndex(DateContract.DateContractEntry._ID));
            c.close();
            Cursor c2 = nDb.rawQuery("select * from "+ NoteContract.NoteDataEntry.TABLE_NAME+" where "+
                    NoteContract.NoteDataEntry.COLUMN_KEY+"="+id,null);
            if(c2.getCount()!=0) {
                c2.moveToFirst();
                do{
                    String name = c2.getString(c2.getColumnIndex(NoteContract.NoteDataEntry.COLUMN_EXERCISE_NAME));
                    int set = c2.getInt(c2.getColumnIndex(NoteContract.NoteDataEntry.COLUMN_SETTIME));
                    int rep = c2.getInt(c2.getColumnIndex(NoteContract.NoteDataEntry.COLUMN_REP));
                    float weight = c2.getFloat(c2.getColumnIndex(NoteContract.NoteDataEntry.COLUMN_WEIGHT));
                    long current_id = c2.getLong(c2.getColumnIndex(NoteContract.NoteDataEntry._ID));
                    NoteContract data = new NoteContract(name,set,rep,weight);
                    data.setId(current_id);
                    Log.d("currentid fragmmm", current_id+"");
                    list.add(data);

                }while (c2.moveToNext());
                c2.close();
            }
        } else{}
    }

    public void update(){
        if(adapter!=null){
            adapter = null;
            setList();
            adapter = new RecyclerAdapter_day(getActivity(), list);
            recyclerView.setAdapter(adapter);
        }
    }
}
