package com.health.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.health.myapplication.model.custom_program.CustomProgramItem

@Dao
interface CustomProgramItemDao {
    @Query("SELECT * FROM program WHERE activity =:activity AND divide=:divide ORDER BY itemorder ASC")
    fun getItems(activity: Int, divide:Int): LiveData<List<CustomProgramItem>>

    @Query("SELECT * FROM program WHERE activity =:activity AND divide=:divide ORDER BY itemorder ASC")
    fun getGeneralItems(activity: Int, divide:Int): List<CustomProgramItem>

    @Query("SELECT max(itemorder) FROM program WHERE activity =:activity AND divide=:divide")
    fun getMaxItemOrder(activity: Int, divide:Int): Int

    @Insert
    fun insert(customProgramItem: CustomProgramItem)

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