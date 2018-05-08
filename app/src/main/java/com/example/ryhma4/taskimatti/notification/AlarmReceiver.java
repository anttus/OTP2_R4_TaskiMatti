package com.example.ryhma4.taskimatti.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.ryhma4.taskimatti.activity.MainActivity;
import com.example.ryhma4.taskimatti.activity.SetTaskActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //Fetch extra string from the intent
        String state = intent.getExtras().getString("extra");

        //Create and intent to the ringtone service
        Intent serviceIntent = new Intent(context, NotificationService.class);

        //Put the extra string from Main Activity to the Ringtone Playing Service
        serviceIntent.putExtra("extra", state);

        //Start the ringtone service
//        context.startService(serviceIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, NotificationService.class));
        } else {
            context.startService(new Intent(context, NotificationService.class));
        }


        //Trigger the notification
        int requestCode = intent.getExtras().getInt("code");
        String title = intent.getExtras().getString("title");
        String content = intent.getExtras().getString("content");

        if (intent.getAction() != null) {
            Class cls;
            switch (intent.getAction()) {
                case "task":
                    cls = MainActivity.class;
                    break;
                case "week":
                    cls = SetTaskActivity.class;
                    break;
                default:
                    cls = MainActivity.class;
                    break;
            }

            NotificationService.showNotification(context, cls, title, content, requestCode);
        }

    }
}
