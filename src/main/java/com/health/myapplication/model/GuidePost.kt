package com.health.myapplication.model

data class GuidePost(
        var id: Int?,
        val userId:Int,
        val exercise:String,
        val title:String,
        val content:String,
        val createdAt:String?,
        val likeCount:Int=0,
        val commentCount:Int=0
){
    constructor(userId: Int,exercise: String,title: String,content: String)
    :this(null,userId, exercise, title, content, null)
}