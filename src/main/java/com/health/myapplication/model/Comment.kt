package com.health.myapplication.model

data class Comment(
        var id: Int?,
        val userId:Int,
        val comment:String,
        val createdAt:String?,
        val parent:Int?,
        val like:Boolean?,
        var likeCount:Int?,
        val nickname:String?
){
    constructor(userId: Int,comment: String)
    :this(null,userId, comment,null,null,null,null,null)
}