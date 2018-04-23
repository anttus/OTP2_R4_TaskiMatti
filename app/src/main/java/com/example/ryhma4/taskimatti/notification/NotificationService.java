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
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.ryhma4.taskimatti.Controller.Database;
import com.example.ryhma4.taskimatti.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

public class NotificationService extends Service {

    boolean isRunning;
    private static final int WEEKLY_REMINDER_REQUEST_CODE = 0;

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
     * @param context Context of the activity
     * @param cls Alarm's receiver class
     * @param title Title of the notification
     * @param content Content of the notification
     * @param requestCode Unique request code to separate the notifications
     */
    public static void showNotification(Context context,Class<?> cls,String title,String content, int requestCode) {
        if(requestCode == WEEKLY_REMINDER_REQUEST_CODE) {
            Database db = Database.getInstance();
            db.resetForgottenTasks();
            db.findTasksToActivate();
        }

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(
                requestCode,PendingIntent.FLAG_UPDATE_CURRENT);

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
        notificationManager.notify(requestCode, notificationPopup);

        System.out.println("ALARM IS ON!");
    }

    /**
     * Sets the weekly reminder for scheduling tasks
     * @param context Context of the activity
     * @param cls Alarm's receiver class
     * @param date Date and time when the reminder occurs
     */
    public static void setWeeklyReminder(Context context, Class<?> cls, Calendar date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, date.get(Calendar.DAY_OF_WEEK));
        calendar.set(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, date.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, 0);
        Calendar checkDate = Calendar.getInstance();

        if(checkDate.after(calendar)) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        // cancel already scheduled reminders
        cancelReminder(context,cls, WEEKLY_REMINDER_REQUEST_CODE);

        // Enable a receiver
        enableReceiver(context, cls);

        PendingIntent pendingIntent = getPendingIntent(context,cls,"week", WEEKLY_REMINDER_REQUEST_CODE);

        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent);

        System.out.println("CALENDAR: " + calendar.getTime());
    }

    /**
     * Sets the pending notification to the background
     * @param context Context of the activity
     * @param cls Alarm's receiver class
     * @param setCalendar Calendar object which holds the date and time data
     */
    public static void setReminder(Context context, Class<?> cls, Calendar setCalendar) {
        int requestCode = ThreadLocalRandom.current().nextInt(1,999999999);
        Calendar calendar = Calendar.getInstance();

        // cancel already scheduled reminders
        cancelReminder(context,cls,requestCode);

        if(setCalendar.before(calendar))
            setCalendar.add(Calendar.DATE,1);

        // Enable a receiver
        enableReceiver(context,cls);

        PendingIntent pendingIntent = getPendingIntent(context, cls,"task", requestCode);

        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setExact(AlarmManager.RTC_WAKEUP, setCalendar.getTimeInMillis(), pendingIntent);
    }

    /**
     * Enables the receiver for reminders
     * @param context Context of the activity
     * @param cls Alarms receiver class
     */
    public static void enableReceiver(Context context, Class<?> cls){
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
     * Returns the PendingIntent for the notification.
     * @param context Context for the activity
     * @param cls Alarms receiver class
     * @param type  String of the notifications type
     * @param requestCode Unique request code to separate the notifications
     * @return the PendingIntent for the notification
     */
    public static PendingIntent getPendingIntent(Context context, Class<?> cls, String type, int requestCode) {
        Intent intent1 = new Intent(context, cls);
        intent1.putExtra("extra", "Alarm on");
        intent1.putExtra("type", type);
        intent1.putExtra("code", requestCode);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                requestCode, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    /**
     * Method to cancel the reminder. Called in setReminder and setWeeklyReminder.
     * @param context Context of the activity
     * @param cls Alarm's receiver class
     * @param requestCode Unique request code to separate the notifications
     */
    public static void cancelReminder(Context context,Class<?> cls, int requestCode) {
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
