package com.health.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.health.myapplication.model.calculator.OneRMRecord
import com.health.myapplication.model.etc.Alarm

@Dao
interface OneRMRecordDao {
    @Query("SELECT * FROM onermTable")
    fun getRecords(): LiveData<List<OneRMRecord>>

    @Insert
    fun insert(oneRMRecord: OneRMRecord)

    @Query("SELECT * FROM onermTable WHERE exercise LIKE :exercise AND dateId=:dateId")
    fun find(exercise :String, dateId: Int) : OneRMRecord?
}