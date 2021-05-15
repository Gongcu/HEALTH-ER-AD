package com.health.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.health.myapplication.model.record.Record

@Dao
interface RecordDao {
    @Query("SELECT * FROM exerciseNote ORDER BY _id")
    fun getAll(): LiveData<List<Record>>

    @Query("SELECT * FROM exerciseNote WHERE _id=:id")
    fun getRecordById(id:Int): Record

    @Query("SELECT * FROM exerciseNote WHERE dateid=:dateId")
    fun getAllRecordByDateId(dateId:Int): LiveData<List<Record>>

    @Query("SELECT * FROM exerciseNote WHERE dateid=:dateId")
    fun getGeneralListRecordByDateId(dateId:Int): List<Record>

    @Query("SELECT Count(*) as count FROM exerciseNote WHERE dateid=:dateId")
    fun getCount(dateId:Int): Int

    @Insert
    fun insert(record: Record)

    @Delete
    fun delete(record: Record)

    @Query("UPDATE exerciseNote SET exercisename=:exercise,settime=:set,rep=:rep,weight=:weight WHERE _id=:id")
    fun update(id: Int, exercise:String, set:Int, rep:Int,  weight:Double)

    @Query("DELETE FROM exerciseNote WHERE _id=:id")
    fun deleteById(id: Int)

    @Query("INSERT INTO exerciseNote(exerciseName,settime,rep, weight,dateid) SELECT exerciseName,settime,rep, weight, :todayDateId FROM exerciseNote WHERE dateid=:dateId")
    fun copyRecord(dateId: Int, todayDateId: Int)

    @Query("DELETE FROM exerciseNote WHERE _id > 0")
    fun deleteAllForTest()
}