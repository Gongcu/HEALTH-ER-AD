package com.health.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.health.myapplication.R
import com.health.myapplication.databinding.ActivityMainBinding
import com.health.myapplication.ui.body_weight.BodyWeightActivity
import com.health.myapplication.ui.one_rm.OneRmActivity
import com.health.myapplication.ui.custom_program.CustomProgramActivity
import com.health.myapplication.ui.exercise_info.ExercisePartCategoryActivity
import com.health.myapplication.ui.recommend_program.ProgramRecommendActivity
import com.health.myapplication.ui.record.RecordActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var binding : ActivityMainBinding
    private val adRequest = AdRequest.Builder().build()

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initAds()

        viewModel.minutesStr.observe(this) {
            minuteEditText.setText(it)
        }
        viewModel.secondsStr.observe(this) {
            secondEditText.setText(it)
        }

        setOnClickListenerToView()
    }

    private fun initAds(){
        MobileAds.initialize(this,resources.getString(R.string.admob_app_id))
        adView.loadAd(adRequest)
    }

    override fun onClick(v: View?) {
        var intent : Intent? = null
        when(v?.id){
            R.id.weight_info_view ->
                intent = Intent(this@MainActivity, ExercisePartCategoryActivity::class.java)
            R.id.custom_program_view ->
                intent = Intent(this@MainActivity, CustomProgramActivity::class.java)
            R.id.daily_note_view ->
                intent = Intent(this@MainActivity, RecordActivity::class.java)
            R.id.calculator_view ->
                intent = Intent(this@MainActivity, OneRmActivity::class.java)
            R.id.body_weight_view ->
                intent = Intent(this@MainActivity, BodyWeightActivity::class.java)
            R.id.recommend_program_view ->
                intent = Intent(this@MainActivity, ProgramRecommendActivity::class.java)
        }
        if(intent!=null)
            startActivity(intent)
    }

    fun setOnClickListenerToView(){
        weight_info_view.setOnClickListener(this)
        custom_program_view.setOnClickListener(this)
        daily_note_view.setOnClickListener(this)
        calculator_view.setOnClickListener(this)
        body_weight_view.setOnClickListener(this)
        recommend_program_view.setOnClickListener(this)
    }
}
