package com.health.myapplication.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.health.myapplication.model.BodyWeight
import com.health.myapplication.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class BodyWeightViewModel(application: Application): AndroidViewModel(application) {
    private val repository = Repository(application)

    fun getWeight(): LiveData<List<BodyWeight>>{
        return repository.getWeightDao().getAll()
    }

    fun findAndInsert(weight: Double){
        viewModelScope.launch(Dispatchers.IO) {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val strDate = sdf.format(Date())
            if(repository.getWeightDao().getCurrentDate(strDate)!=null){
                repository.getWeightDao().update(weight,strDate)
            }else{
                repository.getWeightDao().insert(BodyWeight(weight,strDate))
            }
        }
    }

    fun insert(bodyWeight: BodyWeight){
        repository.getWeightDao().insert(bodyWeight)
    }
    fun update(weight: Double,date: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWeightDao().update(weight, date)
        }
    }
    fun delete(bodyWeight: BodyWeight){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWeightDao().delete(bodyWeight)
        }
    }
}