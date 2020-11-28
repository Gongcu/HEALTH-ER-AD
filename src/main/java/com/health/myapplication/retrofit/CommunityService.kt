package com.health.myapplication.retrofit

import com.health.myapplication.model.CommunityUser
import com.health.myapplication.model.Comment
import com.health.myapplication.model.GuidePost
import com.health.myapplication.model.GuideItem
import retrofit2.Call
import retrofit2.http.*

interface CommunityService {
    @GET("post/exercise/{exercise}")
    fun getPostList(
            @Path("exercise") exercise:String,
            @Query("offset") offset:Int) : Call<List<GuideItem>>

    @GET("post/exercise/{exercise}/hot")
    fun getHotPostList(@Path("exercise") exercise:String) : Call<List<GuideItem>>

    @GET("post/{postId}")
    fun getPostById(@Path("postId") postId:Int) : Call<GuidePost>

    @GET("post/like/{postId}/{userId}")
    fun getLikeState(
            @Path("postId") postId:Int,
            @Path("userId") userId:Int
            ) : Call<Boolean>

    @GET("post/{postId}/comment/{userId}")
    fun getComment(@Path("postId") postId:Int,
                   @Path("userId") userId:Int) : Call<List<Comment>>

    @POST("user")
    fun postUser(@Body user: CommunityUser) : Call<Void>


    @POST("post")
    fun writePost(@Body guide: GuidePost) : Call<Void>

    @POST("post/{postId}/comment")
    fun writeComment(
            @Path("postId") postId:Int,
            @Body comment: Comment) : Call<List<Comment>>

    @POST("post/{postId}/reply/{commentId}")
    fun replyComment(
            @Path("postId") postId:Int,
            @Path("commentId") commentId:Int,
            @Body comment: Comment) : Call<List<Comment>>

    @PATCH("post/like/{postId}/{userId}")
    fun likePost(
            @Path("postId") postId:Int,
            @Path("userId") userId:Int) : Call<Boolean>

    @PATCH("post/like/comment/{commentId}/{userId}")
    fun likeComment(
            @Path("commentId") commentId:Int,
            @Path("userId") userId:Int) : Call<Boolean>
}