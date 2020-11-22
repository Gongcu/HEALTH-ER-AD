package com.health.myapplication.model

data class GuideInfo(
        var id: Int?,
        val userId:String,
        val exercise:String,
        val title:String,
        val description:String,
        val datetime:String,
        val like:Int,
        val dislike:Int,
        val viewCount:Int
){
    constructor(userId: String,exercise: String,title: String,description: String,datetime: String,like: Int,dislike:Int,viewCount: Int)
    :this(null,userId, exercise, title, description, datetime, like,dislike, viewCount)
}