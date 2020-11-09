package com.health.myapplication.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.health.myapplication.R
import com.health.myapplication.adapter.BasicAdapter
import com.health.myapplication.model.ExerciseModel
import com.health.myapplication.util.Util
import kotlinx.android.synthetic.main.activity_exercise_guide.*
import kotlinx.android.synthetic.main.activity_exercise_part_category.*
import kotlinx.android.synthetic.main.activity_exercise_part_category.ad_view
import org.json.JSONObject

class ExerciseCategoryActivity : AppCompatActivity() {
    private val PART_NAME : String by lazy {
        intent!!.extras!!.getString("part")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_part_category)

        ad_view.loadAd(AdRequest.Builder().build())

        part_name_text_view.text = PART_NAME
        recycler_view.layoutManager = LinearLayoutManager(this)

        recycler_view.adapter = BasicAdapter(
                getExerciseCategoryData(PART_NAME)
        ) { basicModel ->
            Util.startExerciseGuideActivity(applicationContext, basicModel as ExerciseModel)
        }
    }

    private fun getExerciseCategoryData(partName: String) :List<ExerciseModel> {
        var json: String? = null
        val exerciseList = ArrayList<ExerciseModel>()
        try {
            val inputStream = assets.open("exercise.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, charset("UTF-8"))
            val jsonObject = JSONObject(json)
            val jsonArray = jsonObject.getJSONArray(partName) //partName으로 된 json array get
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                exerciseList.add(ExerciseModel(
                        obj.getString("name"),
                        obj.getString("desc"),
                        obj.getString("tip"),
                        applicationContext.resources.getIdentifier(obj.getString("imageR"), "drawable", applicationContext.packageName),
                        applicationContext.resources.getIdentifier(obj.getString("imageF"), "drawable", applicationContext.packageName)
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            return exerciseList
        }
    }
}