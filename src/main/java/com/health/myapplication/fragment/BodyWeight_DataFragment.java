package com.health.myapplication.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.health.myapplication.DbHelper.DbHelper_weight;
import com.health.myapplication.R;
import com.health.myapplication.adapter.RecyclerAdapter_weight;
import com.health.myapplication.data.WeightContract;
import com.health.myapplication.dialog.BodyWeightDialog;
import com.health.myapplication.listener.AdapterListener;
import com.health.myapplication.listener.DialogListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class BodyWeight_DataFragment extends Fragment {
    private ArrayList<WeightContract> list;

    private BodyWeightDialog dialog;
    private RecyclerAdapter_weight mAdapter;
    private RecyclerView recyclerView;
    private Button button;
    // SQL DB의 레퍼런스
    private SQLiteDatabase mDb;

    private String date = "";
    private double weight = 0.0;

    private AdView mAdView;
    public BodyWeight_DataFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bodyweight_data, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        DbHelper_weight dbHelper = new DbHelper_weight(getActivity());
        mDb = dbHelper.getWritableDatabase(); //데이터에 db 채우기 위함

        button = view.findViewById(R.id.AddButton);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new RecyclerAdapter_weight(getContext(), getAllWeight());
        recyclerView.setAdapter(mAdapter);

        mAdapter.setListener(new AdapterListener(){
            @Override
            public void deleteSuccess() {
                update();
            }
            @Override
            public void dateDeleteSuccess(String date) {
            }
        });

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new BodyWeightDialog(getActivity(),false);
                dialog.setDialogListener(new DialogListener() {  // DialogListener 를 구현 추상 클래스이므로 구현 필수 -> dialog의 값을 전달 받음
                    @Override
                    public void onPositiveClicked(int date, String part, String exercise) {}
                    @Override
                    public void onPositiveClicked(String time, String name, int set, int rep) {
                    }

                    @Override
                    public void onPositiveClicked() {
                    }

                    @Override
                    public void onPositiveClicked(double height, double weight) {
                        setResult(height, weight);
                        addToNote(recyclerView);
                    }

                    //이거 수정 가능성 높음
                    @Override
                    public void onNegativeClicked() {
                    }
                });
                dialog.show();
            }
        });


    }


    public void update(){
        if(mAdapter!=null)
            mAdapter.swapCursor(getAllWeight());
    }


    //for dialog listener
    private void setResult(double height, double weight) {
        this.weight = weight;
    }

    public void setRecyclerView(RecyclerView id){
        recyclerView=id;
    }

    private Cursor getAllWeight() {
        // 두번째 파라미터 (Projection array)는 여러 열들 중에서 출력하고 싶은 것만 선택해서 출력할 수 있게 한다.
        // 모든 열을 출력하고 싶을 때는 null 을 입력한다.
        return mDb.query(
                WeightContract.WeightEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WeightContract.WeightEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }

    // 등록하기 버튼을 눌렀을 때 불리는 메서드
    public void addToNote(View view) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues cv = new ContentValues();
        Date date = new Date();
        String strDate = sdf.format(date);
        Cursor c = mDb.rawQuery("select * from "+ WeightContract.WeightEntry.TABLE_NAME+" where "+ WeightContract.WeightEntry.COLUMN_DATE+
        "='"+strDate+"'",null);
        if(c.getCount()>0) {
            Toast.makeText(getActivity(), "이미 오늘 입력한 몸무게가 있습니다. 꾹 눌러 편집 하세요.", Toast.LENGTH_SHORT).show();
            c.close();
            return;
        }else {
            cv.put(WeightContract.WeightEntry.COLUMN_DATE, strDate);
            cv.put(WeightContract.WeightEntry.COLUMN_HEIGHT, 0);
            cv.put(WeightContract.WeightEntry.COLUMN_WEIGHT, weight);
            mDb.insert(WeightContract.WeightEntry.TABLE_NAME, null, cv);
            mAdapter.swapCursor(getAllWeight());
        }
    }



}
