package com.health.myapplication.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.material.tabs.TabLayout
import com.health.myapplication.R
import com.health.myapplication.view_pager.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_exercise_guide.*

class ExerciseGuideActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_guide)

        ad_view.loadAd(AdRequest.Builder().build())

        exercise_name_text_view.text = "${intent.extras!!.getString("name")} 자세"
        how_to_text_view.text = intent.extras!!.getString("desc")
        tip_text_view.text = "Tip. ${intent.extras!!.getString("tip")}"

        val drawable = arrayOf(
                ContextCompat.getDrawable(applicationContext, intent.extras!!.getInt("imageR")),
                ContextCompat.getDrawable(applicationContext, intent.extras!!.getInt("imageF"))
        )
        view_pager.adapter = ViewPagerAdapter(applicationContext,drawable)

        tab_layout.setupWithViewPager(view_pager, true)
    }
}