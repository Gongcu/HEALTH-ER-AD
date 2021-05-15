package com.health.myapplication.model.etc


open class BaseModel (
        val id: Int?,
        val text: String,
        var resId: Int?
){
        constructor(text: String, resId: Int):this(null,text,resId)
}