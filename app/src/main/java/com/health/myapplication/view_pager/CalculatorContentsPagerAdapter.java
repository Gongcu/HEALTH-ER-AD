package com.health.myapplication.view_pager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.health.myapplication.ui.one_rm.chart.CalculatorChartFragment;
import com.health.myapplication.ui.one_rm.record.CalculatorDataFragment;


public class CalculatorContentsPagerAdapter extends FragmentStatePagerAdapter {

    private int mPageCount;

    public CalculatorContentsPagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.mPageCount = pageCount;

    }



    @Override

    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CalculatorChartFragment calculatorChartFragment = new CalculatorChartFragment();
                notifyDataSetChanged();
                return calculatorChartFragment;

            case 1:
                CalculatorDataFragment calculatorDataFragment = new CalculatorDataFragment();
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
        if (object instanceof CalculatorChartFragment) {
            // Create a new method notifyUpdate() in your fragment
            // it will get call when you invoke
            // notifyDatasetChaged();
            ((CalculatorChartFragment) object).update();
        } else if((object instanceof CalculatorDataFragment)){
            ((CalculatorDataFragment) object).update();
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