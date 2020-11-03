package com.health.myapplication.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.health.myapplication.DbHelper.DbHelper_proItem;
import com.health.myapplication.R;
import com.health.myapplication.adapter.RecyclerAdapter_program;
import com.health.myapplication.model.PartData;
import com.health.myapplication.data.ProgramItem;
import com.health.myapplication.dialog.DeleteDialog;
import com.health.myapplication.dialog.ProgramSelectDialog;
import com.health.myapplication.listener.DeleteDialogListener;
import com.health.myapplication.listener.ProgramSelectListener;

import java.util.ArrayList;

public class ProgramActivity extends AppCompatActivity {
    private ProgramSelectDialog dialog;
    private RecyclerView recyclerView;
    private Button button;
    private RecyclerAdapter_program adapter;
    private ArrayList<PartData> list;
    private DbHelper_proItem dbHelper;
    private SQLiteDatabase mDb;
    private DeleteDialog deleteDialog;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        dbHelper = new DbHelper_proItem(ProgramActivity.this);
        mDb =dbHelper.getWritableDatabase();

        getData();

        adapter=new RecyclerAdapter_program(ProgramActivity.this,list);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProgramActivity.this));
        recyclerView.setAdapter(adapter);

        button = findViewById(R.id.imageAddButton);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick (View view){
                dialog = new ProgramSelectDialog(ProgramActivity.this);
                dialog.setDialogListener(new ProgramSelectListener() {  // DialogListener 를 구현 추상 클래스이므로 구현 필수 -> dialog의 값을 전달 받음
                    @Override
                    public void onPositiveClicked(String activity) {
                        addToList(activity);
                    }
                });
                dialog.show();
            }
        });
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
                final int id = (int) viewHolder.itemView.getTag();
                deleteDialog = new DeleteDialog(ProgramActivity.this);
                deleteDialog.setDialogListener(new DeleteDialogListener() {
                    @Override
                    public void onPositiveClicked() {
                        if(removeProgram(id)) {
                            adapter.removeItem(id);
                            adapter.notifyDataSetChanged();
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onNegativeClicked(){
                        adapter.notifyDataSetChanged();
                    }
                });
                deleteDialog.setCancelable(false);

                deleteDialog.show();
            }
        }).attachToRecyclerView(recyclerView);  //리사이클러뷰에 itemTouchHelper 를 붙인다.

    }

    private boolean removeProgram(final int id) {
        Log.d("id:",""+id);
        String activity="";
        if(id==1)
            activity="무분할";
        else
            activity=id+"분할";
        return mDb.delete(ProgramItem.Entry.TABLE_NAME, ProgramItem.Entry.COLUMN_ACTIVITY + "='" + activity+"'", null)>0;
    }



    public void addToList(String activity){
        ContentValues cv = new ContentValues();
        for(int i=1; i<=5; i++){
            if(activity.equals(i+"분할")) {
                break;
            }
            else if(activity.equals("무분할")) {
                break;
            }
        }
        Cursor c = mDb.rawQuery("select * from "+ProgramItem.Entry.TABLE_NAME+" where "+ProgramItem.Entry.COLUMN_ACTIVITY+"='"+activity+"'",null);
        if(c.getCount()>0) {
            Toast.makeText(ProgramActivity.this, "이미 해당 분할이 존재합니다. 스와이프하여 삭제하세요.", Toast.LENGTH_LONG).show();
            c.close();
            return;
        }else {
            cv.put(ProgramItem.Entry.COLUMN_ACTIVITY, activity);
            mDb.insert(ProgramItem.Entry.TABLE_NAME,null,cv);
            switch (activity){
                case "무분할":
                    adapter.addItem(new PartData("무분할", R.drawable.one));
                    break;
                case "2분할":
                    adapter.addItem(new PartData("2분할", R.drawable.two));
                    break;
                case "3분할":
                    adapter.addItem(new PartData("3분할", R.drawable.three));
                    break;
                case "4분할":
                    adapter.addItem(new PartData("4분할", R.drawable.four));
                    break;
                case "5분할":
                    adapter.addItem(new PartData("5분할", R.drawable.five));
                    break;
                case "6분할":
                    adapter.addItem(new PartData("6분할", R.drawable.six));
                    break;
                case "7분할":
                    adapter.addItem(new PartData("7분할", R.drawable.seven));
                    break;
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void getData() {
        list = new ArrayList<>();
        Cursor c = mDb.rawQuery("select * from "+ ProgramItem.Entry.TABLE_NAME,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do {
                String item = c.getString(c.getColumnIndex(ProgramItem.Entry.COLUMN_ACTIVITY));
                switch (item) {
                    case "무분할":
                        list.add(new PartData("무분할", R.drawable.one));
                        break;
                    case "2분할":
                        list.add(new PartData("2분할", R.drawable.two));
                        break;
                    case "3분할":
                        list.add(new PartData("3분할", R.drawable.three));
                        break;
                    case "4분할":
                        list.add(new PartData("4분할", R.drawable.four));
                        break;
                    case "5분할":
                        list.add(new PartData("5분할", R.drawable.five));
                        break;
                    case "6분할":
                        list.add(new PartData("4분할", R.drawable.six));
                        break;
                    case "7분할":
                        list.add(new PartData("5분할", R.drawable.seven));
                        break;
                }
            }while (c.moveToNext());
        }
        c.close();

    }
}
