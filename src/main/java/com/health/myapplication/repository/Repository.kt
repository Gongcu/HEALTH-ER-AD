package com.health.myapplication.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.health.myapplication.dao.*
import com.health.myapplication.db.*
import com.health.myapplication.model.BodyWeight
import com.health.myapplication.retrofit.CommunityService
import com.health.myapplication.retrofit.Retrofit

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
}