package com.health.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.health.myapplication.R
import com.health.myapplication.application.GlobalApplication
import com.health.myapplication.model.GuidePost
import com.health.myapplication.view_model.CommunityViewModel
import kotlinx.android.synthetic.main.activity_community_write.*

class CommunityWriteActivity : AppCompatActivity() {
    val viewModel : CommunityViewModel by viewModels()
    val EXERCISE: String by lazy{
        intent.extras.getString("EXERCISE")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_write)

        close_btn.setOnClickListener {
            finish()
        }
        success_btn.setOnClickListener {
            Log.d("asdf",(application as GlobalApplication).userId.toInt().toString())
            val guidePost = GuidePost(
                    (application as GlobalApplication).userId.toInt(),
                    EXERCISE,
                    title_edit_text.text.toString(),
                    content_edit_text.text.toString()
            )
            viewModel.writePost(guidePost)
            finish()
        }
    }
}