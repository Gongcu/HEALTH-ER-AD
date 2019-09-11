package com.health.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.health.myapplication.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var adView : AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.health.myapplication.R.layout.activity_main)

         MobileAds.initialize(this) { }

        adView = findViewById<View>(R.id.adView) as AdView
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        val intent = Intent(this, LoadingActivity::class.java)
        startActivity(intent)

        view1.setOnClickListener {
            val intent = Intent(this@MainActivity, ExerciseCategoryActivity::class.java)
            startActivity(intent)
        }
        view2.setOnClickListener {
            val intent = Intent(this@MainActivity, ProgramActivity::class.java)
            startActivity(intent)
        }
        view3.setOnClickListener {
            val intent = Intent(this@MainActivity, ExerciseDataActivity::class.java)
            startActivity(intent)
        }
        view4.setOnClickListener {
            val intent = Intent(this@MainActivity, CalculatorActivity::class.java)
            startActivity(intent)
        }
        view5.setOnClickListener {
            val intent = Intent(this@MainActivity, BodyWeightActivity::class.java)
            startActivity(intent)
        }
        view6.setOnClickListener {
            val intent = Intent(this@MainActivity, RecommendActivity::class.java)
            startActivity(intent)
        }
    }
}
