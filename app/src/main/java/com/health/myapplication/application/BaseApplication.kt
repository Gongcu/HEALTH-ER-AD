package com.health.myapplication.application

import android.app.Application
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.health.myapplication.R

class BaseApplication : Application() {
    private val colorMap: Map<String, Int> by lazy {
        mapOf(
                "blue" to ContextCompat.getColor(this, R.color.colorSub4),
                "red" to ContextCompat.getColor(this, R.color.colorSub),
                "black" to ContextCompat.getColor(this, R.color.colorPrimary)
        )
    }

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

    @JvmName("getColorMap1")
    fun getColorMap():Map<String,Int>{
        return colorMap
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