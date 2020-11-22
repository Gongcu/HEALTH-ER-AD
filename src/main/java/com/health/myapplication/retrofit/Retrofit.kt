package com.health.myapplication.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit{
    private var instance : Retrofit? = null
    fun getInstance():Retrofit{
        if(instance==null){
            instance = Retrofit.Builder()
                    .baseUrl("http://133.186.212.78:3000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return instance!!
    }
}