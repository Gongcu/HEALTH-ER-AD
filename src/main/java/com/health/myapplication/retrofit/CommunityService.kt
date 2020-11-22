package com.health.myapplication.retrofit

import com.health.myapplication.model.GuideComment
import com.health.myapplication.model.GuideInfo
import com.health.myapplication.model.GuideItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CommunityService {
    @GET("exercise/:id")
    fun getGuideList(id:Int) : List<GuideItem>

    @GET("exercise/:id")
    fun getGuideById(id:Int) : Call<GuideItem>

    @GET("exercise/:id/comment")
    fun getCommentList(id:Int) : List<GuideComment>


    @POST("exercise")
    fun postGuide(@Body guide: GuideInfo) : Call<Void>

    @POST("exercise/{id}")
    fun postComment(@Body comment: GuideComment) : Call<Void>
}