package com.health.myapplication.ui.custom_program;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.myapplication.db.DbHelper_program;
import com.health.myapplication.R;
import com.health.myapplication.callback.ItemTouchHelperCallback;
import com.health.myapplication.model.etc.ProgramContract;
import com.health.myapplication.dialog.ProgramDialog;
import com.health.myapplication.listener.ProgramDialogListener;

import java.util.ArrayList;
import java.util.Collections;

public class ProgramFragment extends Fragment implements ItemTouchHelperCallback.ItemMoveListener {
    private RecyclerView recyclerView;
    private SpecificProgramAdapter eAdapter;
    private LinearLayoutManager layoutManager;

    private SQLiteDatabase mDb;
    private DbHelper_program DbHelper;

    private ArrayList<ProgramContract> item_list;

    private ProgramDialog dialog;

    private int DATE;
    private int ACTIVITY_NUMBER;

    private Button button;


    public ProgramFragment() {
    }

    public static ProgramFragment newInstance(int DATE, int ACTIVITY_NUMBER) {
        ProgramFragment fragment = new ProgramFragment();
        Bundle args = new Bundle();
        args.putInt("date",DATE);
        args.putInt("activity_number", ACTIVITY_NUMBER);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.DATE=getArguments().getInt("date");
        this.ACTIVITY_NUMBER=getArguments().getInt("activity_number");
        return inflater.inflate(R.layout.fragment_program, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DbHelper = new DbHelper_program(getActivity());
        mDb = DbHelper.getWritableDatabase(); //데이터에 db 채우기 위함

        // initPartList();
        initList();  //바뀜

        recyclerView = view.findViewById(R.id.recyclerView);
        button = view.findViewById(R.id.AddButton);


        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        eAdapter = new SpecificProgramAdapter(getActivity(), getData(),ACTIVITY_NUMBER,DATE,item_list); //1일차 part
        recyclerView.setAdapter(eAdapter);


        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(this);// create MyItemTouchHelperCallback
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback); // Create ItemTouchHelper and pass with parameter the MyItemTouchHelperCallback
        touchHelper.attachToRecyclerView(recyclerView); // Attach ItemTouchHelper to RecyclerView

        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog = new ProgramDialog(getActivity(), ACTIVITY_NUMBER);
                dialog.setDialogListener(new ProgramDialogListener() {  // DialogListener 를 구현 추상 클래스이므로 구현 필수 -> dialog의 값을 전달 받음
                    @Override
                    public void onPositiveClicked(int date, String part, String exercise, int set, int rep) {
                        addProgram(part, exercise, set, rep);
                        initList();
                        eAdapter = new SpecificProgramAdapter(getActivity(), getData(),ACTIVITY_NUMBER,DATE,item_list); //1일차 part
                        recyclerView.setAdapter(eAdapter);
                    }
                });
                dialog.show();
            }
        });
    }


    private void initList(){
        item_list = new ArrayList<>();
        //해당 ACTVITY, DATE 의 값을 가져옴
        Cursor c = mDb.rawQuery("select *"+ " from " + ProgramContract.ProgramDataEntry.TABLE_NAME + " where " +
                ProgramContract.ProgramDataEntry.COLUMN_ACTIVITY + "="+ACTIVITY_NUMBER+" and " + ProgramContract.ProgramDataEntry.COLUMN_DATE + "=" + DATE+" order by "+ ProgramContract.ProgramDataEntry.COLUMN_ORDER+" ASC", null);
        if(c.getCount()>0) {
            c.moveToFirst();
            do {
                String part = c.getString(c.getColumnIndex(ProgramContract.ProgramDataEntry.COLUMN_EXERCISE));
                int set = c.getInt(c.getColumnIndex(ProgramContract.ProgramDataEntry.COLUMN_SET));
                int rep = c.getInt(c.getColumnIndex(ProgramContract.ProgramDataEntry.COLUMN_REP));
                int order = c.getInt(c.getColumnIndex(ProgramContract.ProgramDataEntry.COLUMN_ORDER));
                int id = c.getInt(c.getColumnIndex(ProgramContract.ProgramDataEntry._ID));
                item_list.add(new ProgramContract(part,set,rep,order,id));
            } while (c.moveToNext());
        }
        c.close();
        Collections.sort(item_list);
    }

    public void addProgram(String part, String name, int set, int rep) {
        Cursor c = mDb.rawQuery("select * from "+ ProgramContract.ProgramDataEntry.TABLE_NAME+" where "
                + ProgramContract.ProgramDataEntry.COLUMN_ACTIVITY+"="+ACTIVITY_NUMBER + " and "
                + ProgramContract.ProgramDataEntry.COLUMN_DATE+"="+DATE +" and "
                + ProgramContract.ProgramDataEntry.COLUMN_EXERCISE+"='"+name+"'", null);
        if(c.getCount()>0){
            Toast.makeText(getActivity(),"이미 같은 운동이 있습니다.",Toast.LENGTH_SHORT).show();
            c.close();
            return;
        }
        c= mDb.rawQuery("select * from "+ ProgramContract.ProgramDataEntry.TABLE_NAME+" where "
                + ProgramContract.ProgramDataEntry.COLUMN_ACTIVITY+"="+ACTIVITY_NUMBER + " and "
                + ProgramContract.ProgramDataEntry.COLUMN_DATE+"="+DATE+" order by "+ ProgramContract.ProgramDataEntry.COLUMN_ORDER+" ASC", null);
        int order = 1;
        if(c.getCount()>0){
            c.moveToLast();
            order=c.getInt(c.getColumnIndex(ProgramContract.ProgramDataEntry.COLUMN_ORDER))+1;
        }
        ContentValues cv = new ContentValues();
        cv.put(ProgramContract.ProgramDataEntry.COLUMN_ACTIVITY, ACTIVITY_NUMBER);
        cv.put(ProgramContract.ProgramDataEntry.COLUMN_DATE, DATE);
        cv.put(ProgramContract.ProgramDataEntry.COLUMN_PART, part);
        cv.put(ProgramContract.ProgramDataEntry.COLUMN_EXERCISE, name);
        cv.put(ProgramContract.ProgramDataEntry.COLUMN_SET, set);
        cv.put(ProgramContract.ProgramDataEntry.COLUMN_REP, rep);
        cv.put(ProgramContract.ProgramDataEntry.COLUMN_ORDER, order);
        if(mDb.insert(ProgramContract.ProgramDataEntry.TABLE_NAME,null,cv)>0) {
            c = mDb.rawQuery("select * from " + ProgramContract.ProgramDataEntry.TABLE_NAME, null);
            int id = 0;
            if (c.getCount() > 0) {
                c.moveToLast();
                id = c.getInt(c.getColumnIndex(ProgramContract.ProgramDataEntry._ID));
            }
            c.close();
            item_list.add(new ProgramContract(name, set, rep, order, id));
            Collections.sort(item_list);
        }
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

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        item_list.add(toPosition,item_list.remove(fromPosition));// change position
        ContentValues cv = new ContentValues();
        for(int i=0; i<item_list.size(); i++){
            cv.put(ProgramContract.ProgramDataEntry.COLUMN_ORDER, i+1);
            mDb.update(ProgramContract.ProgramDataEntry.TABLE_NAME, cv,ProgramContract.ProgramDataEntry._ID+"="+item_list.get(i).getId(),null);
            cv.clear();
        }
        eAdapter.notifyItemMoved(fromPosition, toPosition); //notifies changes in adapter, in this case use the notifyItemMoved
    }

    @Override
    public void onItemSwiped(int id) {
        mDb.delete(ProgramContract.ProgramDataEntry.TABLE_NAME,ProgramContract.ProgramDataEntry._ID+"="+id,null);
        eAdapter.remove(id);
        eAdapter.notifyDataSetChanged();
    }
    @Override
    public void onItemMoveFinished(){}
}
