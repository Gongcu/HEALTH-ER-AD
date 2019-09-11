package com.health.myapplication.view_pager;


import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.health.myapplication.RecommendActivity.RecommendFragment;


public class ContentsPagerAdapter_rec extends FragmentStatePagerAdapter {
    private Context mContext;
    private int mPageCount;
    private int LEVEL;

    public ContentsPagerAdapter_rec(FragmentManager fm, int pageCount, Context context, int LEVEL) {
        super(fm);
        this.mPageCount = pageCount;
        mContext = context;
        this.LEVEL = LEVEL;
    }



    @Override

    public Fragment getItem(int position) {
        return new RecommendFragment(mContext, position+1, LEVEL);
    }

    @Override
    public int getCount() {
        return mPageCount;
    }

/*
    @Override
    public int getItemPosition(Object object) {
        if (object instanceof RecommendFragment) {
            // Create a new method notifyUpdate() in your fragment
            // it will get call when you invoke
            // notifyDatasetChaged();
           // ((RecommendFragment) object).update();
        } else if((object instanceof RecommendFragment)){
            //((RecommendFragment) object).update();
        }
        //don't return POSITION_NONE, avoid fragment recreation.
        return super.getItemPosition(object);
    }*/

}