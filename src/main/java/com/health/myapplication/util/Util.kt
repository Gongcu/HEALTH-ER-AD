package com.health.myapplication.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.health.myapplication.R
import java.text.SimpleDateFormat
import java.util.*

class Util {
    companion object {
        fun createTabView(tabName: String, context: Context): View {
            val tabView: View = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
            val textName = tabView.findViewById<View>(R.id.txt_name) as TextView
            textName.text = tabName
            return tabView
        }

        fun getDate():String{
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            return  sdf.format(Date())
        }

        fun dayToString(dayNum: Int): String? {
            var day: String? = null
            when (dayNum) {
                1 -> day = "Sun"
                2 -> day = "Mon"
                3 -> day = "Tue"
                4 -> day = "Wed"
                5 -> day = "Thu"
                6 -> day = "Fri"
                7 -> day = "Sat"
            }
            return day
        }
    }
}