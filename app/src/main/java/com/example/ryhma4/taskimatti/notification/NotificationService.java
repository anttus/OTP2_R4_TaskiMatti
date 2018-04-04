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
    Context context;
    private AlarmManager alarmManager;
    private AlarmReceiver alarmReceiver;
    final Calendar calendar = Calendar.getInstance();
    final Intent myIntent = new Intent(this.context, AlarmReceiver.class);
    private PendingIntent pendingIntent;

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
                .setContentTitle("Wake up!")
                .setContentText("Click here!")
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setContentIntent(pendingIntent_main_activity)
                .setAutoCancel(true)
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

//THIS IS WHERE THE RINGTONE STARTS PLAYING

            //Instance of media player

//Replace this with the method that plays the notes
//            mediaPlayer = MediaPlayer.create(this, R.raw.jazzy);

            mediaPlayer.start();

//            randAlarm();

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

    public void setAlarm(Context context, int hour, int minute) {
        //Initialize alarm manager
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        calendar.add(Calendar.SECOND, 3);

        //Get the int values of the hour and minute
//        final int hour = timePicker.getHour();
//        final int minute = timePicker.getMinute();

//        setAlarmText("You clicked " + hour + " and " + minute);

//        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
//        calendar.set(Calendar.MINUTE, timePicker.getMinute());

        //Put in extra string into myIntent
        //Tells the clock that you pressed the "Alarm on" button
        myIntent.putExtra("extra", "Alarm on");

        //Create a pending intent that delays the intent
        //until the specified calendar time
        pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Set the alarm manager
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        //Convert the int values to string
//        String hour_string = String.valueOf(hour);
//        String minute_string = String.valueOf(minute);
//        if (minute < 10) {
//            minute_string = "0" + String.valueOf(minute);
//        }

        //Method to change updateText
//        setAlarmText("Alarm set to " + hour_string + ":" + minute_string);
    }

}
