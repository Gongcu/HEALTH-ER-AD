package com.health.myapplication.fragment;


import android.content.ContentValues;
import android.content.Context;
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
import com.health.myapplication.DbHelper.DbHelper_date;
import com.health.myapplication.DbHelper.DbHelper_date_sub;
import com.health.myapplication.R;
import com.health.myapplication.adapter.RecyclerAdapter_date;
import com.health.myapplication.adapter.RecyclerAdapter_date_sub;
import com.health.myapplication.data.CalContract;
import com.health.myapplication.data.DateContract;
import com.health.myapplication.data.NoteContract;
import com.health.myapplication.dialog.TrainingDataDialog;
import com.health.myapplication.listener.DialogListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class Data_DataFragment extends Fragment {
    private ArrayList<DateContract> list;
    private ArrayList<String> date_list;
    private TrainingDataDialog dialog;
    private RecyclerAdapter_date dAdapter; //날짜 recycler뷰
    private RecyclerAdapter_date_sub mAdapter; //이제 날짜 밑의 기록 recyclerview로 바꿔야됨
    private RecyclerView recyclerView;
    private Button button;
    // SQL DB의 레퍼런스
    private SQLiteDatabase mDb, nDb;

    public  static Context context;

    private String date="";
    private String name="";
    private int set =0;
    private int rep =0;


    public Data_DataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        list = new ArrayList<>();


        DbHelper_date dbHelper_date = new DbHelper_date(getActivity());
        DbHelper_date_sub dbHelper_note = new DbHelper_date_sub(getActivity());

        mDb = dbHelper_date.getWritableDatabase(); //데이터에 db 채우기 위함
        nDb = dbHelper_note.getWritableDatabase(); //데이터에 db 채우기 위함

        return inflater.inflate(R.layout.fragment_data__data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        setList();

        dAdapter = new RecyclerAdapter_date(getActivity(), getAllDate(),getAllNote(),date_list);
        recyclerView.setAdapter(dAdapter);
        button = view.findViewById(R.id.imageAddButton);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick (View view){
                dialog = new TrainingDataDialog(getActivity());
                dialog.setDialogListener(new DialogListener() {  // DialogListener 를 구현 추상 클래스이므로 구현 필수 -> dialog의 값을 전달 받음
                    @Override
                    public void onPositiveClicked(int date, String part, String exercise) {}
                    @Override
                    public void onPositiveClicked() {
                    }
                    @Override
                    public void onPositiveClicked(double height, double weight) {
                    }
                    @Override
                    public void onPositiveClicked(String time, String name, int set, int rep) {
                        setResult(time, name, set,rep);
                        addToDate(recyclerView, time,name,set,rep);
                    }

                    @Override
                    public void onNegativeClicked() { }
                });
                dialog.show();
            }
        });

/* 잘못 스와이프 해서 기록 다 날라갈수도
        // 목록에서 항목을 왼쪽, 오른쪽 방향으로 스와이프 하는 항목을 처리
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            // 사용하지 않는다.
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // 스와이프된 아이템의 아이디를 가져온다.
                long id = (long) viewHolder.itemView.getTag();
                // DB 에서 해당 아이디를 가진 레코드를 삭제한다.
                removeDate(id);
                // 리스트를 갱신한다.
                dAdapter.swapCursor(getAllDate());
            }
        }).attachToRecyclerView(recyclerView);  //리사이클러뷰에 itemTouchHelper 를 붙인다.
        super.onViewCreated(view, savedInstanceState);
        */
    }

    public void addToDate(View view, String date, String name, int set, int rep) {
        ContentValues cv = new ContentValues();

        int add_mode =0; //0 없는경우, 1 있는경우
        long parent_id=1;

        //테이블에 같은 날짜가 있는지 확인
        Cursor c= mDb.rawQuery("SELECT * FROM "+DateContract.DateContractEntry.TABLE_NAME,null);
        if(c.getCount()>0) {
            c.moveToLast();
            parent_id = c.getLong(c.getColumnIndex(DateContract.DateContractEntry._ID)); //dateTable의 가장 최신 id
            if (c.moveToFirst()) {
                do {
                    if (c.getString(c.getColumnIndex(DateContract.DateContractEntry.COLUMN_DATE)).equals(date)) { //같은 날짜가 있으면 해당 id를 받아오고 반복문 탈출
                        add_mode = 1; //1인 경우 date 추가는 안함 밑의 switch문 참고
                        parent_id = c.getLong(c.getColumnIndex(DateContract.DateContractEntry._ID));
                        break;
                    }
                } while (c.moveToNext());
            }
        }
        c.close();

        switch (add_mode){
            case 0: //해당 날짜가 없는 경우 방금 추가된 parent_table의 id를 가져온다
                if(!date_list.contains(date))
                    date_list.add(0,date);
                cv.put(DateContract.DateContractEntry.COLUMN_DATE, date);
                mDb.insert(DateContract.DateContractEntry.TABLE_NAME, null, cv);
                cv.clear();

                //가장 최신 id를 가져옴
                Cursor getIDcursor = mDb.rawQuery("SELECT "+DateContract.DateContractEntry._ID+ " From "+DateContract.DateContractEntry.TABLE_NAME,null);
                getIDcursor.moveToLast();
                parent_id=getIDcursor.getLong(getIDcursor.getColumnIndex(DateContract.DateContractEntry._ID));

                cv.put(NoteContract.NoteDataEntry.COLUMN_KEY, parent_id); //부모키(가장 최신 날짜)
                cv.put(NoteContract.NoteDataEntry.COLUMN_EXERCISE_NAME, name);
                cv.put(NoteContract.NoteDataEntry.COLUMN_SETTIME, set);
                cv.put(NoteContract.NoteDataEntry.COLUMN_REP, rep);
                nDb.insert(NoteContract.NoteDataEntry.TABLE_NAME, null, cv);
                dAdapter = null;
                dAdapter = new RecyclerAdapter_date(getActivity(), getAllDate(),getAllNote(),date_list);
                recyclerView.setAdapter(dAdapter);
                break;
            case 1: //해당 날짜가 있는경우
                cv.put(NoteContract.NoteDataEntry.COLUMN_KEY, parent_id); //부모키
                cv.put(NoteContract.NoteDataEntry.COLUMN_EXERCISE_NAME, name);
                cv.put(NoteContract.NoteDataEntry.COLUMN_SETTIME, set);
                cv.put(NoteContract.NoteDataEntry.COLUMN_REP, rep);
                nDb.insert(NoteContract.NoteDataEntry.TABLE_NAME, null, cv);
                dAdapter = null;
                dAdapter = new RecyclerAdapter_date(getActivity(), getAllDate(),getAllNote(),date_list);
                recyclerView.setAdapter(dAdapter);
                break;
        }

    }


    private Cursor getAllNote() {
        // 두번째 파라미터 (Projection array)는 여러 열들 중에서 출력하고 싶은 것만 선택해서 출력할 수 있게 한다.
        // 모든 열을 출력하고 싶을 때는 null 을 입력한다.
        return nDb.query(
                NoteContract.NoteDataEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                NoteContract.NoteDataEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }

    private Cursor getAllDate() {
        return mDb.query(
                DateContract.DateContractEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DateContract.DateContractEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }

    private void setList(){
        if(date_list!=null)
            date_list.clear();
        date_list=new ArrayList<>();
        Cursor cursor = mDb.rawQuery("select * from " + DateContract.DateContractEntry.TABLE_NAME+" group by "+ CalContract.Entry.COLUMN_DATE, null);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                date_list.add(cursor.getString(cursor.getColumnIndex(CalContract.Entry.COLUMN_DATE)));
                Log.d("adtefrag",""+cursor.getString(cursor.getColumnIndex(CalContract.Entry.COLUMN_DATE)));
            } while (cursor.moveToNext());
        }
        Collections.sort(date_list);
        Collections.reverse(date_list);
        cursor.close();
    }

    //for dialog listener
    private void setResult(String date, String name, int set,int rep){
        int check=0;
        for(int i=0; i<list.size(); i++){
            if(list.get(i).getDate().equals(date))
            {
                NoteContract data = new NoteContract(name,set,rep);
                list.get(i).getList().add(data);
                check=1;
                return;
            }
        }
        if(check==0) //겹치는 날이 없을경우
            list.add(new DateContract(date, new NoteContract(name,set,rep)));
    }

    public void update(){
        if(dAdapter!=null) {
            dAdapter=null;

            setList();

            dAdapter = new RecyclerAdapter_date(getActivity(), getAllDate(),getAllNote(), date_list);
            recyclerView.setAdapter(dAdapter);
        }
    }
}
