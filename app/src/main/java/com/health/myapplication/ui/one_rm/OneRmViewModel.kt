package com.health.myapplication.ui.one_rm

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.*
import com.health.myapplication.application.BaseApplication
import com.health.myapplication.entity.etc.DialogType
import com.health.myapplication.ui.one_rm.dialog.OneRmDialog
import com.health.myapplication.listener.OneRmDialogListener
import com.health.myapplication.entity.one_rm.OneRmDate
import com.health.myapplication.entity.one_rm.OneRmRecord
import com.health.myapplication.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class OneRmViewModel(
        application: Application
) : AndroidViewModel(application) {
    private val repository = Repository(application)
    private val oneRMDateDao = repository.getOneRMDateDao()
    private val oneRMRecordDao = repository.getOneRMRecordDao()
    lateinit var oneRmDateList: LiveData<List<OneRmDate>>
    lateinit var oneRmRecordList: LiveData<List<OneRmRecord>>
    val crudAlert = MutableLiveData<CRUD_ALERT>()
    private val colorMap = getApplication<BaseApplication>().getColorMap()

    init {
        viewModelScope.launch {
            oneRmDateList = oneRMDateDao.getAllLiveDate()
            oneRmRecordList = oneRMRecordDao.getRecords()
        }
    }


    fun insert(date: String, exercise: String, weight: Double) {
        val oneRMDate = oneRMDateDao.getDate(date)

        if (oneRMDate == null) {
            val dateId = oneRMDateDao.insert(OneRmDate(date)).toInt()
            oneRMRecordDao.insert(OneRmRecord(exercise, weight, dateId))
        } else {
            val item = oneRMRecordDao.find(exercise, oneRMDate!!.id!!)
            if (item != null) {
                crudAlert.postValue(CRUD_ALERT.EXIST)
                return
            }
            oneRMRecordDao.insert(OneRmRecord(exercise, weight, oneRMDate!!.id!!))

        }
    }

    fun updateOneRMRecord(id: Int, weight: Double){
        viewModelScope.launch(Dispatchers.IO) {
            oneRMRecordDao.update(id,weight)
            crudAlert.postValue(CRUD_ALERT.UPDATE)
        }
    }

    fun deleteOneRMRecord(oneRMRecord: OneRmRecord){
        viewModelScope.launch(Dispatchers.IO) {
            oneRMRecordDao.deleteById(oneRMRecord.id!!)
            if(oneRMRecordDao.getRecordsByDateId(oneRMRecord.dateId!!).isEmpty())
                oneRMDateDao.deleteById(oneRMRecord.dateId!!)
            crudAlert.postValue(CRUD_ALERT.DELETE)
        }
    }

    suspend fun getAllDate(): List<OneRmDate>{
        var dateList : List<OneRmDate> = listOf()
        val job = viewModelScope.launch(Dispatchers.IO) {
            dateList = oneRMDateDao.getAllDate()
        }
        job.join()
        return dateList
    }

    suspend fun getChartLineData(records : List<OneRmRecord>)  : LineData{
        val benchList = ArrayList<Entry>()
        val squatList = ArrayList<Entry>()
        val deadList = ArrayList<Entry>()
        var dateList : List<OneRmDate> = listOf()
        val job = viewModelScope.launch(Dispatchers.IO) {
            dateList = oneRMDateDao.getAllDate()
        }
        job.join()
        for(i in dateList.indices){
            val dateId = dateList[i].id!!
            val squat = records.find {
                it.dateId == dateId && it.exercise =="스쿼트"
            }?: OneRmRecord.NULL
            val bench = records.find {
                it.dateId == dateId && it.exercise =="벤치프레스"
            }?: OneRmRecord.NULL
            val dead = records.find {
                it.dateId == dateId && it.exercise =="데드리프트"
            }?: OneRmRecord.NULL

            squatList.add(Entry(i.toFloat(),squat.weight.toFloat()))
            benchList.add(Entry(i.toFloat(),bench.weight.toFloat()))
            deadList.add(Entry(i.toFloat(),dead.weight.toFloat()))
        }
        val squatSet = setLineDataAttributes(LineDataSet(squatList, "스쿼트"), colorMap["blue"]!!)
        val benchSet = setLineDataAttributes(LineDataSet(benchList, "벤치프레스"), colorMap["red"]!!)
        val deadSet = setLineDataAttributes(LineDataSet(deadList, "데드리프트"), colorMap["black"]!!)
        return LineData(listOf(
                squatSet,
                benchSet,
                deadSet
        ))
    }

    fun getTotalOneRm(records : List<OneRmRecord>): Double{
        val squatMax =  records.filter{it.exercise=="스쿼트"}.map { it.weight }.max()?:0.0
        val benchMax =  records.filter{it.exercise=="벤치프레스"}.map { it.weight }.max()?:0.0
        val deadMax =  records.filter{it.exercise=="데드리프트"}.map { it.weight }.max()?:0.0
        return squatMax+benchMax+deadMax
    }

    fun getBarData(records:List<OneRmRecord>) : BarData{
        val squatMaxRecord = records.filter {
            it.exercise == "스쿼트"
        }.maxBy {
            it.weight
        } ?: OneRmRecord.NULL
        val benchMaxRecord = records.filter {
            it.exercise == "벤치프레스"
        }.maxBy {
            it.weight
        } ?: OneRmRecord.NULL
        val deadMaxRecord = records.filter {
            it.exercise == "데드리프트"
        }.maxBy {
            it.weight
        } ?: OneRmRecord.NULL

        val entries = listOf(
                BarEntry(0f, squatMaxRecord.weight.toFloat()),
                BarEntry(1f, benchMaxRecord.weight.toFloat()),
                BarEntry(2f, deadMaxRecord.weight.toFloat())
        )
        val barDataSet = BarDataSet(entries, "3대운동")
        barDataSet.setColors(*intArrayOf(colorMap["blue"]!!, colorMap["red"]!!, colorMap["black"]!!))
        barDataSet.setDrawValues(true)

        val barData = BarData(barDataSet)
        barData.setDrawValues(true)
        return barData
    }

    fun showAddDialog(activity: FragmentActivity){
        val dialog = OneRmDialog(activity, DialogType.INSERT)
        dialog.setDialogListener(OneRmDialogListener { date, exercise, one_rm ->
            GlobalScope.launch(Dispatchers.IO) {
                insert(date, exercise, one_rm)
            }
        })
        dialog.show()
    }

    private fun setLineDataAttributes(lineDataSet: LineDataSet, color : Int): LineDataSet {
        lineDataSet.apply {
            setCircleColor(color)
            setColor(color)
            circleHoleColor = color
            circleRadius = 3f
            valueTextSize = 10f
            lineWidth = 1.5f
        }
        return lineDataSet
    }

    enum class CRUD_ALERT{
        INSERT, DELETE, UPDATE, EXIST
    }
}