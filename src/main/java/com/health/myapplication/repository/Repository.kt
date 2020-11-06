package com.health.myapplication.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.health.myapplication.db.BodyWeightDatabase
import com.health.myapplication.db.CustomProgramDatabase
import com.health.myapplication.dao.BodyWeightDao
import com.health.myapplication.dao.CustomProgramDao
import com.health.myapplication.dao.RecordDao
import com.health.myapplication.dao.RecordDateDao
import com.health.myapplication.db.RecordDatabase
import com.health.myapplication.db.RecordDateDatabase
import com.health.myapplication.model.BodyWeight

class Repository(application: Application) {
    private val bodyWeightDatabase: BodyWeightDatabase = BodyWeightDatabase.getInstance(application)!!
    private val bodyWeightDao: BodyWeightDao = bodyWeightDatabase.BodyWeightDao()

    private val customProgramDatabase: CustomProgramDatabase = CustomProgramDatabase.getInstance(application)!!
    private val customProgramDao: CustomProgramDao = customProgramDatabase.customProgramDao()

    private val recordDateDatabase: RecordDateDatabase = RecordDateDatabase.getInstance(application)!!
    private val recordDateDao: RecordDateDao = recordDateDatabase.recordDateDao()

    private val recordDatabase: RecordDatabase = RecordDatabase.getInstance(application)!!
    private val recordDao: RecordDao = recordDatabase.recordDao()

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