package com.health.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.health.myapplication.model.Record
import com.health.myapplication.model.RecordDate
import kotlinx.coroutines.*

class RecordViewModel(application: Application): AndroidViewModel(application) {
    private val recordDateDao = Repository(application).getRecordDateDao()
    private val recordDao = Repository(application).getRecordDao()

    fun getAllDate(): LiveData<List<RecordDate>>{
        return recordDateDao.getAll()
    }
    fun getAllRecord(): LiveData<List<Record>>{
        return recordDao.getAll()
    }
    fun getIdByDate(date:String) :Int?{
        return recordDateDao.getRecordDateIdByDate(date)?.id
    }
    fun getAllRecordByDatedId(dateId:Int) :LiveData<List<Record>>?{
        return recordDao.getAllRecordByDateId(dateId)
    }
    suspend fun getAllRecordByDate(date:String) :LiveData<List<Record>>?{
        var dateId :Int? = null
        val job = viewModelScope.launch(Dispatchers.IO) {
            dateId= recordDateDao.getRecordDateIdByDate(date)?.id
        }
        job.join()
        if(dateId == null)
            return null
        else
            return recordDao.getAllRecordByDateId(dateId!!)
    }

    suspend fun getAllGeneralListRecordByDate(date:String) :List<Record>?{
        var dateId :Int? = null
        val job = viewModelScope.launch(Dispatchers.IO) {
            dateId= recordDateDao.getRecordDateIdByDate(date)?.id
        }
        job.join()
        if(dateId == null)
            return null
        else
            return recordDao.getGeneralListRecordByDateId(dateId!!)
    }


    suspend fun getAllRecordByDateAtCalendar(date:String) :LiveData<List<Record>>?{
        var dateId = recordDateDao.getRecordDateIdByDate(date)?.id
        if(dateId == null)
            return null
        else
            return recordDao.getAllRecordByDateId(dateId!!)
    }

    fun insert(date:String, exercise: String,set: Int,rep: Int,weight: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val dateId:Int? = recordDateDao.getRecordDateIdByDate(date)?.id
            if(dateId==null){
                recordDateDao.insert(RecordDate(null,date))
                val dateId = recordDateDao.getRecordDateIdByDate(date)!!.id
                recordDao.insert(Record(null,exercise,rep,set,weight,dateId))
            }else {
                recordDao.insert(Record(null, exercise, rep, set, weight, dateId))
            }
        }
    }

    fun updateRecord(id:Int,exercise:String, set: Int, rep: Int,  weight:Double) {
        viewModelScope.launch(Dispatchers.IO) {
            recordDao.update(id,exercise,set,rep,weight)
        }
    }
    fun deleteRecordById(id:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val recordDatedId = recordDao.getRecordById(id).dateid
            recordDao.deleteById(id)
            if(recordDao.getCount(recordDatedId!!)==0)
                recordDateDao.deleteById(recordDatedId!!)
        }
    }

    fun delete(recordDate: RecordDate){
        viewModelScope.launch(Dispatchers.IO) {
            recordDateDao.delete(recordDate)
        }
    }
    fun deleteById(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            recordDateDao.deleteById(id)
        }
    }
}