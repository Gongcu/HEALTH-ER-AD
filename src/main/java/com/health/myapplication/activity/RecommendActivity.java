package com.health.myapplication.activity;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.health.myapplication.R;
import com.health.myapplication.RecommendActivity.BeginnerActivity;
import com.health.myapplication.RecommendActivity.ExpertActivity;
import com.health.myapplication.RecommendActivity.IntermediateActivity;
import com.health.myapplication.RecommendActivity.NoviceActivity;

public class RecommendActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView cardView;
    private CardView cardView1;
    private CardView cardView2;
    private CardView cardView3;
    private Intent intent;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        cardView = findViewById(R.id.cardview);
        cardView1 = findViewById(R.id.cardview1);
        cardView2 = findViewById(R.id.cardview2);
        cardView3 = findViewById(R.id.cardview3);

        cardView.setOnClickListener(this);
        cardView1.setOnClickListener(this);
        cardView2.setOnClickListener(this);
        cardView3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cardview:
                intent = new Intent(this, BeginnerActivity.class);
                startActivity(intent);
                break;
            case R.id.cardview1:
                intent = new Intent(this, NoviceActivity.class);
                startActivity(intent);
                break;
            case R.id.cardview2:
                intent = new Intent(this, IntermediateActivity.class);
                startActivity(intent);
                break;
            case R.id.cardview3:
                intent = new Intent(this, ExpertActivity.class);
                startActivity(intent);
                break;
        }

    }
}
