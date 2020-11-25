package com.health.myapplication.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.health.myapplication.R
import com.health.myapplication.util.Util
import com.health.myapplication.view_pager.ContentsPagerAdapter
import com.health.myapplication.view_pager.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_exercise_guide.*

class ExerciseGuideActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_guide)

        tab_layout.addTab(tab_layout.newTab().setCustomView(Util.createTabView("자세",applicationContext)))
        tab_layout.addTab(tab_layout.newTab().setCustomView(Util.createTabView("커뮤니티",applicationContext)))

        view_pager.adapter = ContentsPagerAdapter(this, tab_layout.tabCount, ContentsPagerAdapter.GUIDE_ACTIVITY)

        view_pager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(i: Int) {
                tab_layout.getTabAt(i)!!.select()
            }
        })
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                view_pager.currentItem=tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

}