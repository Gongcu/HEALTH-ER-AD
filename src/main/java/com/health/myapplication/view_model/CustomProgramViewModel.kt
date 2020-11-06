package com.health.myapplication.view_model

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.health.myapplication.model.CustomProgram
import com.health.myapplication.repository.Repository
import kotlinx.coroutines.*

class CustomProgramViewModel(application: Application): AndroidViewModel(application) {
    private val customProgramDao = Repository(application).getCustomProgramDao()

    fun getCustomProgram(): LiveData<List<CustomProgram>>{
        return customProgramDao.getAll()
    }

    suspend fun getCustomProgramById(id:Int): CustomProgram {
        return customProgramDao.getCustomProgramById(id)
    }

    fun insert(customProgram: CustomProgram) {
        viewModelScope.launch(Dispatchers.IO) {
            if(customProgramDao.isExist(customProgram.activity)==null) {
                customProgramDao.insert(customProgram)
            }else{
                viewModelScope.launch(Dispatchers.Main) {
                    Toast.makeText(getApplication(), "이미 해당 분할이 존재합니다. 스와이프하여 삭제하세요.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    fun update(customProgram: CustomProgram){
        viewModelScope.launch(Dispatchers.IO) {
            customProgramDao.update(customProgram)
        }
    }
    fun delete(customProgram: CustomProgram){
        viewModelScope.launch(Dispatchers.IO) {
            customProgramDao.delete(customProgram)
        }
    }
    fun deleteById(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            customProgramDao.deleteById(id)
        }
    }
}