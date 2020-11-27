package com.health.myapplication.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.health.myapplication.model.Comment
import com.health.myapplication.model.GuideItem
import com.health.myapplication.model.GuidePost
import com.health.myapplication.repository.Repository
import retrofit2.Call

class CommunityViewModel(application: Application): AndroidViewModel(application) {
    private val repository = Repository(application)


    fun getList(exercise:String): Call<List<GuideItem>> {
        return repository.getList(exercise)
    }
    fun getHotPostList(exercise:String): Call<List<GuideItem>> {
        return repository.getHotPostList(exercise)
    }

    fun writePost(guidePost: GuidePost) {
        return repository.writePost(guidePost)
    }

    fun getPost(id: Int) :Call<GuidePost>{
        return repository.getPost(id)
    }

    fun getComment(postId:Int,userId: Int):Call<List<Comment>>{
        return repository.getComment(postId,userId)
    }

    fun writeComment(postId:Int, guideComment: Comment):Call<List<Comment>>{
        return repository.writeComment(postId,guideComment)
    }

    fun likePost(postId:Int, userId:Int):Call<Boolean>{
        return repository.likePost(postId,userId)
    }

    fun likeComment(comment:Int, userId:Int):Call<Boolean>{
        return repository.likeComment(comment,userId)
    }

    fun replyComment(postId:Int, commentId: Int,comment: Comment):Call<List<Comment>>{
        return repository.replyComment(postId,commentId,comment)
    }

    fun getLikeState(postId: Int,userId:Int): Call<Boolean>{
        return repository.getLikeState(postId,userId)
    }
}