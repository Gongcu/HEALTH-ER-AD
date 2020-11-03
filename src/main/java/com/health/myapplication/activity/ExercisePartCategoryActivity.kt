package com.health.myapplication.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.health.myapplication.R
import com.health.myapplication.adapter.BasicAdapter
import com.health.myapplication.model.BasicModel
import kotlinx.android.synthetic.main.activity_exercise_part_category.*
import kotlin.collections.ArrayList

class ExercisePartCategoryActivity : AppCompatActivity() {
    private val adRequest = AdRequest.Builder().build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_part_category)
        ad_view.loadAd(adRequest)

        recycler_view.layoutManager = LinearLayoutManager(this)

        recycler_view.adapter = BasicAdapter(
                getExercisePartCategoryData()
        ) { basicModel ->
            val intent = Intent(applicationContext, ExerciseCategoryActivity::class.java)
            intent.putExtra("part", basicModel.text)
            startActivity(intent)
        }
    }

    private fun getExercisePartCategoryData() :List<BasicModel> {
        val list = ArrayList<BasicModel>()
        val partNameArr = listOf("가슴", "등", "하체", "어깨", "복근", "이두", "삼두")
        val resIdArr = listOf(
                R.drawable.chest,
                R.drawable.back,
                R.drawable.thigh,
                R.drawable.shoulder,
                R.drawable.abdominal,
                R.drawable.biceps,
                R.drawable.triceps)
        for (i in partNameArr.indices)
            list.add(BasicModel(partNameArr[i],resIdArr[i]))
        return list
    }
}