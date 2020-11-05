package com.health.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.health.myapplication.model.BodyWeight
import com.health.myapplication.model.CustomProgram

@Dao
interface CustomProgramDao {
    @Query("SELECT * FROM program ORDER BY activity")
    fun getAll(): LiveData<List<CustomProgram>>

    @Query("SELECT * FROM program WHERE _id = :id")
    fun getCustomProgramById(id:Int): CustomProgram

    @Query("SELECT * FROM program WHERE activity like :activity")
    fun isExist(activity:String): CustomProgram?

    @Insert
    fun insert(customProgram: CustomProgram)

    @Update
    fun update(customProgram: CustomProgram)

    @Delete
    fun delete(customProgram: CustomProgram)

    @Query("DELETE FROM program WHERE _id = :id")
    fun deleteById(id: Int)
}