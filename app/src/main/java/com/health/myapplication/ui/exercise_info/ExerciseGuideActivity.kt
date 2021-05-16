package com.health.myapplication.ui.exercise_info

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdRequest
import com.health.myapplication.R
import com.health.myapplication.entity.etc.ExerciseVo
import com.health.myapplication.view_pager.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_exercise_guide.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.timer

class ExerciseGuideActivity : AppCompatActivity() {
    private var currentPage: Int = 0
    private val exercise: ExerciseVo by lazy {
        intent.extras!!.get("exercise") as ExerciseVo
    }

    private val imgDrawables by lazy {
        arrayOf(
            ContextCompat.getDrawable(this, exercise.imageR),
            ContextCompat.getDrawable(this, exercise.imageF)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_guide)
        ad_view.loadAd(AdRequest.Builder().build())
        exercise_name_text_view.text = "${exercise.name} 자세"
        how_to_text_view.text = exercise.desc
        tip_text_view.text = "Tip. ${exercise.tip}"


        view_pager.adapter = ViewPagerAdapter(this, imgDrawables)

        tab_layout.setupWithViewPager(view_pager, true)

        timer(period = 1500) {
            lifecycleScope.launch(Dispatchers.Main) {
                if (view_pager != null) {
                    currentPage = view_pager.currentItem
                    if (currentPage == 0) {
                        view_pager.setCurrentItem(1, true)
                    } else {
                        view_pager.setCurrentItem(0, true)
                    }
                }
            }
        }
    }

}