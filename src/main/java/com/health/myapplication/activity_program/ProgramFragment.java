package com.health.myapplication.activity_program;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.health.myapplication.DbHelper.DbHelper_program;
import com.health.myapplication.R;
import com.health.myapplication.adapter.RecyclerAdapter_part;
import com.health.myapplication.data.ExerciseData;
import com.health.myapplication.data.ProgramContract;
import com.health.myapplication.dialog.ProgramDialog;
import com.health.myapplication.listener.ProgramDialogListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramFragment extends Fragment implements View.OnClickListener{
    private Context mContext;
    private static final String TAG="RECOMMEND FRAGMENT";
    private TextView textView;
    private RecyclerView recyclerView;
    private RecyclerAdapter_part pAdapter;
    private LinearLayoutManager layoutManager;

    private SQLiteDatabase mDb;
    private DbHelper_program DbHelper;

    private ArrayList<ExerciseData> list;
    private ArrayList<ArrayList<String>> list_image;

    private ArrayList<String> part_list;
    private ArrayList<ExerciseData> exercise_list;
    private ProgramDialog dialog;

    private int DATE;
    private int ACTIVITY_NUMBER;

    private Button button;
    private AdView mAdView;

    public ProgramFragment() {
    }

    public ProgramFragment(Context context, int DATE, int ACTIVITY_NUMBER) {
        mContext=context;
        this.DATE = DATE;
        this.ACTIVITY_NUMBER = ACTIVITY_NUMBER;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_program, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        DbHelper = new DbHelper_program(getActivity());
        mDb = DbHelper.getWritableDatabase(); //데이터에 db 채우기 위함

        initPartList();

        textView = view.findViewById(R.id.textView);
        recyclerView = view.findViewById(R.id.recyclerView);
        button = view.findViewById(R.id.AddButton);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        pAdapter = new RecyclerAdapter_part(getActivity(), getData(),part_list,DATE,ACTIVITY_NUMBER); //1일차 part
        recyclerView.setAdapter(pAdapter);


        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.AddButton:
                dialog = new ProgramDialog(getActivity(), ACTIVITY_NUMBER);
                dialog.setDialogListener(new ProgramDialogListener() {  // DialogListener 를 구현 추상 클래스이므로 구현 필수 -> dialog의 값을 전달 받음
                    @Override
                    public void onPositiveClicked(int date, String part, String exercise, int set, int rep) {
                        addToDate(part, exercise, set, rep);

                        initPartList();
                        pAdapter = new RecyclerAdapter_part(getActivity(), getData(), part_list, DATE, ACTIVITY_NUMBER); //1일차 part
                        recyclerView.setAdapter(pAdapter);
                    }
                });
                dialog.show();
                break;
        }
    }


    public void addToDate(String part, String name, int set, int rep) {
        Cursor c = mDb.rawQuery("select * from "+ ProgramContract.ProgramDataEntry.TABLE_NAME+" where "
                + ProgramContract.ProgramDataEntry.COLUMN_ACTIVITY+"="+ACTIVITY_NUMBER + " and "
                + ProgramContract.ProgramDataEntry.COLUMN_DATE+"="+DATE +" and "
                + ProgramContract.ProgramDataEntry.COLUMN_EXERCISE+"='"+name+"'", null);
        if(c.getCount()>0){
            Toast.makeText(getActivity(),"이미 같은 운동이 있습니다.",Toast.LENGTH_SHORT).show();
            c.close();
            return;
        }
        int add=0;
        ContentValues cv = new ContentValues();
        cv.put(ProgramContract.ProgramDataEntry.COLUMN_ACTIVITY, ACTIVITY_NUMBER);
        cv.put(ProgramContract.ProgramDataEntry.COLUMN_DATE, DATE);
        cv.put(ProgramContract.ProgramDataEntry.COLUMN_PART, part);
        cv.put(ProgramContract.ProgramDataEntry.COLUMN_EXERCISE, name);
        cv.put(ProgramContract.ProgramDataEntry.COLUMN_SET, set);
        cv.put(ProgramContract.ProgramDataEntry.COLUMN_REP, rep);

        for(int i=0; i<part_list.size(); i++){
            if(part_list.get(i).equals(part)) {
                add = 1;
                break;
            }
        }
        if(add==0) {
            part_list.add(part);
        }

        mDb.insert(ProgramContract.ProgramDataEntry.TABLE_NAME,null,cv);
    }

    private void initPartList(){
        Cursor c;
        //해당 ACTVITY, DATE 의 값을 가져옴
        c = mDb.rawQuery("select *"+ " from " + ProgramContract.ProgramDataEntry.TABLE_NAME + " where " +
                ProgramContract.ProgramDataEntry.COLUMN_ACTIVITY + "="+ACTIVITY_NUMBER+" and " + ProgramContract.ProgramDataEntry.COLUMN_DATE + "=" + DATE + " group by " + ProgramContract.ProgramDataEntry.COLUMN_PART, null);
        c.moveToFirst();
        part_list = new ArrayList<>();
        try {
            do {
                String part=c.getString(c.getColumnIndex(ProgramContract.ProgramDataEntry.COLUMN_PART));
                part_list.add(part);
            } while (c.moveToNext());
        }catch (CursorIndexOutOfBoundsException e){e.printStackTrace();part_list.add("");} //->cursor index 오류: 없는 값을 참조하려함. try catch로 해결
        c.close();
    }

    private Cursor getData() {
        String whereClause = ProgramContract.ProgramDataEntry.COLUMN_ACTIVITY+"=? and "+ProgramContract.ProgramDataEntry.COLUMN_DATE+"=?";
        String[] whereArgs={String.valueOf(ACTIVITY_NUMBER),String.valueOf(DATE)};
        return mDb.query(
                ProgramContract.ProgramDataEntry.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                ProgramContract.ProgramDataEntry.COLUMN_TIMESTAMP + " ASC"
        );
    }

}
