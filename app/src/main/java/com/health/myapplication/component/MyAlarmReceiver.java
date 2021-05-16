package com.health.myapplication.component;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.health.myapplication.dao.AlarmDao;
import com.health.myapplication.db.AlarmDatabase;
import com.health.myapplication.entity.etc.Alarm;

import java.util.Calendar;

public class MyAlarmReceiver extends BroadcastReceiver {
    private static final int REQUEST_CODE = 100;
    private static final int REQUEST_CODE_AT_SERVICE = 101;

    @Override
    public void onReceive(Context context, Intent intent) {
        //Receiver가 알람을 받음
        Log.e("intent", intent.getExtras().getInt("code") + "");
        if (intent.getExtras().getInt("code") == REQUEST_CODE) {
            Intent service_intent = new Intent(context, AlarmService.class);
            AlarmService.enqueueWork(context, service_intent); //service 시작
        } else if (intent.getExtras().getInt("code") == REQUEST_CODE_AT_SERVICE) {
            PendingResult result = goAsync();
            try {
                setAlarm(context);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result.finish();
        }
    }

    private void setAlarm(Context context) throws InterruptedException {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        AlarmRepeater alarmRepeater = new AlarmRepeater(context);
        Thread thread = new Thread(alarmRepeater);
        thread.start();
        thread.join();
        Alarm alarm = alarmRepeater.alarm;

        Intent intent = new Intent(context, MyAlarmReceiver.class);
        intent.putExtra("code", REQUEST_CODE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, alarm.getMinute());
        calendar.add(Calendar.SECOND, alarm.getSecond());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        Toast.makeText(context, alarm.getMinute() + "분 " + alarm.getSecond() + "초 뒤에 알람이 울립니다", Toast.LENGTH_SHORT).show();


    }

    public class AlarmRepeater implements Runnable {
        private volatile Alarm alarm;
        private AlarmDao alarmDao;

        AlarmRepeater(Context context) {
            alarmDao = AlarmDatabase.Companion.getInstance(context).alarmDao();
        }

        @Override
        public void run() {
            alarm = alarmDao.getCurrentAlarm();
        }
    }
}
