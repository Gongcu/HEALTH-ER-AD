package com.health.myapplication.RecommendActivity;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.health.myapplication.R;
import com.health.myapplication.adapter.RecyclerAdapter_rec;
import com.health.myapplication.data.ExerciseData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends Fragment {
    private Context mContext;
    private static final String TAG="RECOMMEND FRAGMENT";
    private TextView textView;
    private RecyclerView recyclerView;
    private RecyclerAdapter_rec adapter;
    private LinearLayoutManager layoutManager;

    private ArrayList<ExerciseData> list;
    private ArrayList<ArrayList<String>> list_image;

    private int TYPE;
    private int LEVEL;
    private static final int BEGINNER=0;
    private static final int NOVICE=1;
    private static final int INTERMEDIATE=2;
    private static final int EXPERT=3;

    private AdView mAdView;


    public RecommendFragment() {
    }

    public RecommendFragment(Context context, int TYPE, int LEVEL) {
        mContext=context;
        this.TYPE = TYPE;
        this.LEVEL = LEVEL;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recommend, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        textView = view.findViewById(R.id.textView);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        switch (LEVEL) {
            case BEGINNER:
                getData();
                textView.setText("전신 운동");
                adapter = new RecyclerAdapter_rec(list, list_image);
                recyclerView.setAdapter(adapter);
                break;
            case NOVICE:
                getNoviceData();
                switch (TYPE) {
                    case 1:
                        textView.setText("가슴, 등");
                        break;
                    case 2:
                        textView.setText("하체, 어깨");
                        break;
                }
                adapter = new RecyclerAdapter_rec(list, list_image);
                recyclerView.setAdapter(adapter);
                break;
            case INTERMEDIATE:
                getIntermediateData();
                switch (TYPE) {
                    case 1:
                        textView.setText("가슴, 삼두");
                        break;
                    case 2:
                        textView.setText("등, 이두");
                        break;
                    case 3:
                        textView.setText("하체, 어깨");
                        break;
                }
                adapter = new RecyclerAdapter_rec(list, list_image);
                recyclerView.setAdapter(adapter);
                break;
            case EXPERT:
                getExpertData();
                switch (TYPE) {
                    case 1:
                        textView.setText("가슴, 삼두");
                        break;
                    case 2:
                        textView.setText("등, 이두");
                        break;
                    case 3:
                        textView.setText("어깨, 복근");
                        break;
                    case 4:
                        textView.setText("하체");
                        break;
                }
                adapter = new RecyclerAdapter_rec(list, list_image);
                recyclerView.setAdapter(adapter);
                break;

        }
    }

    private void getData() {
        String json = null;
        list = new ArrayList<>();
        list_image = new ArrayList<>();
        //Context context;
        try {
            InputStream is = mContext.getAssets().open("exercise.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("무분할");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                ExerciseData item = new ExerciseData(o.getString("name"), o.getString("desc"), o.getString("tip")
                ,o.getInt("set"),o.getInt("rep"));
                list.add(item);

                list_image.add(new ArrayList<String>());
                list_image.get(i).add(o.getString("imageR"));
                list_image.get(i).add(o.getString("imageF"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getNoviceData() {
        String json = null;
        list = new ArrayList<>();
        list_image = new ArrayList<>();
        //Context context;
        try {
            InputStream is = mContext.getAssets().open("exercise.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("초보자"+TYPE);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                ExerciseData item = new ExerciseData(o.getString("name"), o.getString("desc"), o.getString("tip")
                        ,o.getInt("set"),o.getInt("rep"));
                list.add(item);

                list_image.add(new ArrayList<String>());
                list_image.get(i).add(o.getString("imageR"));
                list_image.get(i).add(o.getString("imageF"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getExpertData() {
        String json = null;
        list = new ArrayList<>();
        list_image = new ArrayList<>();
        //Context context;
        try {
            InputStream is = mContext.getAssets().open("exercise.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("4분할"+TYPE);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                ExerciseData item = new ExerciseData(o.getString("name"), o.getString("desc"), o.getString("tip")
                        ,o.getInt("set"),o.getInt("rep"));
                list.add(item);

                list_image.add(new ArrayList<String>());
                list_image.get(i).add(o.getString("imageR"));
                list_image.get(i).add(o.getString("imageF"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getIntermediateData() {
        String json = null;
        list = new ArrayList<>();
        list_image = new ArrayList<>();
        //Context context;
        try {
            InputStream is = mContext.getAssets().open("exercise.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("3분할"+TYPE);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                ExerciseData item = new ExerciseData(o.getString("name"), o.getString("desc"), o.getString("tip")
                        ,o.getInt("set"),o.getInt("rep"));
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
