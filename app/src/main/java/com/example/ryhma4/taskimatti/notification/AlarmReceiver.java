package com.example.ryhma4.taskimatti.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
        context.startService(serviceIntent);

        //Trigger the notification
        int requestCode = intent.getExtras().getInt("code");
        String title = intent.getExtras().getString("title");
        String content = intent.getExtras().getString("content");

        NotificationService.showNotification(context, MainActivity.class, title, content , requestCode);
//
//        switch(intent.getExtras().getString("type")){
//            case "task":
//                NotificationService.showNotification(context, MainActivity.class,"TaskiMatti is alerting!", "Click here!", requestCode);
//                break;
//            case "week":
//                NotificationService.showNotification(context, SetTaskActivity.class,"TaskiMatti weekly reminder!", "Set them tasks to get shit done!", requestCode);
//                break;
//            default:
//                NotificationService.showNotification(context, MainActivity.class,"TaskiMatti default alert", "Something went wrong bleep bloop.", requestCode);
//                break;
//        }
    }
}
