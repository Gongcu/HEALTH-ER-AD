package com.health.myapplication.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.health.myapplication.model.GuideItem
import com.health.myapplication.model.GuidePost
import com.health.myapplication.repository.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityViewModel(application: Application): AndroidViewModel(application) {
    private val repository = Repository(application)


    fun getList(exercise:String): Call<List<GuideItem>> {
        return repository.getList(exercise)
    }


    fun writePost(guidePost: GuidePost) {
        return repository.writePost(guidePost)
    }

    fun getPost(id: Int) :Call<GuidePost>{
        return repository.getPost(id)
    }
}