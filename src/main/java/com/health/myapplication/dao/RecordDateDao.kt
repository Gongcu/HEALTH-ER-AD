package com.health.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.health.myapplication.model.BodyWeight
import com.health.myapplication.model.RecordDate

@Dao
interface RecordDateDao {
    @Query("SELECT * FROM dateTable ORDER BY date DESC")
    fun getAll(): LiveData<List<RecordDate>>

    @Query("SELECT * FROM dateTable WHERE date like :date limit 1")
    fun getDate(date:String): RecordDate?

    @Query("SELECT * FROM dateTable WHERE date like :date limit 1")
    fun getRecordDateIdByDate(date:String): RecordDate?

    @Insert
    fun insert(recordDate: RecordDate)

    @Delete
    fun delete(recordDate: RecordDate)

    @Query("DELETE FROM dateTable WHERE _id=:id")
    fun deleteById(id: Int)
}