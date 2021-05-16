package com.health.myapplication.ui.recommend_program.program_info

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.AdRequest
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.health.myapplication.R
import com.health.myapplication.ui.custom_program.CustomProgramViewModel
import com.health.myapplication.util.Util
import com.health.myapplication.util.Util.createTabView
import com.health.myapplication.view_pager.ContentsPagerAdapter
import kotlinx.android.synthetic.main.activity_program_info.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProgramInfoActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_program_info)
        val RECOMMEND_PROGRAM: Int = intent!!.extras.getInt("PROGRAM")

        ad_view.loadAd(AdRequest.Builder().build())

        for (i in 1..RECOMMEND_PROGRAM)
            tab_layout.addTab(tab_layout.newTab().setCustomView(createTabView("${i}일차", this)))
        view_pager.adapter = ContentsPagerAdapter(
            this,
            tab_layout.tabCount,
            RECOMMEND_PROGRAM,
            ContentsPagerAdapter.PROGRAM_RECOMMEND_ACTIVITY
        )


        view_pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(i: Int) {
                tab_layout.getTabAt(i)!!.select()
            }
        })
        tab_layout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                view_pager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

}