package com.example.ibdtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    public static final String LAST_LOGIN_DATE_KEY = "last_login_date"; //key for the date the app was last used.
    private SharedPreferences sharedPreferences; //the users saved preferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide app name bar
        setContentView(R.layout.activity_main);

        //Get the shared preferences file
        sharedPreferences = getSharedPreferences(SelectorActivity.SHARED_PREF_FILE, MODE_PRIVATE);

        //get the type of ibd stored in sp
        String ibdType = sharedPreferences.getString(SelectorActivity.IBD_TYPE_KEY, "" );

        //get the last login stored in sp
        String lastLoginDate = sharedPreferences.getString(MainActivity.LAST_LOGIN_DATE_KEY, LocalDate.now().toString());

        //update the last login date to todays date
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MainActivity.LAST_LOGIN_DATE_KEY, LocalDate.now().toString());

        //apply the changes
        editor.apply();

        //if the value in sp is crohns or colitis, this is not the fist time the app has been used so open activity
        if(ibdType.equals("Crohns") || ibdType.equals("Colitis")) {
            //open crohns dashboard if crohns
            if(ibdType.equals("Crohns")) {
                //create a handler to add a delay
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
                //create a handler to add a delay
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
            //create a handler to add a delay
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
}