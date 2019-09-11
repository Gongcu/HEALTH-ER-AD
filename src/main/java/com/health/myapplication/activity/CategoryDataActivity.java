package com.health.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.health.myapplication.R;
import com.health.myapplication.adapter.RecyclerAdapter_sub;
import com.health.myapplication.data.ExerciseData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class CategoryDataActivity extends AppCompatActivity {
    private RecyclerAdapter_sub adapter;

    private TextView partNameTextView;
    private RecyclerView recyclerView;

    private String ACTIVITY_PART;

    private ArrayList<ExerciseData> list;
    private ArrayList<ArrayList<String>> list_image;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_data);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Intent intent = getIntent();
        ACTIVITY_PART = intent.getExtras().getString("part");


        list = new ArrayList<>();
        list_image = new ArrayList<>();

        partNameTextView = findViewById(R.id.partNameTextView);
        partNameTextView.setText(ACTIVITY_PART);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getData(ACTIVITY_PART);

        adapter = new RecyclerAdapter_sub(list, list_image);
        recyclerView.setAdapter(adapter);
    }

    private void getData(String part_name) {
        String json = null;
        //Context context;
        try {
            InputStream is = getAssets().open("exercise.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(part_name); //part_name으로 된 json array get

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                ExerciseData item = new ExerciseData(o.getString("name"), o.getString("desc"), o.getString("tip"));
                list.add(item);

                list_image.add(new ArrayList<String>());
                list_image.get(i).add(o.getString("imageR"));
                list_image.get(i).add(o.getString("imageF"));


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
