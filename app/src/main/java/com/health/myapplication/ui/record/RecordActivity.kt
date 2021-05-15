package com.health.myapplication.ui.record

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.AdRequest
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.health.myapplication.R
import com.health.myapplication.util.Util
import com.health.myapplication.view_pager.ContentsPagerAdapter
import kotlinx.android.synthetic.main.activity_record.ad_view
import kotlinx.android.synthetic.main.activity_record.tab_layout
import kotlinx.android.synthetic.main.activity_record.view_pager

class RecordActivity : FragmentActivity() {
    private val context:Context by lazy { this }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        ad_view.loadAd(AdRequest.Builder().build())

        tab_layout.addTab(tab_layout.newTab().setCustomView(Util.createTabView("오늘의 기록",context)))
        tab_layout.addTab(tab_layout.newTab().setCustomView(Util.createTabView("캘린더",context)))

        view_pager.adapter = ContentsPagerAdapter(this, tab_layout.tabCount,ContentsPagerAdapter.RECORD_ACTIVITY)

        view_pager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(i: Int) {
                tab_layout.getTabAt(i)!!.select()
            }
        })
        tab_layout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                view_pager.currentItem=tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
    
}