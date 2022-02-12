package com.example.ibdtracker.Colitis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ibdtracker.AI.RuleBasedSystem;
import com.example.ibdtracker.AppSettingsActivity;
import com.example.ibdtracker.Data.Colitis.ColitisResponseRepository;
import com.example.ibdtracker.Data.Colitis.ColitisSurveyResponse;
import com.example.ibdtracker.Data.Crohns.CrohnsResponseRepository;
import com.example.ibdtracker.Data.Crohns.CrohnsSurveyResponse;
import com.example.ibdtracker.MainActivity;
import com.example.ibdtracker.R;
import com.example.ibdtracker.SelectorActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.List;

public class ColitisDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPreferences; //the users saved preferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide app name bar
        setContentView(R.layout.activity_colitis_dashboard);

        //Get the shared preferences file
        sharedPreferences = getSharedPreferences(SelectorActivity.SHARED_PREF_FILE, MODE_PRIVATE);

        //add on click to predict button
        Button btnPredict = findViewById(R.id.btnPredictColitis);
        btnPredict.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        //if the predict button was pressed
        if(view.getId() == R.id.btnPredictColitis) {
            //get the text view
            TextView tvPrediction = findViewById(R.id.tvPrediction);

            //get the response
            List<ColitisSurveyResponse> responses = ColitisResponseRepository.getRepository(getApplicationContext()).getAllResponsesSorted();

            //create the rule based system
            RuleBasedSystem rbs = new RuleBasedSystem(sharedPreferences.getString(MainActivity.IBD_TYPE_KEY, ""));

            //make the prediction
            String prediction = rbs.predictStatus(responses);

            //update the text view
            tvPrediction.setText(prediction);
        }
    }
}