package com.health.myapplication.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.health.myapplication.R;
import com.health.myapplication.adapter.RecyclerAdapter;
import com.health.myapplication.data.PartData;

import java.util.Arrays;
import java.util.List;

;

public class ExerciseCategoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excercise_category);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        init();
        getData();
    }

    private void init() {
        recyclerView = findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        List<String> partName = Arrays.asList("가슴", "등", "하체", "어깨", "복근", "이두", "삼두");
        List<Integer> listResId = Arrays.asList(
                R.drawable.chest,
                R.drawable.back,
                R.drawable.thigh,
                R.drawable.shoulder,
                R.drawable.abdominal,
                R.drawable.biceps,
                R.drawable.triceps);
        for (int i = 0; i < partName.size(); i++) {
            PartData data = new PartData();
            data.setPartName(partName.get(i)); //데이타에 이름지정
            data.setResId(listResId.get(i));

            //data를 adapter에 추가가
            adapter.addItem(data);

        }
        //값 변경됨을 알림, data 노출
        adapter.notifyDataSetChanged();
    }

}
