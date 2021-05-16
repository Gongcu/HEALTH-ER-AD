package com.health.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.health.myapplication.entity.body_weight.BodyWeight

@Dao
interface BodyWeightDao {
    @Query("SELECT * FROM weightTable ORDER BY _id")
    fun getAll(): LiveData<List<BodyWeight>>

    @Query("SELECT * FROM weightTable WHERE date like :date limit 1")
    fun getCurrentDate(date:String): BodyWeight?

    @Insert
    fun insert(bodyWeight: BodyWeight)

    @Query("UPDATE weightTable SET weight=:weight WHERE date like :date")
    fun update(weight: Double, date: String)

    @Delete
    fun delete(bodyWeight: BodyWeight)

    @Query("DELETE FROM weightTable WHERE _id=:id")
    fun deleteById(id: Int)
}