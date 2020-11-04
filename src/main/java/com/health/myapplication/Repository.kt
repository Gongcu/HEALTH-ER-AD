package com.health.myapplication

import android.app.Application
import androidx.lifecycle.LiveData
import com.health.myapplication.DbHelper.BodyWeightDatabase
import com.health.myapplication.dao.BodyWeightDao
import com.health.myapplication.model.BodyWeight

class Repository(application: Application) {
    private val bodyWeightDatabase: BodyWeightDatabase = BodyWeightDatabase.getInstance(application)!!
    private val bodyWeightDao: BodyWeightDao = bodyWeightDatabase.BodyWeightDao()
    private val bodyWeights:LiveData<List<BodyWeight>> = bodyWeightDao.getAll()

    fun getWeightDao():BodyWeightDao {
        return bodyWeightDao
    }
}