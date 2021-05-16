package com.health.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.health.myapplication.entity.custom_program.CustomProgramExercise

@Dao
interface CustomProgramExerciseDao {
    @Query("SELECT * FROM program WHERE activity =:activity AND divide=:divide ORDER BY itemorder ASC")
    fun getCustomProgramExercise(activity: Int, divide:Int): LiveData<List<CustomProgramExercise>>

    @Query("SELECT * FROM program WHERE activity =:activity AND divide=:divide ORDER BY itemorder ASC")
    fun getGeneralItems(activity: Int, divide:Int): List<CustomProgramExercise>

    @Query("SELECT max(itemorder) FROM program WHERE activity =:activity AND divide=:divide")
    fun getMaxItemOrder(activity: Int, divide:Int): Int


    @Query("SELECT * FROM program WHERE activity=:activity AND divide=:divide AND exercise=:exercise")
    fun exerciseExistCheck(activity: Int, divide: Int, exercise: String) : CustomProgramExercise?

    @Insert
    fun insert(customProgramItem: CustomProgramExercise)

    @Query("UPDATE program SET itemorder=:itemorder WHERE _id=:id")
    fun updateItemOrderById(id: Int, itemorder: Int)

    @Transaction
    fun swapOrder(fromId: Int, fromItemorder: Int, toId:Int, toItemorder:Int){
        updateItemOrderById(fromId, toItemorder)
        updateItemOrderById(toId, fromItemorder)
    }



    @Query("DELETE FROM program WHERE _id=:id")
    fun deleteById(id: Int)
}