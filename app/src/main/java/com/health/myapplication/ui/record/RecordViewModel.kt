package com.health.myapplication.ui.record

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.health.myapplication.entity.etc.DialogType
import com.health.myapplication.listener.RecordDialogListener
import com.health.myapplication.listener.DateRecordCopyListener
import com.health.myapplication.entity.record.Record
import com.health.myapplication.entity.record.RecordDate
import com.health.myapplication.repository.Repository
import com.health.myapplication.ui.record.dialog.RecordDialog
import kotlinx.coroutines.*
import java.util.ArrayList

class RecordViewModel(application: Application): AndroidViewModel(application) {
    private val recordDateDao = Repository(application).getRecordDateDao()
    private val recordDao = Repository(application).getRecordDao()


    private fun getRecentDate(): List<RecordDate>{
        return recordDateDao.getRecentDate()
    }

    fun getAllRecord(): LiveData<List<Record>>{
        return recordDao.getAll()
    }
    fun getIdByDate(date:String) :Int?{
        return recordDateDao.getRecordDateIdByDate(date)?.id
    }

    suspend fun getAllGeneralListRecordByDate(date:String) :List<Record>?{
        var dateId :Int? = null
        val job = viewModelScope.launch(Dispatchers.IO) {
            dateId= recordDateDao.getRecordDateIdByDate(date)?.id
        }
        job.join()
        Log.d("dateId",dateId.toString())
        if(dateId == null)
            return null
        else
            return recordDao.getGeneralListRecordByDateId(dateId!!)
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
    fun copyInsert(date:String, selectedDateId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val todayDateId:Int? = recordDateDao.getRecordDateIdByDate(date)?.id
            if(todayDateId==null){
                val todayDateId = recordDateDao.insert(RecordDate(null,date))
                recordDao.copyRecord(selectedDateId,todayDateId.toInt())
            }else {
                recordDao.copyRecord(selectedDateId,todayDateId!!)
            }
        }
    }

    fun updateRecord(id:Int, set: Int, rep: Int,  weight:Double) {
        viewModelScope.launch(Dispatchers.IO) {
            recordDao.update(id,set,rep,weight)
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



    suspend fun showAddDialog(context: Context, targetDate:String){
        val list:ArrayList<RecordDate> =  ArrayList(viewModelScope.async(Dispatchers.IO) {
            return@async getRecentDate()
        }.await())
        val dialog = RecordDialog(context, DialogType.INSERT,list)
        dialog.setRecordDialogListener(object : RecordDialogListener {
            override fun onPositiveClicked(time: String, name: String, set: Int, rep: Int, weight: Double) {
                insert(targetDate, name, set, rep, weight)
            }
        })
        dialog.setDateRecordCopyListener(object: DateRecordCopyListener {
            override fun onDateSelected(selectedDateId: Int) {
                copyInsert(targetDate,selectedDateId)
            }
        })
        viewModelScope.launch {
            dialog.show()
        }
    }
}