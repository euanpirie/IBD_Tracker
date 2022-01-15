package com.example.ibdtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ColitisDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide app name bar
        setContentView(R.layout.activity_colitis_dashboard);

        //Bottom navigation bar set up
        BottomNavigationView bottomNav = findViewById(R.id.bnvNavigation); //initialising and assigning the bottomNav variable
        bottomNav.setSelectedItemId(R.id.navDashboard); //setting the selected item in the nav bar

        //set the click listeners for each of the nav bar items
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //switch for each of the menu options
                switch(item.getItemId()) {
                    //settings page
                    case R.id.navSettings:
                        startActivity(new Intent(getApplicationContext(), AppSettingsActivity.class)); //start settings activity
                        finish(); //end current activity
                        return true;
                    //dashboard page
                    case R.id.navDashboard:
                        return true; //do nothing
                    //survey page
                    case R.id.navSurvey:
                        startActivity(new Intent(getApplicationContext(), ColitisSurveyActivity.class)); //start survey activity
                        finish(); //end current activity
                        return true;
                }
                return false; //if not in switch statement something has gone very wrong
            }
        });
    }
}