package com.health.myapplication.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.health.myapplication.db.DbHelper_alarm
import com.health.myapplication.R
import com.health.myapplication.alarm.MyAlarmReceiver
import com.health.myapplication.exception.ExceptionHandler
import com.health.myapplication.model.AlarmContract
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var adView: AdView

    //FOR ALARM
    private val REQUEST_CODE = 100
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var dbHelper : DbHelper_alarm
    private lateinit var alarmTime : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this,resources.getString(R.string.admob_app_id))

        adView = findViewById<View>(R.id.adView) as AdView
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        val intent = Intent(this, LoadingActivity::class.java)
        startActivity(intent)

        dbHelper=DbHelper_alarm(this)
        alarmTime=dbHelper.alarm
        if(!alarmTime.equals("0")) {
            var indexOfColon = alarmTime.lastIndexOf(":") + 1;
            minuteEditText.setText(alarmTime.substring(0, indexOfColon-1))
            secondEditText.setText(alarmTime.substring(indexOfColon))
        }

        view1.setOnClickListener {
            val intent = Intent(this@MainActivity, ExercisePartCategoryActivity::class.java)
            startActivity(intent)
        }
        view2.setOnClickListener {
            val intent = Intent(this@MainActivity, CustomProgramActivity::class.java)
            startActivity(intent)
        }
        view3.setOnClickListener {
            val intent = Intent(this@MainActivity, RecordActivity::class.java)
            startActivity(intent)
        }
        view4.setOnClickListener {
            val intent = Intent(this@MainActivity, CalculatorActivity::class.java)
            startActivity(intent)
        }
        view5.setOnClickListener {
            val intent = Intent(this@MainActivity, BodyWeightActivity::class.java)
            startActivity(intent)
        }
        view6.setOnClickListener {
            val intent = Intent(this@MainActivity, ProgramRecommendActivity::class.java)
            startActivity(intent)
        }

        startButton.setOnClickListener {
            // Creating the pending intent to send to the BroadcastReceiver
            try {
                alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this, MyAlarmReceiver::class.java)
                intent.putExtra("code",REQUEST_CODE)
                pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)

                // Setting the specific time for the alarm manager to trigger the intent
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = System.currentTimeMillis()

                //0~60초, 분 이내의 시간 입력
                if (Integer.parseInt(secondEditText.text.toString()) > 60 || Integer.parseInt(secondEditText.text.toString()) < 0 ||
                        Integer.parseInt(minuteEditText.text.toString()) > 60 || Integer.parseInt(minuteEditText.text.toString()) < 0) {
                    Toast.makeText(this@MainActivity, "올바른 시간을 입력하세요.", Toast.LENGTH_LONG).show()
                } else {
                    calendar.add(Calendar.SECOND, Integer.parseInt(secondEditText.text.toString()))
                    calendar.add(Calendar.MINUTE, Integer.parseInt(minuteEditText.text.toString()))

                    dbHelper.updateAlarm(AlarmContract(Integer.parseInt(minuteEditText.text.toString()), Integer.parseInt(secondEditText.text.toString())))

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                    }

                    Toast.makeText(this@MainActivity, minuteEditText.text.toString() + "분 " +
                            secondEditText.text.toString() + "초 뒤에 알람이 울립니다", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "알람 설정 오류", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
