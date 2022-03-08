package com.example.ibdtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.content.SharedPreferences;

import com.example.ibdtracker.Colitis.ColitisDashboardActivity;
import com.example.ibdtracker.Crohns.CrohnsDashboardActivity;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    //Shared preferences set up
    public static final String SHARED_PREF_FILE = "com.example.ibdtracker"; //shared pref file name
    public static final String IBD_TYPE_KEY = "ibd_type"; //key for the ibd type stored in SP
    public static final String TYPICAL_WEIGHT_KEY = "typical_weight"; //key for the users typical weight stored in SP
    private SharedPreferences sharedPreferences; //the users saved preferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide app name bar
        setContentView(R.layout.activity_main);

        //create the notification channel
        createNotificationChannel();

        //Get the shared preferences file
        sharedPreferences = getSharedPreferences(SelectorActivity.SHARED_PREF_FILE, MODE_PRIVATE);

        //get the type of ibd stored in sp
        String ibdType = sharedPreferences.getString(SelectorActivity.IBD_TYPE_KEY, "" );

        //if the value in sp is crohns or colitis, this is not the fist time the app has been used so open activity
        if(ibdType.equals("Crohns") || ibdType.equals("Colitis")) {
            //open crohns dashboard if crohns
            if(ibdType.equals("Crohns")) {
                //create a handler to add a delay - testing purposes
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), CrohnsDashboardActivity.class)); //start dashboard activity
                        finish(); //finish current activity - stops user from going back to activity
                    }
                },1000); //1 second delay
            }
            //otherwise must be colitis
            else{
                //create a handler to add a delay - testing purposes
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), ColitisDashboardActivity.class)); //start dashboard activity
                        finish(); //finish current activity - stops user from going back to activity
                    }
                },1000); //1 second delay
            }
        }
        //if the values are not crohns or colitis - the selector activity needs to be opened
        else {
            //create a handler to add a delay - testing purposes
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), SelectorActivity.class)); //start selector activity
                    finish(); //finish current activity - stops user from going back to activity
                }
            },1000); //1 second delay
        }

    }

    /**
     * A method to create a notification channel for the app
     * The code below is taken from the Android Developers Documentation and can be found at:
     * https://developer.android.com/training/notify-user/channels
     */
    private void createNotificationChannel() {
        // if the SDK version is >26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String id = getString(R.string.misc_notification_channel_id);
            CharSequence name = getString(R.string.misc_notification_channel_id); //get the channel id
            String description = getString(R.string.misc_notification_channel_description); //get the channel descritpion
            int importance = NotificationManager.IMPORTANCE_DEFAULT; //set the importance to the default value

            //create a notification channel with the previous info
            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.setDescription(description);

            // register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}