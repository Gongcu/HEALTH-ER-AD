package com.health.myapplication.alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.health.myapplication.db.DbHelper_alarm;
import com.health.myapplication.R;
import com.health.myapplication.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmService extends JobIntentService {
    private final int REQUEST_CODE_AT_SERVICE = 101;
    static final int JOB_ID = 1001;
    private Calendar calendar;
    private static Context mContext;
    private DbHelper_alarm dbHelper_alarm;

    //receiver에서 실행하는 함수
    static void enqueueWork(Context context, Intent work) {
        mContext = context;
        enqueueWork(context, AlarmService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        calendar=Calendar.getInstance();
        notification("HEALTH-ER", AlarmService.this); //notification 함수
        stopSelf();
    }



    public void notification(String title, Context context) {
        String text = "더 쉬면 근손실.";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = createID();
        String channelId = "channel-id";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(text)
                .setVibrate(new long[]{100, 250})
                .setLights(Color.YELLOW, 500, 5000)
                .addAction(R.drawable.alarm_restart_btn,"알람재시작",getPendingIntent())
                .setAutoCancel(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setSmallIcon(R.drawable.noti);
            mBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            mBuilder.setSmallIcon(R.drawable.noti);
        }
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(new Intent(context, MainActivity.class));
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }

    public int createID() {
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.KOREA).format(now));
        return id;
    }

    private PendingIntent getPendingIntent(){
        Intent intent = new Intent(this, MyAlarmReceiver.class);
        intent.putExtra("code",REQUEST_CODE_AT_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE_AT_SERVICE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}