package com.health.myapplication.activity_program;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.health.myapplication.DbHelper.DbHelper_dialog;
import com.health.myapplication.R;
import com.health.myapplication.data.DialogContract;
import com.health.myapplication.dialog.ProgramAlertDialog;
import com.health.myapplication.view_pager.ContentsPagerAdapter_pro;

public class OneDayProgram extends AppCompatActivity {
    private static final int ACTIVITY = 1;
    private Context mContext;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ContentsPagerAdapter_pro mContentsPagerAdapter;
    private boolean ALERT;
    private SQLiteDatabase mDb;
    private DbHelper_dialog dbHelper;
    private ProgramAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert);
        mContext = this;

        final ShowAlert alert = new ShowAlert(mContext);

        mViewPager = (ViewPager) findViewById(R.id.pager_content);
        mTabLayout = (TabLayout) findViewById(R.id.layout_tab);
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(createTabView("1일차")));

        mContentsPagerAdapter = new ContentsPagerAdapter_pro(
                getSupportFragmentManager(), mTabLayout.getTabCount(), OneDayProgram.this,ACTIVITY);

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
