package com.health.myapplication.ui.exercise_info

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.health.myapplication.R
import com.health.myapplication.model.etc.ExerciseModel
import com.health.myapplication.ui.common_adapter.CommonItemAdapter
import com.health.myapplication.util.JsonParser
import com.health.myapplication.util.Util
import kotlinx.android.synthetic.main.activity_exercise_part_category.*
import kotlinx.android.synthetic.main.activity_exercise_part_category.ad_view

class ExerciseCategoryActivity : AppCompatActivity() {
    private val part : String by lazy {
        intent!!.extras!!.getString("part")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_part_category)

        ad_view.loadAd(AdRequest.Builder().build())

        part_name_text_view.text = part

        recycler_view.adapter = CommonItemAdapter(
            JsonParser.getPartExercise(applicationContext,part)
        ) { basicModel ->
            Util.startExerciseGuideActivity(applicationContext, basicModel as ExerciseModel)
        }
    }

}