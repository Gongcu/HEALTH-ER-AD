package com.health.myapplication.ui.custom_program.program_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.AdRequest
import com.google.android.material.tabs.TabLayout
import com.health.myapplication.R
import com.health.myapplication.ui.custom_program.CustomProgramViewModel
import com.health.myapplication.util.Util
import com.health.myapplication.view_pager.ContentsPagerAdapter
import kotlinx.android.synthetic.main.activity_program_info.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomProgramInfoActivity : FragmentActivity() {
    private val viewModel: CustomProgramViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_program_info)
        val ID: Int = intent!!.extras.getInt("ID")

        ad_view.loadAd(AdRequest.Builder().build())
        Util.showAlert(this)
        lifecycleScope.launch(Dispatchers.IO) {
            val item = viewModel.getCustomProgramById(ID)
            val division = item.activity
            lifecycleScope.launch {
                for (i in 1..division)
                    tab_layout.addTab(tab_layout.newTab().setCustomView(Util.createTabView("${i}일차", applicationContext)))
                view_pager.adapter = ContentsPagerAdapter(this@CustomProgramInfoActivity, tab_layout.tabCount, ID, ContentsPagerAdapter.CUSTOM_PROGRAM_ACTIVITY)
            }
        }

        view_pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(i: Int) {
                tab_layout.getTabAt(i)!!.select()
            }
        })
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                view_pager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}