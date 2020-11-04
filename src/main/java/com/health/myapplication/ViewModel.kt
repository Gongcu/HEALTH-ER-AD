package com.health.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.health.myapplication.model.BodyWeight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel(application: Application): AndroidViewModel(application) {
    private val repository = Repository(application)

    fun getWeight(): LiveData<List<BodyWeight>>{
        return repository.getWeightDao().getAll()
    }
    fun getCurrentDate(date: String): BodyWeight?{
        return repository.getWeightDao().getCurrentDate(date)
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