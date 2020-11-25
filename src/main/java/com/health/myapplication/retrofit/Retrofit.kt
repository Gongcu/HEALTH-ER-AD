package com.health.myapplication.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit{
    private var instance : Retrofit? = null
    fun getInstance():Retrofit{
        if(instance==null){
            instance = Retrofit.Builder()
                    .baseUrl("http://211.176.83.66:3000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return instance!!
    }
}