package com.health.myapplication.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.health.myapplication.R;
import com.health.myapplication.view_pager.ViewPagerAdapter;


public class InformationActivity extends AppCompatActivity {
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private TextView exerciseNameTextView;
    private TextView howToTextView;
    private TextView tipTextView;

    private String name;
    private String desc;
    private String tip;
    private String imageR_str;
    private String imageF_str;

    private int images[];
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        exerciseNameTextView = findViewById(R.id.exerciseNameTextView);
        howToTextView = findViewById(R.id.howToTextView);
        tipTextView = findViewById(R.id.tipTextView);
        viewPager = findViewById(R.id.view);

        Intent intent = getIntent();
        name = intent.getExtras().getString("name");
        desc = intent.getExtras().getString("desc");
        tip = intent.getExtras().getString("tip");
        imageR_str = intent.getExtras().getString("imageR");
        imageF_str = intent.getExtras().getString("imageF");

        int id = getResources().getIdentifier(imageR_str, "drawable", getPackageName());
        Drawable drawable1 = getResources().getDrawable(id);
        int id2 = getResources().getIdentifier(imageF_str, "drawable", getPackageName());
        Drawable drawable2 = getResources().getDrawable(id2);

        Drawable drawable[] = {drawable1, drawable2};
        Log.d(drawable1.toString(), drawable2.toString());
        exerciseNameTextView.setText(name + " 자세");
        howToTextView.setText(desc);
        tipTextView.setText("Tip. " + tip);

        adapter = new ViewPagerAdapter(this, drawable);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);

    }
}
