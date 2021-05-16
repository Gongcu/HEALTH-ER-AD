package com.health.myapplication.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.health.myapplication.R
import com.health.myapplication.ui.exercise_info.ExerciseGuideActivity
import com.health.myapplication.entity.etc.DialogContract
import com.health.myapplication.db.DbHelper_dialog
import com.health.myapplication.dialog.ProgramAlertDialog
import com.health.myapplication.entity.etc.ExerciseVo
import java.text.SimpleDateFormat
import java.util.*


object Util {

    fun createTabView(tabName: String, context: Context): View {
        val tabView: View = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        val textName = tabView.findViewById<View>(R.id.txt_name) as TextView
        textName.text = tabName
        return tabView
    }

    @SuppressLint("SimpleDateFormat")
    fun getDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(Date())
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

    fun startExerciseGuideActivity(context: Context, model: ExerciseVo) {
        val intent = Intent(context, ExerciseGuideActivity::class.java)
        intent.putExtra("exercise", model)
        context.startActivity(intent)
    }


    fun showAlert(context: Context) {
        val dbHelper = DbHelper_dialog(context)
        val mDb = dbHelper.writableDatabase
        val c: Cursor = mDb.rawQuery("select * from " + DialogContract.Entry.TABLE_NAME, null)
        if (c.getCount() === 0) {
            val dialog = ProgramAlertDialog(context)
            dialog.show()
        }
        c.close()
    }
}