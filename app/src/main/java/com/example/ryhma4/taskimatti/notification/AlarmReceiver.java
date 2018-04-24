package com.example.ryhma4.taskimatti.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.ryhma4.taskimatti.R;
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
        switch(intent.getExtras().getString("type")){
            case "task":
                NotificationService.showNotification(context, MainActivity.class,"TaskiMatti is alerting!", "Click here!", requestCode);
                break;
            case "week":
                NotificationService.showNotification(context, SetTaskActivity.class,MainActivity.globalRes.getString(R.string.prompt_weekly_reminder_title), MainActivity.globalRes.getString(R.string.prompt_weekly_reminder_content), requestCode);
                break;
            default:
                NotificationService.showNotification(context, MainActivity.class,"TaskiMatti default alert", "Something went wrong bleep bloop.", requestCode);
                break;
        }
    }
}
