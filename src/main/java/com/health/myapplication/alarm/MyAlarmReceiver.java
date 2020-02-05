package com.health.myapplication.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyAlarmReceiver extends BroadcastReceiver {
    private static Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        //Receiver가 알람을 받음
        this.context = context;
        Intent service_intent = new Intent(context, AlarmService.class);
        AlarmService.enqueueWork(context,service_intent); //service 시작

    }
}
