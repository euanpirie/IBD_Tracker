package com.example.ibdtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AppSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPreferences; //the users saved preferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide app name bar
        setContentView(R.layout.activity_app_settings);

        //Get the shared preferences file
        sharedPreferences = getSharedPreferences(SelectorActivity.SHARED_PREF_FILE, MODE_PRIVATE);

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(this);

        Float ibdType = sharedPreferences.getFloat(MainActivity.TYPICAL_WEIGHT_KEY, 0);
        TextView tv = findViewById(R.id.textView2);
        tv.setText(ibdType.toString());

        //Bottom navigation bar set up
        BottomNavigationView bottomNav = findViewById(R.id.bnvNavigation); //initialising and assigning the bottomNav variable
        bottomNav.setSelectedItemId(R.id.navSettings); //setting the selected item in the nav bar

        //set the click listeners for each of the nav bar items
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //switch for each of the menu options
                switch(item.getItemId()) {
                    //settings page
                    case R.id.navSettings:
                        return true; //do nothing
                    //dashboard page
                    case R.id.navDashboard:
                        //if crohns is being tracked
                        if(ibdType.equals("Crohns")) {
                            startActivity(new Intent(getApplicationContext(), CrohnsDashboardActivity.class)); //start crohns dashboard activity
                            finish(); // finish current activity
                            return true;
                        }
                        //otherwise it is colitis
                        else{
                            startActivity(new Intent(getApplicationContext(), ColitisDashboardActivity.class)); //start crohns dashboard activity
                            finish(); // finish current activity
                            return true;
                        }
                    case R.id.navSurvey:
                        //if crohns is being tracked
                        if(ibdType.equals("Crohns")) {
                            startActivity(new Intent(getApplicationContext(), CrohnsSurveyActivity.class)); //start crohns survey activity
                            finish(); // finish current activity
                            return true;
                        }
                        //otherwise it is colitis
                        else{
                            startActivity(new Intent(getApplicationContext(), ColitisSurveyActivity.class)); //start crohns survey activity
                            finish(); // finish current activity
                            return true;
                        }
                }
                return false; //if not in switch statement something has gone very wrong
            }
        });

    }

    @Override
    public void onClick(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SelectorActivity.IBD_TYPE_KEY, "");
        editor.apply();

        finishAffinity();
    }
}