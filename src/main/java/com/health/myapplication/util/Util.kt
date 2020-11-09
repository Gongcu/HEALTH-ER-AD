package com.health.myapplication.util

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.health.myapplication.R
import com.health.myapplication.activity.ExerciseGuideActivity
import com.health.myapplication.model.DialogContract
import com.health.myapplication.db.DbHelper_dialog
import com.health.myapplication.dialog.ProgramAlertDialog
import com.health.myapplication.model.ExerciseModel
import org.json.JSONObject
import java.io.InputStream
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

        fun startExerciseGuideActivity(context: Context, model: ExerciseModel){
            val intent = Intent(context, ExerciseGuideActivity::class.java)
            intent.putExtra("name", model.name)
            intent.putExtra("desc", model.desc)
            intent.putExtra("tip", model.tip)
            intent.putExtra("imageR", model.imageR)
            intent.putExtra("imageF", model.imageF)
            context.startActivity(intent)
        }

        fun getExerciseModel(exercise: String, context: Context) :ExerciseModel{
            var json: String? = null
            try {
                val inputStream: InputStream = context.getAssets().open("total_exercise.json")
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                json = String(buffer, charset("UTF-8"))
                val jsonObject = JSONObject(json)
                val jsonArray = jsonObject.getJSONArray("전체운동")
                for (i in 0 until jsonArray.length()) {
                    val o = jsonArray.getJSONObject(i)
                    if (o.getString("name") == exercise) {
                        val exerciseModel = ExerciseModel(
                                exercise,
                                o.getString("desc"),
                                o.getString("tip"),
                                context.resources.getIdentifier(o.getString("imageR"), "drawable", context.packageName),
                                context.resources.getIdentifier(o.getString("imageR"), "drawable", context.packageName))
                        return exerciseModel
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            //Cannot reach this return statement
            return ExerciseModel("", "", "", 0, 0)
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
}