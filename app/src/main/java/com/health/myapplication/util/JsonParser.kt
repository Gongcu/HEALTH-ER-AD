package com.health.myapplication.util

import android.content.Context
import com.health.myapplication.R
import com.health.myapplication.entity.etc.ExerciseVo
import org.json.JSONObject

object JsonParser {
    fun getPartExercise(context: Context, part: String) :List<ExerciseVo>{
        val exerciseList = ArrayList<ExerciseVo>()
        try {
            val inputStream = context.assets.open("exercise.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, charset("UTF-8"))
            val jsonObject = JSONObject(json)
            val jsonArray = jsonObject.getJSONArray(part) //partName으로 된 json array get
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                exerciseList.add(
                    ExerciseVo(
                        obj.getString("name"),
                        obj.getString("desc"),
                        obj.getString("tip"),
                        context.resources.getIdentifier(obj.getString("imageR"), "drawable", context.packageName),
                        context.resources.getIdentifier(obj.getString("imageF"), "drawable", context.packageName)
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            return exerciseList
        }
    }

    fun getRecommendProgramInfo(context: Context, program: String) :List<ExerciseVo>{
        val exerciseList = ArrayList<ExerciseVo>()
        try {
            val inputStream = context.assets.open("exercise.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, charset("UTF-8"))
            val jsonObject = JSONObject(json)
            val jsonArray = jsonObject.getJSONArray(program) //partName으로 된 json array get
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                exerciseList.add(
                    ExerciseVo(
                        obj.getString("name"),
                        obj.getString("desc"),
                        obj.getString("tip"),
                        obj.getInt("set"),
                        obj.getInt("rep"),
                        context.resources.getIdentifier(obj.getString("imageR"), "drawable", context.packageName),
                        context.resources.getIdentifier(obj.getString("imageF"), "drawable", context.packageName)
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            return exerciseList
        }
    }

    fun getExerciseFromJson(context: Context, exercise: String) : ExerciseVo?{
        try {
            val inputStream = context.assets.open("total_exercise.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, charset("UTF-8"))
            val jsonObject = JSONObject(json)
            val jsonArray = jsonObject.getJSONArray("전체운동")
            for (i in 0 until jsonArray.length()) {
                val o = jsonArray.getJSONObject(i)
                if (o.getString("name") == exercise) {
                    return ExerciseVo(
                            o.getString("name"),
                            o.getString("desc"),
                            o.getString("tip"),
                            context.resources.getIdentifier(o.getString("imageR"), "drawable", context.packageName),
                            context.resources.getIdentifier(o.getString("imageF"), "drawable", context.packageName)
                    )
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}