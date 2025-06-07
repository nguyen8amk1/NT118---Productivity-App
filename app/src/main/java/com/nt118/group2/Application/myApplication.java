package com.nt118.group2.Application;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.nt118.group2.Database.DataLocal.DataLocalManager;
import com.nt118.group2.Library.Key;
import com.nt118.group2.R;

public class myApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataLocalManager.init(getApplicationContext());
        createChanelNotification();
    }

    private void createChanelNotification() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    Key.CHANNEL_NOTIFICATION,
                    this.getString(R.string.notification_job),
                    NotificationManager.IMPORTANCE_NONE);
            NotificationChannel channelCountUp = new NotificationChannel(
                    Key.CHANNEL_COUNT_UP,
                    this.getString(R.string.notification_count_up),
                    NotificationManager.IMPORTANCE_NONE);
            NotificationChannel notificationJobChannel = new NotificationChannel(
                    Key.CHANNEL_NOTIFICATION_JOB,
                    this.getString(R.string.notification_job),
                    NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if(manager !=null){
                manager.createNotificationChannel(channelCountUp);
                manager.createNotificationChannel(notificationChannel);
                manager.createNotificationChannel(notificationJobChannel);
            }

        }
    }


}
