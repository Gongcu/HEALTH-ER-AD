package com.health.myapplication.ui.one_rm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.health.myapplication.model.calculator.OneRMDate
import com.health.myapplication.model.calculator.OneRMRecord
import com.health.myapplication.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

private const val TAG = "OneRMViewModel"

class OneRMViewModel(
        application: Application
) : AndroidViewModel(application) {
    private val repository = Repository(application)
    val dateList = MutableLiveData<List<String>>()
    val oneRMDateList = MutableLiveData<List<OneRMDate>>(listOf())
    val formattedDateList = MutableLiveData<List<String>>(listOf())
    val barEntries = MutableLiveData<List<BarEntry>>()
    val totalOneRM = MutableLiveData<Double>()

    val entries = MutableLiveData<Map<String,List<Entry>>>()

    init {
        viewModelScope.launch() {
            getDate()
            initChartRecords()
            initBarRecords()
        }
    }
    fun insert(date: String, exercise: String, weight: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val oneRMDate = repository.getOneRMDateDao().getDate(date)

            if (oneRMDate == null) {
                val dateId = repository.getOneRMDateDao().insert(OneRMDate(date)).toInt()
                repository.getOneRMRecordDao().insert(OneRMRecord(exercise, weight, dateId))
            } else {
                val item = repository.getOneRMRecordDao().find(exercise, oneRMDate!!.id!!)
                if (item != null)
                    return@launch
                repository.getOneRMRecordDao().insert(OneRMRecord(exercise, weight, oneRMDate!!.id!!))

            }
        }
    }

    private fun getDate() {
        viewModelScope.launch() {
            val list = repository.getOneRMDateDao().getAllDate().value ?: listOf()
            oneRMDateList.postValue(list)
            val prevSdf = SimpleDateFormat("yyyy-mm-dd")
            val newSdf = SimpleDateFormat("mm/dd")
            dateList.postValue(list!!.map {
                it.date!!
            })
            formattedDateList.postValue(list!!.map {
                val date = prevSdf.parse(it.date)
                newSdf.format(date)
            })
        }
    }

    private fun initChartRecords() {
        val benchList = ArrayList<Entry>()
        val squatList = ArrayList<Entry>()
        val deadList = ArrayList<Entry>()
        val records = repository.getOneRMRecordDao().getRecords().value ?: listOf()
        Log.d(TAG, "initChartRecords: ${records.toString()}")
        for(i in oneRMDateList.value!!.indices){
            val dateId = oneRMDateList.value!![i].id!!
            val squat = records.find {
                it.dateId == dateId && it.exercise =="스쿼트"
            }?: OneRMRecord.NULL
            val bench = records.find {
                it.dateId == dateId && it.exercise =="벤치프레스"
            }?: OneRMRecord.NULL
            val dead = records.find {
                it.dateId == dateId && it.exercise =="데드리프트"
            }?: OneRMRecord.NULL

            squatList.add(Entry(i.toFloat(),squat.weight.toFloat()))
            benchList.add(Entry(i.toFloat(),bench.weight.toFloat()))
            deadList.add(Entry(i.toFloat(),dead.weight.toFloat()))
        }
        val map = mapOf(
                "squat" to squatList,
                "benchPress" to benchList,
                "deadLift" to deadList
        )
        entries.postValue(map)
    }


    private fun initBarRecords() {
        val list = repository.getOneRMRecordDao().getRecords().value ?: listOf()

        val squatMaxRecord = list.filter {
            it.exercise == "스쿼트"
        }.maxBy {
            it.weight
        } ?: OneRMRecord.NULL
        val benchMaxRecord = list.filter {
            it.exercise == "벤치프레스"
        }.maxBy {
            it.weight
        } ?: OneRMRecord.NULL
        val deadMaxRecord = list.filter {
            it.exercise == "데드리프트"
        }.maxBy {
            it.weight
        } ?: OneRMRecord.NULL

        totalOneRM.postValue(squatMaxRecord.weight + benchMaxRecord.weight + deadMaxRecord.weight)

        val entries = listOf(
                BarEntry(0f, squatMaxRecord.weight.toFloat()),
                BarEntry(1f, benchMaxRecord.weight.toFloat()),
                BarEntry(2f, deadMaxRecord.weight.toFloat())
        )
        barEntries.postValue(entries)
    }


}