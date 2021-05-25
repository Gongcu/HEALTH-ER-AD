package com.health.myapplication.ui.body_weight

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.health.myapplication.listener.BodyWeightDialogListener
import com.health.myapplication.entity.body_weight.BodyWeight
import com.health.myapplication.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class BodyWeightViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository(application)
    lateinit var weights: LiveData<List<BodyWeight>>

    init {
        viewModelScope.launch {
            weights = repository.getWeightDao().getAll()
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun findAndInsert(weight: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val strDate = sdf.format(Date())
            if (repository.getWeightDao().getCurrentDate(strDate) != null) {
                repository.getWeightDao().update(weight, strDate)
            } else {
                repository.getWeightDao().insert(BodyWeight(weight, strDate))
            }
        }
    }


    fun insert(bodyWeight: BodyWeight) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWeightDao().insert(bodyWeight)
        }
    }

    fun update(weight: Double, date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWeightDao().update(weight, date)
        }
    }

    fun delete(bodyWeight: BodyWeight) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWeightDao().delete(bodyWeight)
        }
    }

    fun showAddDialog() {
        val dialog = BodyWeightDialog(getApplication(), false)
        dialog.setDialogListener(object : BodyWeightDialogListener {
            override fun onPositiveClicked(weight: Double) {
                findAndInsert(weight)
            }
        })
        dialog.show()

    }
}