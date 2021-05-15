package com.health.myapplication.application

import android.app.Application
import android.widget.Toast

class BaseApplication : Application() {
    var userId : Long = -1


    companion object {
        var instance: BaseApplication? = null

        fun getGlobalApplicationContext() : BaseApplication? {
            checkNotNull(this) { "this application does not inherit com.kakao.GlobalApplication" }
            return instance
        }
    }

    fun makeToast(text:String){
        Toast.makeText(this,text, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }
}