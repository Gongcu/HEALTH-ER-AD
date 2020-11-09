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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.health.myapplication.db.DbHelper_Calculator;
import com.health.myapplication.db.DbHelper_Calculator_sub;
import com.health.myapplication.R;
import com.health.myapplication.adapter.RecyclerAdapter_cal;
import com.health.myapplication.model.CalContract;
import com.health.myapplication.model.CalDateContract;
import com.health.myapplication.dialog.CalculatorDialog;
import com.health.myapplication.listener.CalculatorDialogListener;

import java.util.ArrayList;
import java.util.Collections;


public class Calculator_DataFragment extends Fragment {

    private SQLiteDatabase mDb,nDb;
    private DbHelper_Calculator_sub dbHelper_note;
    private DbHelper_Calculator dbHelper_date;

    private CalculatorDialog dialog;
    private Button addBtn;
    private RecyclerView recyclerView;
    private RecyclerAdapter_cal adapter;

    private ArrayList<String> date_list;

    private AdView mAdView;

    public Calculator_DataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        date_list = new ArrayList<>();

        dbHelper_date=new DbHelper_Calculator(getActivity());
        mDb=dbHelper_date.getWritableDatabase();

        dbHelper_note=new DbHelper_Calculator_sub(getActivity());
        nDb=dbHelper_note.getWritableDatabase();
        return inflater.inflate(R.layout.fragment_data_calculator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        addBtn = view.findViewById(R.id.AddButton);
        recyclerView= view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        setList();

        adapter = new RecyclerAdapter_cal(getActivity(), getAllWeight(),date_list);
        recyclerView.setAdapter(adapter);


        addBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new CalculatorDialog(getActivity());
                dialog.setDialogListener(new CalculatorDialogListener() {  // DialogListener 를 구현 추상 클래스이므로 구현 필수 -> dialog의 값을 전달 받음
                    @Override
                    public void onPositiveClicked() { }
                    @Override
                    public void onPositiveClicked(String date,String name, double one_rm) {
                        addData(date, name, one_rm);
                    }
                    @Override
                    public void onNegativeClicked() {
                    }
                });
                dialog.show();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    public void update(){
        if(adapter!=null) {
            adapter=null;
            date_list.clear();
            setList();
            adapter = new RecyclerAdapter_cal(getActivity(), getAllWeight(), date_list);
            recyclerView.setAdapter(adapter);
        }
    }


    private void setList(){
        Cursor cursor = mDb.rawQuery("select * from " + CalDateContract.Entry.TABLE_NAME+" group by "+ CalDateContract.Entry.COLUMN_DATE, null);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                date_list.add(cursor.getString(cursor.getColumnIndex(CalDateContract.Entry.COLUMN_DATE)));
            } while (cursor.moveToNext());
        }
        Collections.sort(date_list);
        Collections.reverse(date_list);
        cursor.close();

    }

    public void addData(String date, String name, double one_rm) {
        ContentValues cv = new ContentValues();
        Cursor c2 = mDb.rawQuery("select * from " + CalDateContract.Entry.TABLE_NAME + " where " + CalDateContract.Entry.COLUMN_DATE + "='" + date+"'", null);

        /*같은 날이 있을경우 note만 추가*/
        if(c2.getCount()>0){
            c2.moveToFirst();
            long id = c2.getLong(c2.getColumnIndex(CalDateContract.Entry._ID));
            c2.close();
            Cursor check = nDb.rawQuery("select * from "+CalContract.Entry.TABLE_NAME+" where " + CalContract.Entry.COLUMN_DATE + "='" + date+"' and "+
                    CalContract.Entry.COLUMN_EXERCISE+"='"+name+"'" ,null);
            if(check.getCount()>0) {
                Toast.makeText(getActivity(), "이미 같은 운동이 존재 합니다.", Toast.LENGTH_LONG).show();
                return;
            }
            check.close();

            cv.put(CalContract.Entry.COLUMN_DATE, date);
            cv.put(CalContract.Entry.COLUMN_EXERCISE, name);
            cv.put(CalContract.Entry.COLUMN_ONERM, one_rm);
            cv.put(CalContract.Entry.COLUMN_KEY, id);
            nDb.insert(CalContract.Entry.TABLE_NAME, null, cv);

        }
        else {
            if(!date_list.contains(date))
                date_list.add(0,date);
            cv.put(CalDateContract.Entry.COLUMN_DATE, date);
            mDb.insert(CalDateContract.Entry.TABLE_NAME, null, cv);
            cv.clear();

            Cursor c3= mDb.rawQuery("select * from "+CalDateContract.Entry.TABLE_NAME,null);
            if(c3.getCount()>0) {
                c3.moveToLast();
                long recentId = c3.getLong(c3.getColumnIndex(CalDateContract.Entry._ID));//가장 최신에 삽입된 날짜의 ID를 얻어옴
                cv.put(CalContract.Entry.COLUMN_DATE, date);
                cv.put(CalContract.Entry.COLUMN_EXERCISE, name);
                cv.put(CalContract.Entry.COLUMN_ONERM, one_rm);
                cv.put(CalContract.Entry.COLUMN_KEY, recentId);
                nDb.insert(CalContract.Entry.TABLE_NAME, null, cv);
            }
        }
        adapter = null;
        adapter = new RecyclerAdapter_cal(getActivity(), getAllWeight(),date_list);
        recyclerView.setAdapter(adapter);
    }


    private Cursor getAllWeight() {
        // 두번째 파라미터 (Projection array)는 여러 열들 중에서 출력하고 싶은 것만 선택해서 출력할 수 있게 한다.
        // 모든 열을 출력하고 싶을 때는 null 을 입력한다.
        return nDb.query(
                CalContract.Entry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}
