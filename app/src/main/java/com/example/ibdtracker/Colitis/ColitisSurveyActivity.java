package com.example.ibdtracker.Colitis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ibdtracker.AppSettingsActivity;
import com.example.ibdtracker.Data.Colitis.ColitisResponseRepository;
import com.example.ibdtracker.Data.Colitis.ColitisSurveyResponse;
import com.example.ibdtracker.Data.Crohns.CrohnsSurveyResponse;
import com.example.ibdtracker.R;
import com.example.ibdtracker.SelectorActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColitisSurveyActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPreferences; //the users saved preferences

    //instance state variable setup
    private static final String COLITIS_Q1_SAVE = "colitisQ1";
    private static final String COLITIS_Q2_SAVE = "colitisQ2";
    private static final String COLITIS_Q3_SAVE = "colitisQ3";
    private static final String COLITIS_Q4_SAVE = "colitisQ4";
    private static final String COLITIS_Q5_SAVE = "colitisQ5";
    private static final String COLITIS_Q6_SAVE = "colitisQ6";
    private static final String COLITIS_WEIGHT_SAVE = "colitisWeight";

    //current date variable set up
    private static LocalDate currentDate;
    private static String currentDateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide app name bar
        setContentView(R.layout.activity_colitis_survey);

        //Get the shared preferences file
        sharedPreferences = getSharedPreferences(SelectorActivity.SHARED_PREF_FILE, MODE_PRIVATE);

        //get the current date, set up a string to copy
        currentDate = LocalDate.now();
        currentDateString = currentDate.toString();

        //update the date text view
        TextView tvSurveyDate = findViewById(R.id.tvSurveyDate);
        tvSurveyDate.setText("Response for: " + currentDateString);

        //get all colitis survey responses
        List<ColitisSurveyResponse> responseList = ColitisResponseRepository.getRepository(getApplicationContext()).getAllResponsesSorted();

        //find a survey with todays date
        ColitisSurveyResponse response = responseList.stream()
                .filter(r -> LocalDate.now().toString().equals(r.getDate()))
                .findAny()
                .orElse(null);

        //if there is nothing saved in saved instances and there is a response for the current date
        if(savedInstanceState == null && response != null) {
            //update the layout
            updateSurveyWithResponse(response);
        }

        //if the response list isn't empty and isnt' null
        if(!responseList.isEmpty() && responseList != null) {
            //get the latest response from the response list
            ColitisSurveyResponse latestResponse = responseList.get(responseList.size() - 1);

            //if there is a response after the systems current date, notify the user
            if(latestResponse != null && LocalDate.now().isBefore(LocalDate.parse(latestResponse.getDate()))) {
                showToast("There is a response for a date later than your systems current date. Please keep this in mind.", Toast.LENGTH_LONG);
            }
        }

        //if there is something saved in saved instances
        if(savedInstanceState != null) {
            //get everything from saved instances
            String q1 = savedInstanceState.getString(COLITIS_Q1_SAVE);
            String q2 = savedInstanceState.getString(COLITIS_Q2_SAVE);
            int q3 = savedInstanceState.getInt(COLITIS_Q3_SAVE);
            String q4 = savedInstanceState.getString(COLITIS_Q4_SAVE);
            String q5 = savedInstanceState.getString(COLITIS_Q5_SAVE);
            int q6 = savedInstanceState.getInt(COLITIS_Q6_SAVE);
            String weight = savedInstanceState.getString(COLITIS_WEIGHT_SAVE);

            //update the layout
            updateSurvey(q1, q2, q3, q4, q5, q6, weight);
        }

        //Bottom navigation bar set up
        BottomNavigationView bottomNav = findViewById(R.id.bnvNavigation); //initialising and assigning the bottomNav variable
        bottomNav.setSelectedItemId(R.id.navSurvey); //setting the selected item in the nav bar

        //add on click listener to save button
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        //add on click listener to previous day button
        Button btnPreviousDay = findViewById(R.id.btnPreviousDay);
        btnPreviousDay.setOnClickListener(this);

        //add on click listener to next day
        Button btnNextDay = findViewById(R.id.btnNextDay);
        btnNextDay.setOnClickListener(this);

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
                        startActivity(new Intent(getApplicationContext(), ColitisDashboardActivity.class)); //start survey activity
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        //get all of the question answer elements
        EditText colitisQ1 = findViewById(R.id.numberColitisQ1A);
        EditText colitisQ2 = findViewById(R.id.numberColitisQ2A);
        RadioGroup colitisQ3 = findViewById(R.id.rgColitisQ3);
        EditText colitisQ4 = findViewById(R.id.numberColitisQ4A);
        ChipGroup colitisQ5 = findViewById(R.id.cgColitisQ5);
        RadioGroup colitisQ6 = findViewById(R.id.rgColitisQ6);
        EditText weightEntry = findViewById(R.id.numberWeight);

        //get the string versions of the free text inputs
        String colitisQ1AnswerString = colitisQ1.getText().toString();
        String colitisQ2AnswerString = colitisQ2.getText().toString();
        String colitisQ4AnswerString = colitisQ4.getText().toString();
        String weightString = weightEntry.getText().toString();

        //get checked radio button id
        int colitisQ3AnswerID = colitisQ3.getCheckedRadioButtonId();
        int colitisQ6AnswerID = colitisQ6.getCheckedRadioButtonId();

        //get the IDs of all of the selected items from the chip group
        List colitisQ5AnswerIDs = colitisQ5.getCheckedChipIds();

        //convert the selected chip IDS into a string - removing the []
        String colitisQ5IDString = colitisQ5AnswerIDs.toString();
        colitisQ5IDString = colitisQ5IDString.substring(1, colitisQ5IDString.length() -1);

        //save the details in the outstate
        outState.putString(COLITIS_Q1_SAVE, colitisQ1AnswerString);
        outState.putString(COLITIS_Q2_SAVE, colitisQ2AnswerString);
        outState.putString(COLITIS_Q4_SAVE, colitisQ4AnswerString);
        outState.putString(COLITIS_WEIGHT_SAVE, weightString);

        outState.putInt(COLITIS_Q3_SAVE, colitisQ3AnswerID);
        outState.putInt(COLITIS_Q6_SAVE, colitisQ6AnswerID);

        outState.putString(COLITIS_Q5_SAVE, colitisQ5IDString);
    }

    /**
     * On click method for various Views
     * @param view the View item that was pressed/clicked
     */
    @Override
    public void onClick(View view) {

        //get all of the question answer elements
        EditText colitisQ1 = findViewById(R.id.numberColitisQ1A);
        EditText colitisQ2 = findViewById(R.id.numberColitisQ2A);
        RadioGroup colitisQ3 = findViewById(R.id.rgColitisQ3);
        EditText colitisQ4 = findViewById(R.id.numberColitisQ4A);
        ChipGroup colitisQ5 = findViewById(R.id.cgColitisQ5);
        RadioGroup colitisQ6 = findViewById(R.id.rgColitisQ6);
        EditText weightEntry = findViewById(R.id.numberWeight);

        //if the save button was pressed
        if(view.getId() == R.id.btnSave) {

            //get the string versions of the free text inputs
            String colitisQ1AnswerString = colitisQ1.getText().toString();
            String colitisQ2AnswerString = colitisQ2.getText().toString();
            String colitisQ4AnswerString = colitisQ4.getText().toString();
            String weightString = weightEntry.getText().toString();

            //if the strings are invalid show an error message
            if(colitisQ1AnswerString.isEmpty() || colitisQ1AnswerString.equals(null) ||
            colitisQ2AnswerString.isEmpty() || colitisQ2AnswerString.equals(null) ||
            colitisQ4AnswerString.isEmpty() || colitisQ4AnswerString.equals(null) ||
            weightString.isEmpty() || weightString.equals(null)) {
                showToast("Please answer all questions.", Toast.LENGTH_LONG);
            }
            //if the numeric values entered are outside of the boundaries
            else if(Float.parseFloat(colitisQ1AnswerString) < 0 ||
            Float.parseFloat(colitisQ2AnswerString) < 0 ||
            Float.parseFloat(colitisQ4AnswerString) < 0 ||
            Float.parseFloat(colitisQ4AnswerString) > 10 ||
            Float.parseFloat(weightString) < 0) {
                showToast("Please enter valid numeric values.", Toast.LENGTH_LONG);
            }
            //otherwise
            else {
                //convert the strings to ints
                int colitisQ1Answer = Integer.valueOf(colitisQ1AnswerString);
                int colitisQ2Answer = Integer.valueOf(colitisQ2AnswerString);
                int colitisQ4Answer = Integer.valueOf(colitisQ4AnswerString);

                //convert the string to floats
                float weight = Float.parseFloat(weightString);

                //other answer float setup
                float colitisQ3Answer;
                float colitisQ5Answer;
                float colitisQ6Answer;

                //get checked radio button id
                int colitisQ3AnswerID = colitisQ3.getCheckedRadioButtonId();
                int colitisQ6AnswerID = colitisQ6.getCheckedRadioButtonId();

                //get all checked chips for q5
                List colitisQ5AnswerIDs = colitisQ5.getCheckedChipIds();

                //set the score depending on the chosen value
                switch(colitisQ3AnswerID) {
                    case R.id.rbColitisQ3A1:
                        colitisQ3Answer = 0f;
                        break;
                    case R.id.rbColitisQ3A2:
                        colitisQ3Answer = 1.0f;
                        break;
                    case R.id.rbColitisQ3A3:
                        colitisQ3Answer = 2.0f;
                        break;
                    case R.id.rbColitisQ3A4:
                        colitisQ3Answer = 3.0f;
                        break;
                    default:
                        colitisQ3Answer = 0f;
                        break;
                }

                switch(colitisQ6AnswerID) {
                    case R.id.rbColitisQ6A1:
                        colitisQ6Answer = 0f;
                        break;
                    case R.id.rbColitisQ6A2:
                        colitisQ6Answer = 1.0f;
                        break;
                    case R.id.rbColitisQ6A3:
                        colitisQ6Answer = 2.0f;
                        break;
                    case R.id.rbColitisQ6A4:
                        colitisQ6Answer = 3.0f;
                        break;
                    default:
                        colitisQ6Answer = 0f;
                        break;
                }

                //set the answer to the size of the selected IDs list
                colitisQ5Answer = colitisQ5AnswerIDs.size() * 1.0f;

                //convert the selected chip IDS into a string - removing the []
                String colitisQ5IDString = colitisQ5AnswerIDs.toString();
                colitisQ5IDString = colitisQ5IDString.substring(1, colitisQ5IDString.length() -1);

                //create a new survey response object
                ColitisSurveyResponse response = new ColitisSurveyResponse(colitisQ1Answer,
                        colitisQ2Answer,
                        colitisQ3Answer,
                        colitisQ4Answer,
                        colitisQ5Answer,
                        colitisQ6Answer,
                        weight,
                        colitisQ3AnswerID,
                        colitisQ5IDString,
                        colitisQ6AnswerID);

                //update the date
                response.setDate(currentDateString);

                //check if a record for the date existis
                boolean exists = ColitisResponseRepository.getRepository(getApplicationContext()).exisits(response);

                //if a record exists, update it
                if(exists) {
                    ColitisResponseRepository.getRepository(getApplicationContext()).updateResponse(response);
                }
                //otherwise it doesn't so create a record
                else {
                    ColitisResponseRepository.getRepository(getApplicationContext()).storeColitisResponse(response);
                }
            }
        }
        //if the button was the previous day button
        else if(view.getId() == R.id.btnPreviousDay) {
            //subtract one day from the current day, update the current day string
            currentDate = currentDate.minusDays(1);
            currentDateString = currentDate.toString();

            //get all colitis survey responses
            List<ColitisSurveyResponse> responseList = ColitisResponseRepository.getRepository(getApplicationContext()).getAllResponses();

            //find a survey with todays date
            ColitisSurveyResponse response = responseList.stream()
                    .filter(r -> currentDateString.equals(r.getDate()))
                    .findAny()
                    .orElse(null);

            //if there is a date update the layout
            if(response != null) {
                updateSurveyWithResponse(response);
            }
            //otherwise create a blank survey
            else {
                updateSurvey("", "", R.id.rbColitisQ3A1, "", "", R.id.rbColitisQ6A1,"");
            }
        }

        //if the next day button was pressed
        else if(view.getId() == R.id.btnNextDay) {
            //add a day to the current day and update the string
            currentDate = currentDate.plusDays(1);
            currentDateString = currentDate.toString();

            //get all colitis survey responses
            List<ColitisSurveyResponse> responseList = ColitisResponseRepository.getRepository(getApplicationContext()).getAllResponses();

            //find a survey with todays date
            ColitisSurveyResponse response = responseList.stream()
                    .filter(r -> currentDateString.equals(r.getDate()))
                    .findAny()
                    .orElse(null);

            //if there is a response update the layout
            if(response != null) {
                updateSurveyWithResponse(response);
            }

            //if there is no response, and the new date is after the system date, undo changes to date and display error
            else if(currentDate.isAfter(LocalDate.now())) {
                currentDate = currentDate.minusDays(1);
                currentDateString = currentDate.toString();

                showToast("You can't go past this date.", Toast.LENGTH_LONG);
            }

            //otherwise create a default layout
            else {
                updateSurvey("", "", R.id.rbColitisQ3A1, "", "", R.id.rbColitisQ6A1, "");
            }
        }
    }

    /**
     * a method to update the survey with multiple parameters
     * @param q1 the answer to q1
     * @param q2 the answer to q3
     * @param q3 the id to check of q3
     * @param q4 the answer to q4
     * @param q5 the id of all checked chips for q5
     * @param q6 the id to check for q6
     * @param weight the weight
     */
    private void updateSurvey(String q1, String q2, int q3, String q4, String q5, int q6, String weight) {
        //get all of the question answer elements
        EditText colitisQ1 = findViewById(R.id.numberColitisQ1A);
        EditText colitisQ2 = findViewById(R.id.numberColitisQ2A);
        RadioGroup colitisQ3 = findViewById(R.id.rgColitisQ3);
        EditText colitisQ4 = findViewById(R.id.numberColitisQ4A);
        ChipGroup colitisQ5 = findViewById(R.id.cgColitisQ5);
        RadioGroup colitisQ6 = findViewById(R.id.rgColitisQ6);
        EditText weightEntry = findViewById(R.id.numberWeight);

        //set all of the edit texts
        colitisQ1.setText(q1);
        colitisQ2.setText(q2);
        colitisQ4.setText(q4);
        weightEntry.setText(weight);

        //check the radio group button
        colitisQ3.check(q3);
        colitisQ6.check(q6);

        //if the string isn't empty or null
        if(!q5.isEmpty() && !q5.equals(null)) {
            //seperate the string into a list of strings, splitting by comma and space
            List<String> q5StringList = new ArrayList<String>(Arrays.asList(q5.split(", ")));

            //create a list to store ints
            List<Integer> q5IDList = new ArrayList<Integer>();

            //for each string in the list, convert to int and add to int list
            for(String s: q5StringList) {
                q5IDList.add(Integer.valueOf(s));
            }

            //for ever int in int list, check the chip with that ID
            for (Integer i: q5IDList) {
                colitisQ5.check(i);
            }
        }
        //if the string is empty, uncheck all
        else {
            colitisQ5.clearCheck();
        }

        //update the date text view
        TextView tvSurveyDate = findViewById(R.id.tvSurveyDate);
        tvSurveyDate.setText("Response for: " + currentDateString);
    }

    /**
     * A method to take a response and use it to update the layout
     * @param response the response to populate the layout
     */
    public void updateSurveyWithResponse(ColitisSurveyResponse response) {
        //get data from response
        String q1 = String.valueOf(response.getColitisQ1());
        String q2 = String.valueOf(response.getColitisQ2());
        int q3 = response.getColitisQ3ID();
        String q4 = String.valueOf(response.getColitisQ4());
        String q5 = response.getColitisQ5IDs();
        int q6 = response.getColitisQ6ID();
        String weight = String.valueOf(response.getWeight());

        //update the layout
        updateSurvey(q1, q2, q3, q4, q5, q6, weight);
    }

    /**
     * A method to create and display a toast
     * @param mesasge The message to show
     */
    public void showToast(String mesasge, int length) {
        Toast toast = Toast.makeText(getApplicationContext(), mesasge, length);
        toast.show();
    }
}