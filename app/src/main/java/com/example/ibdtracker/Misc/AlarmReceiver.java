package com.example.ibdtracker.Misc;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.ibdtracker.MainActivity;
import com.example.ibdtracker.R;

/**
 * An alarm receiver class to create scheduled tasks
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    /**
     * The method performed when a broadcast is received
     * @param context the context the app is working in
     * @param intent the action being carried out
     */
    public void onReceive(Context context, Intent intent) {

        //get the reminder id
        int id = intent.getIntExtra(ReminderRecyclerViewAdapter.EXTRA_REMINDER_ID, 0);

        //get the reminder text
        String reminderText = intent.getStringExtra(ReminderRecyclerViewAdapter.EXTRA_REMINDER_TEXT);

        //create a notification builder on the notification channel
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.misc_notification_channel_id));
        builder.setContentTitle("IBD Tracker - Reminder"); //set the title of the notification
        builder.setContentText(reminderText); //set the notification cotent
        builder.setSmallIcon(R.drawable.ic_notification_icon); //set the notification icon
        builder.setAutoCancel(true); //make notification clear when pressed

        //create a notification  manager
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        //display notification, assign it an ID
        notificationManager.notify(id, builder.build());

        //Testing purposes
        //Toast.makeText(context, "ALARM IS WORKING", Toast.LENGTH_LONG).show();
    }
}
