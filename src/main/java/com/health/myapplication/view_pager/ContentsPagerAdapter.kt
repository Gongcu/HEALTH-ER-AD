package com.health.myapplication.view_pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.health.myapplication.fragment.*

class ContentsPagerAdapter(fa: FragmentActivity, private val mPageCount: Int, private val ACTIVITY_NUMBER: Int) : FragmentStateAdapter(fa) {
    private var ID = -1

    constructor(fa: FragmentActivity, mPageCount: Int, LEVEL: Int, ACTIVITY_NUMBER: Int) : this(fa, mPageCount, ACTIVITY_NUMBER) {
        this.ID = LEVEL
    }

    override fun createFragment(position: Int): Fragment {
        if(ACTIVITY_NUMBER== BODY_WEIGHT_ACTIVITY){
            return when (position) {
                0 -> BodyWeightChartFragment()
                else -> BodyWeightDataFragment()
            }
        }else if(ACTIVITY_NUMBER== CUSTOM_PROGRAM_ACTIVITY){
            return ProgramFragment.newInstance(position + 1, ID)
        }else if(ACTIVITY_NUMBER== RECORD_ACTIVITY){
            return when(position){
                0 -> RecordTodayFragment()
                else -> RecordCalendarFragment()
            }
        }else if(ACTIVITY_NUMBER== GUIDE_ACTIVITY){
            return when(position){
                0 -> ExerciseGuideFragment()
                else -> ExerciseGuideCommunityFragment()
            }
        }else{//PROGRAM_RECOMMEND_ACTIVITY
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
    }
}