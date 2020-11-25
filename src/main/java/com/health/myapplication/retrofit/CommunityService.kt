package com.health.myapplication.retrofit

import com.health.myapplication.model.CommunityUser
import com.health.myapplication.model.GuideComment
import com.health.myapplication.model.GuidePost
import com.health.myapplication.model.GuideItem
import retrofit2.Call
import retrofit2.http.*

interface CommunityService {
    @GET("post/exercise/{exercise}")
    fun getPostList(@Path("exercise") exercise:String) : Call<List<GuideItem>>

    @GET("post/{postId}")
    fun getPostById(@Path("postId") postId:Int) : Call<GuidePost>

    @GET("post/{postId}/comment")
    fun getCommentList(@Path("postId") postId:Int) : Call<List<GuideComment>>

    @POST("user")
    fun postUser(@Body user: CommunityUser) : Call<Void>

    @POST("post")
    fun writePost(@Body guide: GuidePost) : Call<Void>

    @POST("post/comment/{postId}")
    fun writeComment(
            @Path("postId") postId:Int,
            @Body comment: GuideComment) : Call<Void>

    @POST("post/like/{postId}/{userId}")
    fun like(
            @Path("postId") postId:Int,
            @Path("userId") userId:Int) : Call<Void>
}