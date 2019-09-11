package com.health.myapplication.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.health.myapplication.DbHelper.DbHelper_Calculator_sub;
import com.health.myapplication.R;
import com.health.myapplication.adapter.RecyclerAdapter_cal;
import com.health.myapplication.dialog.CalculatorDialog;
import com.health.myapplication.view_pager.ContentsPagerAdapter_cal;

import java.util.ArrayList;


public class CalculatorActivity extends AppCompatActivity {
    private Context mContext;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ContentsPagerAdapter_cal mContentsPagerAdapter;

    private LineChart lineChart;
    private int entrySize;
    private int xLabelCount;
    private LineData data;
    private LineDataSet dataSet1, dataSet2, dataSet3;
    private XAxis xAxis;

    private SQLiteDatabase mDb;
    private DbHelper_Calculator_sub dbHelper;

    private CalculatorDialog dialog;
    private Button addBtn;
    private RecyclerView recyclerView;
    private RecyclerAdapter_cal adapter;

    private ArrayList<String> date_list;

    public static Context context;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_rmcalculator);
/*
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        mContext = this;

        mViewPager = (ViewPager) findViewById(R.id.pager_content);
        mTabLayout = (TabLayout) findViewById(R.id.layout_tab);
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(createTabView("차트")));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(createTabView("기록")));

        mContentsPagerAdapter = new ContentsPagerAdapter_cal(
                getSupportFragmentManager(), mTabLayout.getTabCount());

        mViewPager.setAdapter(mContentsPagerAdapter);

        mViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(mTabLayout) {
                }
        );
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                mContentsPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });
    }

    private View createTabView(String tabName) {

        View tabView = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
        TextView txt_name = (TextView) tabView.findViewById(R.id.txt_name);
        txt_name.setText(tabName);
        return tabView;

    }


}