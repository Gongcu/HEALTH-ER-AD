package com.health.myapplication.ui

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.health.myapplication.component.MyAlarmReceiver
import com.health.myapplication.application.BaseApplication
import com.health.myapplication.entity.etc.Alarm
import com.health.myapplication.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val context: Context = getApplication<BaseApplication>().applicationContext
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val repository = Repository(getApplication())
    private val alarmDao = repository.getAlarmDao()
    val minutesStr = MutableLiveData<String>()
    val secondsStr = MutableLiveData<String>()

    init {
        //alarmDbHelper = AlarmDbHelper(context)
        viewModelScope.launch(Dispatchers.IO) {
            val alarm: Alarm? = alarmDao.getCurrentAlarm()

            if (alarm != null) {
                minutesStr.postValue(alarm.minute.toString())
                secondsStr.postValue(alarm.second.toString())
            }
        }
    }

    fun setAlarm(minutesText: String, secondsText: String) {
        try {
            val minutes = Integer.parseInt(minutesText)
            val seconds = Integer.parseInt(secondsText)

            if (!correctTimeCheck(minutes, seconds))
                return


            val intent = Intent(context, MyAlarmReceiver::class.java)
            intent.putExtra("code", REQUEST_CODE)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val calendar = setCalendar(minutes, seconds)

            viewModelScope.launch(Dispatchers.IO) {
                alarmDao.upsert(minutes,seconds)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            else
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

            val str = "${minutesText}분 ${secondsText}초 후에 알림이 울립니다."
            getApplication<BaseApplication>().makeToast(str)

        } catch (e: Exception) {
            getApplication<BaseApplication>().makeToast("알람 설정 오류")
        }
    }

    private fun correctTimeCheck(minutes: Int, seconds: Int): Boolean {
        return if (minutes > 60 || minutes < 0 ||
            seconds > 60 || seconds < 0
        ) {
            getApplication<BaseApplication>().makeToast("올바른 시간을 입력하세요.")
            false
        } else
            true
    }

    private fun setCalendar(minutes: Int, seconds: Int): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.SECOND, seconds)
        calendar.add(Calendar.MINUTE, minutes)
        return calendar
    }


    companion object {
        const val REQUEST_CODE = 100
    }
}