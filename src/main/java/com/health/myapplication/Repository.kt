package com.health.myapplication

import android.app.Application
import androidx.lifecycle.LiveData
import com.health.myapplication.db.BodyWeightDatabase
import com.health.myapplication.db.CustomProgramDatabase
import com.health.myapplication.dao.BodyWeightDao
import com.health.myapplication.dao.CustomProgramDao
import com.health.myapplication.model.BodyWeight

class Repository(application: Application) {
    private val bodyWeightDatabase: BodyWeightDatabase = BodyWeightDatabase.getInstance(application)!!
    private val bodyWeightDao: BodyWeightDao = bodyWeightDatabase.BodyWeightDao()
    private val bodyWeights:LiveData<List<BodyWeight>> = bodyWeightDao.getAll()
    private val customProgramDatabase: CustomProgramDatabase = CustomProgramDatabase.getInstance(application)!!
    private val customProgramDao: CustomProgramDao = customProgramDatabase.customProgramDao()


    fun getWeightDao():BodyWeightDao {
        return bodyWeightDao
    }
    fun getCustomProgramDao():CustomProgramDao {
        return customProgramDao
    }
}