package com.health.myapplication.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.health.myapplication.dao.BodyWeightDao
import com.health.myapplication.dao.CustomProgramDao
import com.health.myapplication.dao.RecordDao
import com.health.myapplication.dao.RecordDateDao
import com.health.myapplication.db.BodyWeightDatabase
import com.health.myapplication.db.CustomProgramDatabase
import com.health.myapplication.db.RecordDatabase
import com.health.myapplication.db.RecordDateDatabase
import com.health.myapplication.model.CommunityUser
import com.health.myapplication.model.GuideItem
import com.health.myapplication.model.GuidePost
import com.health.myapplication.retrofit.CommunityService
import com.health.myapplication.retrofit.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Repository(application: Application) {
    private val bodyWeightDatabase: BodyWeightDatabase = BodyWeightDatabase.getInstance(application)!!
    private val bodyWeightDao: BodyWeightDao = bodyWeightDatabase.BodyWeightDao()

    private val customProgramDatabase: CustomProgramDatabase = CustomProgramDatabase.getInstance(application)!!
    private val customProgramDao: CustomProgramDao = customProgramDatabase.customProgramDao()

    private val recordDateDatabase: RecordDateDatabase = RecordDateDatabase.getInstance(application)!!
    private val recordDateDao: RecordDateDao = recordDateDatabase.recordDateDao()

    private val recordDatabase: RecordDatabase = RecordDatabase.getInstance(application)!!
    private val recordDao: RecordDao = recordDatabase.recordDao()

    private val retrofit = Retrofit.getInstance()
    private val guideApi = retrofit.create(CommunityService::class.java)

    fun getGuideApi():CommunityService{
        return guideApi
    }

    fun getRecordDao():RecordDao {
        return recordDao
    }

    fun getRecordDateDao():RecordDateDao {
        return recordDateDao
    }

    fun getWeightDao():BodyWeightDao {
        return bodyWeightDao
    }
    fun getCustomProgramDao():CustomProgramDao {
        return customProgramDao
    }

    fun getList(exercise: String): Call<List<GuideItem>> {
        return guideApi.getPostList(exercise)
    }

    fun writePost(guidePost: GuidePost){
        guideApi.writePost(guidePost).enqueue(object:Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.code()==200) Log.d("REPO",response.body().toString())
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("WRITE_POST",t.message)
            }
        })
    }

    fun postUser(user: CommunityUser){
        guideApi.postUser(user).enqueue(object:Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.code()==200) Log.d("REPO",response.body().toString())
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("POST_USER",t.message)
            }
        })
    }
    fun getPost(id:Int):Call<GuidePost>{
        return guideApi.getPostById(id)
    }
}