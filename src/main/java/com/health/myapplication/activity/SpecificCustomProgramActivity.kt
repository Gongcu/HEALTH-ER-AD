package com.health.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.ads.AdRequest
import com.health.myapplication.R
import kotlinx.android.synthetic.main.activity_specific_custom_program.*

class SpecificCustomProgramActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specific_custom_program)
        ad_view.loadAd(AdRequest.Builder().build())

    }
}