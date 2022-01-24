package com.example.ibdtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.ibdtracker.Data.CrohnsResponseRepository;
import com.example.ibdtracker.Data.CrohnsSurveyResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CrohnsSurveyActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide app name bar
        setContentView(R.layout.activity_crohns_survey);

        //Bottom navigation bar set up
        BottomNavigationView bottomNav = findViewById(R.id.bnvNavigation); //initialising and assigning the bottomNav variable
        bottomNav.setSelectedItemId(R.id.navSurvey); //setting the selected item in the nav bar

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);


        //set the click listeners for each of the nav bar items
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //switch for each of the menu options
                switch(item.getItemId()) {
                    //settings page
                    case R.id.navSettings:
                        startActivity(new Intent(getApplicationContext(), AppSettingsActivity.class)); //start settings activity
                        finish();
                        return true;
                    //dashboard page
                    case R.id.navDashboard:
                        startActivity(new Intent(getApplicationContext(), CrohnsDashboardActivity.class)); //start survey activity
                        finish(); //end current activity
                    //survey page
                    case R.id.navSurvey:
                        return true; // do nothing
                }
                return false; //if not in switch statement something has gone very wrong
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnSave) {

            float q1 = 10.0f;
            float q2 = 20.0f;
            float q3 = 30.0f;
            float q4 = 40.0f;
            float q5 = 50.0f;
            float q6 = 60.0f;
            float q7 = 70.0f;
            float weight = 80.0f;

            CrohnsSurveyResponse response = new CrohnsSurveyResponse(q1, q2, q3, q4, q5, q6, q7,weight);

            boolean exists = CrohnsResponseRepository.getRepository(getApplicationContext()).exists(response);

            if(exists) {
                CrohnsResponseRepository.getRepository(getApplicationContext()).updateResponse(response);
            }
            else {
                CrohnsResponseRepository.getRepository(getApplicationContext()).storeCrohnsResponse(response);
            }


        }
    }
}