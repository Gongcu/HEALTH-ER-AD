package com.health.myapplication.view_pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.health.myapplication.ui.body_weight.chart.BodyWeightChartFragment
import com.health.myapplication.ui.body_weight.record.BodyWeightRecordFragment
import com.health.myapplication.ui.custom_program.program_info.CustomProgramFragment
import com.health.myapplication.ui.one_rm.chart.OneRmChartFragment
import com.health.myapplication.ui.one_rm.record.OneRmRecordFragment
import com.health.myapplication.ui.recommend_program.program_info.RecommendFragment
import com.health.myapplication.ui.record.calendar.RecordCalendarFragment
import com.health.myapplication.ui.record.today.RecordTodayFragment

class ContentsPagerAdapter(fa: FragmentActivity, private val mPageCount: Int, private val ACTIVITY_NUMBER: Int) : FragmentStateAdapter(fa) {
    private var ID = -1

    constructor(fa: FragmentActivity, mPageCount: Int, LEVEL: Int, ACTIVITY_NUMBER: Int) : this(fa, mPageCount, ACTIVITY_NUMBER) {
        this.ID = LEVEL
    }

    override fun createFragment(position: Int): Fragment {
        if(ACTIVITY_NUMBER== BODY_WEIGHT_ACTIVITY){
            return when (position) {
                0 -> BodyWeightChartFragment()
                else -> BodyWeightRecordFragment()
            }
        }else if(ACTIVITY_NUMBER== CUSTOM_PROGRAM_ACTIVITY){
            return CustomProgramFragment.newInstance(position + 1, ID)
        }else if(ACTIVITY_NUMBER== RECORD_ACTIVITY){
            return when(position){
                0 -> RecordTodayFragment()
                else -> RecordCalendarFragment()
            }
        }else if(ACTIVITY_NUMBER== ONE_RM_ACTIVITY){
            return when(position){
                0 ->  OneRmChartFragment()
                else ->  OneRmRecordFragment()
            }
        }
        else{//PROGRAM_RECOMMEND_ACTIVITY
            return RecommendFragment.newInstance(position + 1, ID)
        }
    }

    override fun getItemCount(): Int {
        return mPageCount
    }

    companion object{
        const val BODY_WEIGHT_ACTIVITY = 600
        const val CUSTOM_PROGRAM_ACTIVITY = 300
        const val PROGRAM_RECOMMEND_ACTIVITY = 200
        const val RECORD_ACTIVITY = 400
        const val GUIDE_ACTIVITY = 100
        const val ONE_RM_ACTIVITY = 700
    }
}