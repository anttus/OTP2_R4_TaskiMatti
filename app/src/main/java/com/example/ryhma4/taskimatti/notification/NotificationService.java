package com.example.ryhma4.taskimatti.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.activity.MainActivity;

import java.util.Calendar;

public class NotificationService extends Service {

    MediaPlayer mediaPlayer;
    boolean isRunning;
    private int startId;

    @Nullable
    public IBinder onBind(Intent intent) {
        Log.e("MyActivity", "In the service");
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        //Set up the notification service
        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //Setup the intent that goes to the Main Activity
        Intent intent_main_activity = new Intent(this, MainActivity.class);
        //Set up a pending intent
        PendingIntent pendingIntent_main_activity = PendingIntent.getActivity(this, 0, intent_main_activity, 0);

        //Make the notification parameters
        Notification notificationPopup = new Notification.Builder(this)
                .setContentTitle("TaskiMatti is alerting!")
                .setContentText("Click here!")
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setContentIntent(pendingIntent_main_activity)
                .setAutoCancel(true)
//                .addAction(R.drawable.ic_clear_black_24dp, "Dismiss", )
                .build();

        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        //Fetch the extra string values
        String state = intent.getExtras().getString("extra");

        //This converts the extra strings from the intent to start id's
        assert state != null;
        switch (state) {
            case "Alarm on": startId = 1;
                break;
            case "Alarm off": startId = 0;
                break;
            default: startId = 0;
                break;
        }

        //If no music playing and user pressed "alarm on, music starts"
        if (!this.isRunning && startId == 1) {

            // Ringtone

            mediaPlayer = MediaPlayer.create(this, R.raw.sotna);

            mediaPlayer.start();

            //Setup the notification call command
            notificationManager.notify(0, notificationPopup);

            this.isRunning = true;
            this.startId = 0;

        }

        //If the user presses "alarm off" when no music is playing, do nothing
        else if (!this.isRunning && startId == 0) {
            this.isRunning = false;
            this.startId = 0;
        }
        //If user presses "alarm on" when music is playing, do nothing
        else if (this.isRunning && startId == 1) {
            this.isRunning = true;
            this.startId = 0;
        }
        else {
            mediaPlayer.stop();
            mediaPlayer.reset();

            this.isRunning = false;
            this.startId = 0;
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.isRunning = false;
    }



}
