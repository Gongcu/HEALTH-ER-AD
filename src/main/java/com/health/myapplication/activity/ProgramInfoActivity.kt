package com.health.myapplication.activity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.AdRequest
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.health.myapplication.R
import com.health.myapplication.util.Util.Companion.createTabView
import com.health.myapplication.view_pager.ContentsPagerAdapter
import kotlinx.android.synthetic.main.activity_program_info.*

class ProgramInfoActivity : FragmentActivity(){
    private val context:Context by lazy {
        this
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_program_info)
        val RECOMMEND_PROGRAM=intent.extras!!.getInt("PROGRAM")

        ad_view.loadAd(AdRequest.Builder().build())

        for(i in 1..RECOMMEND_PROGRAM){
            tab_layout.addTab(tab_layout.newTab().setCustomView(createTabView("${i}일차",context)))
        }

        view_pager.adapter = ContentsPagerAdapter(this, tab_layout.tabCount, RECOMMEND_PROGRAM,ContentsPagerAdapter.PROGRAM_RECOMMEND_ACTIVITY)
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