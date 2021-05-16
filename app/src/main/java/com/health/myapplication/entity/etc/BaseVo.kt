package com.health.myapplication.entity.etc


open class BaseVo (
        val id: Int?,
        val text: String,
        var resId: Int?
){
        constructor(text: String, resId: Int):this(null,text,resId)
}