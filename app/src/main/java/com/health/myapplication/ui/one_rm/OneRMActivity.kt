package com.health.myapplication.ui.one_rm

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.health.myapplication.R
import com.health.myapplication.util.Util.Companion.createTabView
import com.health.myapplication.view_pager.ContentsPagerAdapter
import kotlinx.android.synthetic.main.activity_one_rm.*

class OneRMActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_rm)
        tab_layout.addTab(tab_layout.newTab().setCustomView(createTabView("차트", this)))
        tab_layout.addTab(tab_layout.newTab().setCustomView(createTabView("기록", this)))

        view_pager.adapter = ContentsPagerAdapter(this, tab_layout.tabCount,ContentsPagerAdapter.ONE_RM_ACTIVITY)

        view_pager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tab_layout.getTabAt(position)!!.select()
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