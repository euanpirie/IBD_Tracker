package com.example.ibdtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

public class SelectorActivity extends AppCompatActivity implements View.OnClickListener {

    //Shared preferences set up
    public static final String SHARED_PREF_FILE = "com.example.ibdtracker"; //shared pref file name
    public static final String IBD_TYPE_KEY = "ibd_type"; //key for the ibd type stored in SP
    private SharedPreferences sharedPreferences; //the users saved preferences


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide app name bar
        setContentView(R.layout.activity_selector);

        sharedPreferences = getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE); //initialise the shared preferences reference

        //add on click listeners to the selector buttons
        Button btnCrohnsSelector = findViewById(R.id.btnCrohnsSelector);
        btnCrohnsSelector.setOnClickListener(this);

        Button btnColitisSelector = findViewById(R.id.btnColitisSelector);
        btnColitisSelector.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        //if the button id is the btnCrohnsSelector
        if(view.getId() == R.id.btnCrohnsSelector) {
            //Button test
            //Toast toast = Toast.makeText(this.getApplicationContext(), "CROHNS BUTTON", Toast.LENGTH_SHORT);
            //toast.show();

            //get the SP editor and add a value to SP
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(IBD_TYPE_KEY, "Crohns");

            //apply changes
            editor.apply();

            //OPEN THE NEW ACTIVITY
        }
        //if the button id is btnColitisSelector
        else if(view.getId() == R.id.btnColitisSelector) {
            //Button test
            Toast toast = Toast.makeText(this.getApplicationContext(), "COLITIS BUTTON", Toast.LENGTH_SHORT);
            toast.show();

            //get the SP editor and add a value to SP
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(IBD_TYPE_KEY, "Colitis");

            //apply changes
            editor.apply();

            //open the new activity
        }
        //otherwise it is an error, notify user
        else {
            Toast toast = Toast.makeText(this.getApplicationContext(), "ERROR: Please notify e.pirie5@rgu.ac.uk", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}