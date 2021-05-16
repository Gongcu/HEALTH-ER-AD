package com.health.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.health.myapplication.entity.one_rm.OneRmRecord

@Dao
interface OneRmRecordDao {
    @Query("SELECT * FROM onermTable")
    fun getRecords(): LiveData<List<OneRmRecord>>

    @Query("SELECT * FROM onermTable WHERE dateId =:dateId")
    suspend fun getRecordsByDateId(dateId: Int): List<OneRmRecord>

    @Insert
    fun insert(oneRMRecord: OneRmRecord)

    @Query("UPDATE onermTable SET weight=:weight WHERE _id=:id")
    fun update(id: Int, weight: Double)

    @Query("DELETE FROM onermTable WHERE _id=:id")
    fun deleteById(id : Int)

    @Query("SELECT * FROM onermTable WHERE exercise LIKE :exercise AND dateId=:dateId")
    fun find(exercise :String, dateId: Int) : OneRmRecord?
}