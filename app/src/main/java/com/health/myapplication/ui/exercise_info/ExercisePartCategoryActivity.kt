package com.health.myapplication.ui.exercise_info

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.health.myapplication.R
import com.health.myapplication.model.etc.BaseModel
import com.health.myapplication.ui.common_adapter.CommonItemAdapter
import kotlinx.android.synthetic.main.activity_exercise_part_category.*
import kotlin.collections.ArrayList

class ExercisePartCategoryActivity : AppCompatActivity() {
    private val adRequest = AdRequest.Builder().build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_part_category)
        ad_view.loadAd(adRequest)

        recycler_view.adapter = CommonItemAdapter(
                getExercisePartCategoryData()
        ) { basicModel ->
            val intent = Intent(applicationContext, ExerciseCategoryActivity::class.java)
            intent.putExtra("part", basicModel.text)
            startActivity(intent)
        }
    }

    private fun getExercisePartCategoryData() =
        listOf(
            BaseModel("가슴",R.drawable.chest),
            BaseModel("등",R.drawable.back),
            BaseModel("하체",R.drawable.thigh),
            BaseModel("어깨",R.drawable.shoulder),
            BaseModel("복근",R.drawable.abdominal),
            BaseModel("이두",R.drawable.biceps),
            BaseModel("삼두",R.drawable.triceps)
        )
}