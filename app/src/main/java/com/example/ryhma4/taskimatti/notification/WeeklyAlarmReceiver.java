package com.example.ryhma4.taskimatti.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.ryhma4.taskimatti.Controller.Database;
import com.example.ryhma4.taskimatti.activity.MainActivity;
import com.example.ryhma4.taskimatti.activity.SetTaskActivity;

public class WeeklyAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //Fetch extra string from the intent
        String state = intent.getExtras().getString("weekReminder");

        //Create and intent to the ringtone service
        Intent serviceIntent = new Intent(context, NotificationService.class);

        //Put the extra string from Main Activity to the Ringtone Playing Service
        serviceIntent.putExtra("weekReminder", state);

        //Start the ringtone service
        context.startService(serviceIntent);

        //Trigger the notification
        System.out.println("WEEKLY ALARM IS TRYING TO USE NOTIFICATION SERVICE");
        NotificationService.showWeekNotification(context, SetTaskActivity.class,
                "TaskiMatti weekly reminder!", "Set them tasks to get shit done!");
    }
}