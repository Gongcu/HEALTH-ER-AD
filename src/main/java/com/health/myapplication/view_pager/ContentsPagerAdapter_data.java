package com.health.myapplication.view_pager;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.health.myapplication.fragment.Data_CalendarFragment;
import com.health.myapplication.fragment.Data_DataFragment;
import com.health.myapplication.fragment.Data_DayFragment;


public class ContentsPagerAdapter_data extends FragmentStatePagerAdapter {

    private int mPageCount;

    public ContentsPagerAdapter_data(FragmentManager fm, int pageCount) {
        super(fm);
        this.mPageCount = pageCount;
    }



    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Data_CalendarFragment dataCalendarFragment = new Data_CalendarFragment();
                //notifyDataSetChanged();
                return dataCalendarFragment;

            case 1:
                Data_DayFragment dataDayFragment = new Data_DayFragment();
                //notifyDataSetChanged();
                return dataDayFragment;

            case 2:
                Data_DataFragment dataDataFragment = new Data_DataFragment();
                //notifyDataSetChanged();
                return dataDataFragment;
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
        if (object instanceof Data_CalendarFragment) {
            // Create a new method notifyUpdate() in your fragment
            // it will get call when you invoke
            // notifyDatasetChaged();
            ((Data_CalendarFragment) object).update();
        } else if((object instanceof Data_DayFragment)){
            ((Data_DayFragment) object).update();
        }else if((object instanceof Data_DataFragment)){
            ((Data_DataFragment) object).update();
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