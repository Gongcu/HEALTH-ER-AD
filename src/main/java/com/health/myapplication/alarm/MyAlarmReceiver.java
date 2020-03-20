package com.health.myapplication.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.health.myapplication.DbHelper.DbHelper_alarm;

import java.util.Calendar;

public class MyAlarmReceiver extends BroadcastReceiver {
    private static Context context;
    private static final int REQUEST_CODE=100;
    private static final int REQUEST_CODE_AT_SERVICE=101;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        //Receiver가 알람을 받음
        Log.e("intent",intent.getExtras().getInt("code")+"");
        if(intent.getExtras().getInt("code")==REQUEST_CODE) {
            Intent service_intent = new Intent(context, AlarmService.class);
            AlarmService.enqueueWork(context, service_intent); //service 시작
        }else if(intent.getExtras().getInt("code")==REQUEST_CODE_AT_SERVICE){
            setAlarm();
        }

    }

    private void setAlarm(){
        Intent intent = new Intent(context, MyAlarmReceiver.class);
        intent.putExtra("code",REQUEST_CODE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        DbHelper_alarm dbHelper_alarm = new DbHelper_alarm(context);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());


        String alarmTime = dbHelper_alarm.getAlarm();
        int indexOfColon = alarmTime.indexOf(":");
        int minute = Integer.parseInt(alarmTime.substring(0,indexOfColon));
        int second = Integer.parseInt(alarmTime.substring(indexOfColon+1));


        calendar.add(Calendar.MINUTE, minute);
        calendar.add(Calendar.SECOND, second);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        Toast.makeText(context, minute + "분 " + second + "초 뒤에 알람이 울립니다", Toast.LENGTH_SHORT).show();
    }
}
