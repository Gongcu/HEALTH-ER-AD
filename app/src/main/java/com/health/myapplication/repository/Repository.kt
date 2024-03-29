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

    private val customProgramExerciseDatabase: CustomProgramExerciseDatabase = CustomProgramExerciseDatabase.getInstance(application)!!
    private val customProgramExerciseDao: CustomProgramExerciseDao = customProgramExerciseDatabase.customProgramExerciseDao()

    private val oneRMDateDatabase: OneRmDateDatabase = OneRmDateDatabase.getInstance(application)!!
    private val oneRMDateDao: OneRmDateDao = oneRMDateDatabase.oneRMDateDao()

    private val oneRMRecordDatabase: OneRmRecordDatabase = OneRmRecordDatabase.getInstance(application)
    private val oneRMRecordDao: OneRmRecordDao = oneRMRecordDatabase.oneRMRecordDao()

    private val recordDateDatabase: RecordDateDatabase = RecordDateDatabase.getInstance(application)!!
    private val recordDateDao: RecordDateDao = recordDateDatabase.recordDateDao()

    private val recordDatabase: RecordDatabase = RecordDatabase.getInstance(application)!!
    private val recordDao: RecordDao = recordDatabase.recordDao()




    fun getOneRMDateDao():OneRmDateDao {
        return oneRMDateDao
    }

    fun getOneRMRecordDao():OneRmRecordDao {
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

    fun getCustomProgramExerciseDao():CustomProgramExerciseDao {
        return customProgramExerciseDao
    }

}