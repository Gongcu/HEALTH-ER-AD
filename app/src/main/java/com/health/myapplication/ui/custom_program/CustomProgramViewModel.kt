package com.health.myapplication.ui.custom_program

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.health.myapplication.entity.custom_program.CustomProgram
import com.health.myapplication.entity.custom_program.CustomProgramExercise
import com.health.myapplication.repository.Repository
import kotlinx.coroutines.*

class CustomProgramViewModel(application: Application): AndroidViewModel(application) {
    private val customProgramDao = Repository(application).getCustomProgramDao()
    private val customProgramExerciseDao = Repository(application).getCustomProgramExerciseDao()

    val errorAlert = MutableLiveData<Error>()
    /**
     * Program
     * */

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

    /**
     * ProgramExercise
     * */
    fun getCustomProgramExercise(activityNumber: Int, divide: Int): LiveData<List<CustomProgramExercise>>{
        return customProgramExerciseDao.getCustomProgramExercise(activityNumber, divide)
    }

    fun insert(customProgramExercise: CustomProgramExercise, activityNumber: Int, divide: Int){
        viewModelScope.launch {
            var isExist = false
            var order = -1
            val job = viewModelScope.launch(Dispatchers.IO) {
                isExist = customProgramExerciseDao.exerciseExistCheck(activityNumber,divide,customProgramExercise.exercise) !=null
            }
            job.join()
            if(isExist) {
                errorAlert.postValue(Error.EXIST)
                return@launch
            }
            viewModelScope.launch(Dispatchers.IO) {
                order = customProgramExerciseDao.getMaxItemOrder(activityNumber,divide) + 1
                customProgramExercise.itemorder = order
                customProgramExerciseDao.insert(customProgramExercise)
            }
        }
    }

    fun updateList(list : List<CustomProgramExercise>){
        viewModelScope.launch(Dispatchers.IO) {
            for(i in list.indices){
                customProgramExerciseDao.updateItemOrderById(list[i].id!!,i+1)
            }
        }
    }

    fun delete(exerciseId :Int){
        viewModelScope.launch(Dispatchers.IO) {
            customProgramExerciseDao.deleteById(exerciseId)
        }
    }


    enum class Error{
        EXIST
    }
}