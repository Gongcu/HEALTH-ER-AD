package com.health.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.health.myapplication.entity.one_rm.OneRmDate

@Dao
interface OneRmDateDao {
    @Query("SELECT * FROM caldateTable GROUP BY date")
    fun getAllLiveDate() : LiveData<List<OneRmDate>>

    @Query("SELECT * FROM caldateTable GROUP BY date")
    fun getAllDate() : List<OneRmDate>

    @Query("SELECT * FROM caldateTable WHERE date like :date")
    fun getDate(date: String) : OneRmDate?

    @Insert
    fun insert(oneRMDate: OneRmDate) : Long

    @Query("DELETE FROM caldateTable WHERE _id=:id")
    fun deleteById(id : Int)
}