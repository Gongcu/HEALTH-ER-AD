package com.health.myapplication.view_pager;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.health.myapplication.fragment.Calculator_ChartFragment;
import com.health.myapplication.fragment.Calculator_DataFragment;


public class ContentsPagerAdapter_cal extends FragmentStatePagerAdapter {

    private int mPageCount;

    public ContentsPagerAdapter_cal(FragmentManager fm, int pageCount) {
        super(fm);
        this.mPageCount = pageCount;

    }



    @Override

    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Calculator_ChartFragment calculatorChartFragment = new Calculator_ChartFragment();
                notifyDataSetChanged();
                return calculatorChartFragment;

            case 1:
                Calculator_DataFragment calculatorDataFragment = new Calculator_DataFragment();
                notifyDataSetChanged();
                return calculatorDataFragment;
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
        if (object instanceof Calculator_ChartFragment) {
            // Create a new method notifyUpdate() in your fragment
            // it will get call when you invoke
            // notifyDatasetChaged();
            ((Calculator_ChartFragment) object).update();
        } else if((object instanceof Calculator_DataFragment)){
            ((Calculator_DataFragment) object).update();
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