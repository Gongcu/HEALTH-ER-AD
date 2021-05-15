package com.health.myapplication.repository

import android.app.Application
import com.health.myapplication.dao.*
import com.health.myapplication.db.*


class Repository(application: Application) {
    private val alarmDatabase: AlarmDatabase = AlarmDatabase.getInstance(application)!!
    private val alarmDao: AlarmDao = alarmDatabase.alarmDao()

    private val bodyWeightDatabase: BodyWeightDatabase = BodyWeightDatabase.getInstance(application)!!
    private val bodyWeightDao: BodyWeightDao = bodyWeightDatabase.BodyWeightDao()

    private val customProgramDatabase: CustomProgramDatabase = CustomProgramDatabase.getInstance(application)!!
    private val customProgramDao: CustomProgramDao = customProgramDatabase.customProgramDao()

    private val oneRMDateDatabase: OneRmDateDatabase = OneRmDateDatabase.getInstance(application)!!
    private val oneRMDateDao: OneRMDateDao = oneRMDateDatabase.oneRMDateDao()

    private val oneRMRecordDatabase: OneRmRecordDatabase = OneRmRecordDatabase.getInstance(application)
    private val oneRMRecordDao: OneRMRecordDao = oneRMRecordDatabase.oneRMRecordDao()

    private val recordDateDatabase: RecordDateDatabase = RecordDateDatabase.getInstance(application)!!
    private val recordDateDao: RecordDateDao = recordDateDatabase.recordDateDao()

    private val recordDatabase: RecordDatabase = RecordDatabase.getInstance(application)!!
    private val recordDao: RecordDao = recordDatabase.recordDao()




    fun getOneRMDateDao():OneRMDateDao {
        return oneRMDateDao
    }

    fun getOneRMRecordDao():OneRMRecordDao {
        return oneRMRecordDao
    }

    fun getAlarmDao():AlarmDao {
        return alarmDao
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