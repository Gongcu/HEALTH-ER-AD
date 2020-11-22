package com.health.myapplication.model

data class GuideItem(
        var id:Int?,
        val exercise:String,
        val title:String,
        val datetime:String,
        val likeCount:Int,
        val viewCount:Int,
        val commentCount:Int
){
  constructor(exercise:String,title:String,datetime: String,likeCount: Int,viewCount: Int,commentCount: Int) :
          this(null,exercise,title,datetime,likeCount, viewCount,commentCount)
}