package com.example.ryhma4.taskimatti.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.ryhma4.taskimatti.Controller.Database;
import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.activity.MainActivity;
import com.example.ryhma4.taskimatti.model.Reminder;

import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

public class NotificationService extends Service {

    boolean isRunning;
    private static final int WEEKLY_REMINDER_REQUEST_CODE = 0;
    private static final int OREO_WEEKLY_REMINDER_REQUEST_CODE = 234;

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.isRunning = false;
    }

    /**
     * Method to show the received notification
     *
     * @param context     Context of the activity
     * @param cls         Alarm's receiver class
     * @param title       Title of the notification
     * @param content     Content of the notification
     * @param requestCode Unique request code to separate the notifications
     */
    public static void showNotification(Context context, Class<?> cls, String title, String content, int requestCode) {
        if (requestCode == WEEKLY_REMINDER_REQUEST_CODE) {
            Database db = Database.getInstance();
            db.resetForgottenTasks();
            db.findTasksToActivate();
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String ringtone = sp.getString("notifications_new_message_ringtone", "DEFAULT_SOUND");
//        RingtoneManager.setActualDefaultRingtoneUri(context,RingtoneManager.TYPE_NOTIFICATION, Uri.parse(ringtone));
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri alarmSound = Uri.parse(ringtone);
        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(
                requestCode, PendingIntent.FLAG_UPDATE_CURRENT);

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

        // For Android Oreo (v8)
        NotificationManager notificationManager2 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager2.createNotificationChannel(mChannel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setContentIntent(pendingIntent)
                    .setOngoing(false)
                    .setSound(alarmSound)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setAutoCancel(true);
            Intent resultIntent = new Intent(context, cls);
            TaskStackBuilder stackBuilder2 = TaskStackBuilder.create(context);
            stackBuilder2.addParentStack(MainActivity.class);
            stackBuilder2.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder2.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);
            notificationManager2.notify(OREO_WEEKLY_REMINDER_REQUEST_CODE, builder.build());
        } else {
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(requestCode, notificationPopup);
        }
    }

    /**
     * Sets the weekly reminder for scheduling tasks
     *
     * @param context  Context of the activity
     * @param cls      Alarm's receiver class
     * @param reminder Reminder object which holds the reminders information.
     */
    public static void setWeeklyReminder(Context context, Class<?> cls, Reminder reminder) {
        Calendar calendar = Calendar.getInstance();
        Calendar date = reminder.getDate();
        calendar.set(Calendar.DAY_OF_WEEK, date.get(Calendar.DAY_OF_WEEK));
        calendar.set(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, date.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, 0);
        Calendar checkDate = Calendar.getInstance();

        //Testing... Remove once localization strings done.
        reminder.setTitle(MainActivity.globalRes.getString(R.string.prompt_weekly_reminder_title));
        reminder.setContent(MainActivity.globalRes.getString(R.string.prompt_weekly_reminder_content));
        reminder.setType("week");

        if (checkDate.after(calendar)) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        // cancel already scheduled reminders
        cancelReminder(context, cls, WEEKLY_REMINDER_REQUEST_CODE);

        // Enable a receiver
        enableReceiver(context, cls);

        PendingIntent pendingIntent = getPendingIntent(context, cls, reminder, WEEKLY_REMINDER_REQUEST_CODE);

        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);

        System.out.println("CALENDAR: " + calendar.getTime());
    }

    /**
     * Sets the pending notification to the background
     *
     * @param context  Context of the activity
     * @param cls      Alarm's receiver class
     * @param reminder Reminder object which holds the reminders information
     */
    public static void setReminder(Context context, Class<?> cls, Reminder reminder) {
        int requestCode = ThreadLocalRandom.current().nextInt(1, 999999999);
        Calendar setCalendar = reminder.getDate();
        Calendar calendar = Calendar.getInstance();
        reminder.setType("task");

        // cancel already scheduled reminders
        cancelReminder(context, cls, requestCode);

        if (setCalendar.before(calendar))
            setCalendar.add(Calendar.DATE, 1);

        // Enable a receiver
        enableReceiver(context, cls);

        PendingIntent pendingIntent = getPendingIntent(context, cls, reminder, requestCode);

        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setExact(AlarmManager.RTC_WAKEUP, setCalendar.getTimeInMillis(), pendingIntent);
    }

    /**
     * Enables the receiver for reminders
     *
     * @param context Context of the activity
     * @param cls     Alarms receiver class
     */
    public static void enableReceiver(Context context, Class<?> cls) {
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
     * Returns the PendingIntent for the notification.
     *
     * @param context     Context for the activity
     * @param cls         Alarms receiver class
     * @param reminder    Reminder object containing reminder information
     * @param requestCode Unique request code to separate the notifications
     * @return the PendingIntent for the notification
     */
    public static PendingIntent getPendingIntent(Context context, Class<?> cls, Reminder reminder, int requestCode) {
        Intent intent1 = new Intent(context, cls);
        intent1.putExtra("extra", "Alarm on");
        intent1.putExtra("title", reminder.getTitle());
        intent1.putExtra("content", reminder.getContent());
        intent1.putExtra("type", reminder.getType());
        intent1.putExtra("code", requestCode);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                requestCode, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    /**
     * Method to cancel the reminder. Called in setReminder and setWeeklyReminder.
     *
     * @param context     Context of the activity
     * @param cls         Alarm's receiver class
     * @param requestCode Unique request code to separate the notifications
     */
    public static void cancelReminder(Context context, Class<?> cls, int requestCode) {
        // Disable a receiver
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                requestCode, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }

}
