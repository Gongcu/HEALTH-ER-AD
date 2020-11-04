package com.health.myapplication.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.health.myapplication.R

class Util {
    companion object {
        fun createTabView(tabName: String, context: Context): View {
            val tabView: View = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
            val textName = tabView.findViewById<View>(R.id.txt_name) as TextView
            textName.text = tabName
            return tabView
        }
    }
}