package com.health.myapplication.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.health.myapplication.R
import com.health.myapplication.util.Util.Companion.createTabView
import com.health.myapplication.view_pager.ContentsPagerAdapter
import kotlinx.android.synthetic.main.activity_bodyweight.*

class BodyWeightActivity : FragmentActivity() {
    private val context: Context by lazy {this}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bodyweight)

        tab_layout.addTab(tab_layout.newTab().setCustomView(createTabView("차트", context)))
        tab_layout.addTab(tab_layout.newTab().setCustomView(createTabView("기록", context)))

        view_pager.adapter = ContentsPagerAdapter(this, tab_layout.tabCount,ContentsPagerAdapter.BODY_WEIGHT_ACTIVITY)

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