package com.health.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.health.myapplication.model.etc.Alarm

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm ORDER BY _id")
    fun getAll(): LiveData<List<Alarm>>

    @Query("SELECT * FROM alarm ORDER BY _id DESC LIMIT 1")
    fun getCurrentAlarm(): Alarm?

    @Insert
    fun insert(alarm: Alarm)


    @Query("UPDATE alarm SET minute=:minute,second=:second WHERE _id=:id")
    fun update(id: Int, minute: Int, second: Int)

    @Delete
    fun delete(alarm: Alarm)

    @Query("DELETE FROM alarm WHERE _id=:id")
    fun deleteById(id: Int)

    fun upsert(minute: Int, second: Int){
        val alarm = getCurrentAlarm()
        if(alarm!=null)
            update(alarm.id!!,minute,second)
        else
            insert(Alarm(minute, second))
    }
}