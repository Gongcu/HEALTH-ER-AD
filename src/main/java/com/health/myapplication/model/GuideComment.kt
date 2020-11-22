package com.health.myapplication.model

data class GuideComment(
        var id: Int?,
        val userId:String,
        val postId:String,
        val comment:String,
        val datetime:String,
        val likeCount:Int,
        val dislikeCount:Int
){
    constructor(userId: String,postId: String,comment: String,datetime: String,likeCount: Int,dislikeCount:Int)
    :this(null,userId, postId, comment, datetime, likeCount, dislikeCount)
}