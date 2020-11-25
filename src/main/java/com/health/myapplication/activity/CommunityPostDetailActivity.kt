package com.health.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.health.myapplication.R
import com.health.myapplication.model.GuidePost
import com.health.myapplication.view_model.CommunityViewModel
import kotlinx.android.synthetic.main.activity_community_post_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityPostDetailActivity : AppCompatActivity() {
    private val viewModel :CommunityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_post_detail)

        viewModel.getPost(intent.extras.getInt("ID")).enqueue(object: Callback<GuidePost>{
            override fun onResponse(call: Call<GuidePost>, response: Response<GuidePost>) {
                if(response.isSuccessful){
                    val item = response.body()!!
                    title_text_view.text = item.title
                    content_text_view.text = item.content
                    datetime_text_view.text = item.createdAt
                    like_count_text_view.text = item.likeCount.toString()
                    comment_count_text_view.text = item.commentCount.toString()
                }
                Log.d("RESPONSE",response.body().toString())

            }
            override fun onFailure(call: Call<GuidePost>, t: Throwable) {
                Log.d("FAILURE",t.message)
            }
        })

        back_btn.setOnClickListener {
            finish()
        }
    }
}