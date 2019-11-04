package com.health.myapplication.view_pager;


import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.health.myapplication.activity_program.ProgramFragment;


public class ContentsPagerAdapter_pro extends FragmentStatePagerAdapter {
    private Context mContext;
    private int mPageCount;
    private int ACTIVITY;

    public ContentsPagerAdapter_pro(FragmentManager fm, int pageCount, Context context, int ACTIVITY) {
        super(fm);
        this.mPageCount = pageCount;
        mContext = context;
        this.ACTIVITY = ACTIVITY;
    }



    @Override

    public Fragment getItem(int position) {
        return ProgramFragment.newInstance(position+1, ACTIVITY);
    }

    @Override
    public int getCount() {
        return mPageCount;
    }

}