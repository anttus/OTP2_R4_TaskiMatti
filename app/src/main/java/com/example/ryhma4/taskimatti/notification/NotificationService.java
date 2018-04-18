package com.example.ryhma4.taskimatti.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ryhma4.taskimatti.Controller.Database;
import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NotificationService extends Service {

    boolean isRunning;
    private int startId;
    private static final int DAILY_REMINDER_REQUEST_CODE = 1;
    private static final int WEEKLY_REMINDER_REQUEST_CODE = 0;

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
//        Notification notificationPopup = new Notification.Builder(this)
//                .setContentTitle("TaskiMatti is alerting!")
//                .setContentText("Click here!")
//                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
//                .setContentIntent(pendingIntent_main_activity)
//                .setOngoing(false)
////                .addExtras() Additional information
//                .setAutoCancel(true)
//                .build();

//        Log.i("LocalService", "Received start id " + startId + ": " + intent);
//
//        //Fetch the extra string values
//        String state = intent.getExtras().getString("extra");
//
//        //This converts the extra strings from the intent to start id's
//        assert state != null;
//        switch (state) {
//            case "Alarm on": startId = 1;
//                break;
//            case "Alarm off": startId = 0;
//                break;
//            default: startId = 0;
//                break;
//        }
//
//        //If no music playing and user pressed "alarm on, music starts"
//        if (!this.isRunning && startId == 1) {
//
//            // Ringtone
//
//            mediaPlayer = MediaPlayer.create(this, R.raw.pop);
//
//            mediaPlayer.start();
//
//            //Setup the notification call command
//            notificationManager.notify(0, notificationPopup);
//
//            this.isRunning = true;
//            this.startId = 0;
//
//        }
//
//        //If the user presses "alarm off" when no music is playing, do nothing
//        else if (!this.isRunning && startId == 0) {
//            this.isRunning = false;
//            this.startId = 0;
//        }
//        //If user presses "alarm on" when music is playing, do nothing
//        else if (this.isRunning && startId == 1) {
//            this.isRunning = true;
//            this.startId = 0;
//        }
//        else {
//            mediaPlayer.stop();
//            mediaPlayer.reset();
//
//            this.isRunning = false;
//            this.startId = 0;
//        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.isRunning = false;
    }

    public static void showWeekNotification(Context context, Class<?> cls, String title, String content) {
        Database db = Database.getInstance();
        db.resetForgottenTasks();
        db.findTasksToActivate();

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(
                WEEKLY_REMINDER_REQUEST_CODE,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notificationPopup = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setContentIntent(pendingIntent)
                .setOngoing(false)
                .setSound(alarmSound)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(WEEKLY_REMINDER_REQUEST_CODE, notificationPopup);
    }

    public static void showNotification(Context context,Class<?> cls,String title,String content) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(
                DAILY_REMINDER_REQUEST_CODE,PendingIntent.FLAG_UPDATE_CURRENT);

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//        Notification notification = builder.setContentTitle(title)
//                .setContentText(content).setAutoCancel(true)
//                .setSound(alarmSound).setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentIntent(pendingIntent).build();

        Notification notificationPopup = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setContentIntent(pendingIntent)
                .setOngoing(false)
                .setSound(alarmSound)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(DAILY_REMINDER_REQUEST_CODE, notificationPopup);
    }
    public static void setWeeklyReminder(Context context, Class<?> cls, int hour, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        Calendar checkDate = Calendar.getInstance();

        if(checkDate.after(calendar)) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        System.out.println("NOTIFICATION WEEKLY REMINDER: " + sdf.format(calendar.getTime()));
        // cancel already scheduled reminders
        cancelReminder(context,cls);

        // Enable a receiver
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent = new Intent(context, cls);
        intent.putExtra("extra", "Alarm on");
        intent.putExtra("type", "week");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                WEEKLY_REMINDER_REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public static void setReminder(Context context, Class<?> cls, int hour, int min) {
        Calendar calendar = Calendar.getInstance();
        Calendar setCalendar = Calendar.getInstance();
        setCalendar.set(Calendar.HOUR_OF_DAY, hour);
        setCalendar.set(Calendar.MINUTE, min);
        setCalendar.set(Calendar.SECOND, 0);
        // cancel already scheduled reminders
        cancelReminder(context,cls);

        if(setCalendar.before(calendar))
            setCalendar.add(Calendar.DATE,1);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        System.out.println("NOTIFICATION TASK REMINDER: " + sdf.format(setCalendar.getTime()));

        // Enable a receiver
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        intent1.putExtra("extra", "Alarm on");
        intent1.putExtra("type", "task");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                DAILY_REMINDER_REQUEST_CODE, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setCalendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public static void cancelReminder(Context context,Class<?> cls) {
        // Disable a receiver
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }


//    public void setAlarm(Context context, int hour, int minute) {
//        //Initialize alarm manager
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//        calendar.add(Calendar.SECOND, 3);
//
//        //Get the int values of the hour and minute
////        final int hour = timePicker.getHour();
////        final int minute = timePicker.getMinute();
//
//        calendar.set(Calendar.HOUR_OF_DAY, hour);
//        calendar.set(Calendar.MINUTE, minute);
//
//        //Put in extra string into myIntent
//        //Tells the clock that you pressed the "Alarm on" button
//        myIntent.putExtra("extra", "Alarm on");
//
//        //Create a pending intent that delays the intent
//        //until the specified calendar time
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        //Set the alarm manager
//        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//
//
//    }

}
