package com.health.myapplication.view_pager;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.health.myapplication.fragment.BodyWeight_ChartFragment;
import com.health.myapplication.fragment.BodyWeight_DataFragment;


public class ContentsPagerAdapter extends FragmentStatePagerAdapter {

    private int mPageCount;

    public ContentsPagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.mPageCount = pageCount;

    }



    @Override

    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                BodyWeight_ChartFragment bodyWeightChartFragment = new BodyWeight_ChartFragment();
                notifyDataSetChanged();
                return bodyWeightChartFragment;

            case 1:
                BodyWeight_DataFragment bodyWeightDataFragment = new BodyWeight_DataFragment();
                notifyDataSetChanged();
                return bodyWeightDataFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mPageCount;
    }


    @Override
    public int getItemPosition(Object object) {
        if (object instanceof BodyWeight_ChartFragment) {
            // Create a new method notifyUpdate() in your fragment
            // it will get call when you invoke
            // notifyDatasetChaged();
            ((BodyWeight_ChartFragment) object).changeDataSet();
        } else if((object instanceof BodyWeight_DataFragment)){
            ((BodyWeight_DataFragment) object).update();
        }
        //don't return POSITION_NONE, avoid fragment recreation.
        return super.getItemPosition(object);
    }

    /*
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
/*
    @Override
    public int getItemPosition(@NonNull Object object) {
        Fragment fragment = (Fragment)object;
        object.
        if(object.)
        return POSITION_NONE;
    }
*/
}