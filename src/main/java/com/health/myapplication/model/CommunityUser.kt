package com.health.myapplication.model

data class CommunityUser(
        var id: Int?,
        val kakaoId: Int,
        val name: String?
){
    constructor(kakaoId: Int):this(null,kakaoId,null)
}