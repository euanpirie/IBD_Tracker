package com.example.ibdtracker.Crohns;

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
import com.example.ibdtracker.Data.Colitis.ColitisSurveyResponse;
import com.example.ibdtracker.Data.Crohns.CrohnsResponseRepository;
import com.example.ibdtracker.Data.Crohns.CrohnsSurveyResponse;
import com.example.ibdtracker.MainActivity;
import com.example.ibdtracker.Misc.MiscActivity;
import com.example.ibdtracker.R;
import com.example.ibdtracker.SelectorActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CrohnsSurveyActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences sharedPreferences; //the users saved preferences

    //Instance state variable setup
    private static final String CROHNS_Q1_SAVE = "crohnsQ1";
    private static final String CROHNS_Q2_SAVE = "crohnsQ2";
    private static final String CROHNS_Q3_SAVE = "crohnsQ3";
    private static final String CROHNS_Q4_SAVE = "crohnsQ4";
    private static final String CROHNS_Q5_SAVE = "crohnsQ5";
    private static final String CROHNS_Q6_SAVE = "crohnsQ6";
    private static final String CROHNS_Q7_SAVE = "crohnsQ7";

    //current date variable set up
    private static LocalDate currentDate;
    private static String currentDateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide app name bar
        setContentView(R.layout.activity_crohns_survey);

        //Get the shared preferences file
        sharedPreferences = getSharedPreferences(SelectorActivity.SHARED_PREF_FILE, MODE_PRIVATE);

        //get the current date, set up a string to copy
        currentDate = LocalDate.now();
        currentDateString = currentDate.toString();

        //update the date text view
        TextView tvSurveyDate = findViewById(R.id.tvSurveyDate);
        tvSurveyDate.setText("Response for: " + currentDateString);

        //get the list of all survey responses
        List<CrohnsSurveyResponse> responseList = CrohnsResponseRepository.getRepository(getApplicationContext()).getAllResponsesSorted();

        //find a survey with todays date
        CrohnsSurveyResponse response = responseList.stream()
                .filter(r -> LocalDate.now().toString().equals(r.getDate()))
                .findAny()
                .orElse(null);

        //if there is nothing in saved instances and if there is a response for the date, update view to display answers
        if(savedInstanceState == null && response !=null) {
            updateSurveyWithResponse(response);
        }

        //if there is something saved in saved instances
        if(savedInstanceState !=null) {
            //get the neccessary info to update the view from saved instances
            String q1 = savedInstanceState.getString(CROHNS_Q1_SAVE);
            int q2 = savedInstanceState.getInt(CROHNS_Q2_SAVE);
            int q3 = savedInstanceState.getInt(CROHNS_Q3_SAVE);
            int q4 = savedInstanceState.getInt(CROHNS_Q4_SAVE);
            String q5 = savedInstanceState.getString(CROHNS_Q5_SAVE);
            int q6 = savedInstanceState.getInt(CROHNS_Q6_SAVE);
            String q7 = savedInstanceState.getString(CROHNS_Q7_SAVE);

            //update the view
            updateSurvey(q1, q2, q3, q4, q5, q6, q7);
        }

        //if the response list isn't empty and isnt' null
        if(!responseList.isEmpty() && responseList != null) {
            //get the latest response from the response list
            CrohnsSurveyResponse latestResponse = responseList.get(responseList.size() - 1);

            //if there is a response after the systems current date, notify the user
            if(latestResponse != null && LocalDate.now().isBefore(LocalDate.parse(latestResponse.getDate()))) {
                showToast("There is a response for a date later than your systems current date. Please keep this in mind.", Toast.LENGTH_LONG);
            }
        }

        //Bottom navigation bar set up
        BottomNavigationView bottomNav = findViewById(R.id.bnvNavigation); //initialising and assigning the bottomNav variable
        bottomNav.setSelectedItemId(R.id.navSurvey); //setting the selected item in the nav bar

        //add on click listener to the save button
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        //add on click listener to previous day button
        Button btnPreviousDay = findViewById(R.id.btnPreviousDay);
        btnPreviousDay.setOnClickListener(this);

        //add on click listener to next day button
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
                        startActivity(new Intent(getApplicationContext(), CrohnsDashboardActivity.class)); //start survey activity
                        finish(); //end current activity
                    //survey page
                    case R.id.navSurvey:
                        return true; // do nothing
                    //misc page
                    case R.id.navMisc:
                        startActivity(new Intent(getApplicationContext(), MiscActivity.class)); //start misc activity
                        finish(); //end current activity
                        return true;
                }
                return false; //if not in switch statement something has gone very wrong
            }
        });
    }

    /**
     * On click method for various View items
     * @param view the item that has been pressed/clicked
     */
    @Override
    public void onClick(View view) {

        //find all of the questions from the view
        EditText CrohnsQ1 = findViewById(R.id.numberCrohnsQ1);
        RadioGroup CrohnsQ2 = findViewById(R.id.rgCrohnsQ2);
        RadioGroup CrohnsQ3 = findViewById(R.id.rgCrohnsQ3);
        RadioGroup CrohnsQ4 = findViewById(R.id.rgCrohnsQ4);
        ChipGroup CrohnsQ5 = findViewById(R.id.cgCrohnsQ5);
        RadioGroup CrohnsQ6 = findViewById(R.id.rgCrohnsQ6);
        EditText CrohnsQ7 = findViewById(R.id.numberCrohnsQ7);

        if(view.getId() == R.id.btnSave) {

            //get the string version of the free text inputs
            String CrohnsQ1AnswerString = CrohnsQ1.getText().toString();
            String CrohnsQ7AnswerString = CrohnsQ7.getText().toString();

            //if the strings are invalid, show an error message
            if(CrohnsQ1AnswerString.isEmpty() || CrohnsQ1AnswerString == null || CrohnsQ7AnswerString.isEmpty() || CrohnsQ7AnswerString == null) {
                showToast("Please answer all questions", Toast.LENGTH_LONG);
            }

            //if the numeric answers are invalid show an error message
            else if(Integer.valueOf(CrohnsQ1AnswerString) < 0 || Float.parseFloat(CrohnsQ7AnswerString) < 0) {
                showToast("Please enter valid numbers.", Toast.LENGTH_LONG);
            }

            //otherwise
            else {
                //convert the 2 strings to their respective answers
                int CrohnsQ1Answer = Integer.valueOf(CrohnsQ1AnswerString);
                float weight = Float.parseFloat(CrohnsQ7AnswerString);

                //calculate the deviation from the standard weight
                //get the typical weight from shared preferences
                float typicalWeight = sharedPreferences.getFloat(MainActivity.TYPICAL_WEIGHT_KEY, 0.0f);
                float CrohnsQ7Answer = Math.round((weight - typicalWeight)/typicalWeight);
                CrohnsQ7Answer = Math.abs(CrohnsQ7Answer);

                //setup floats for each of the other answers
                float CrohnsQ2Answer;
                float CrohnsQ3Answer;
                float CrohnsQ4Answer;
                float CrohnsQ5Answer;
                float CrohnsQ6Answer;

                //get the IDs of all of the selected values from the radio groups
                int CrohnsQ2AnswerID = CrohnsQ2.getCheckedRadioButtonId();
                int CrohnsQ3AnswerID = CrohnsQ3.getCheckedRadioButtonId();
                int CrohnsQ4AnswerID = CrohnsQ4.getCheckedRadioButtonId();
                int CrohnsQ6AnswerID = CrohnsQ6.getCheckedRadioButtonId();

                //get the IDs of all of the selectedd items from the chip group
                List CrohnsQ5AnswerID = CrohnsQ5.getCheckedChipIds();

                //set the score depending on the selected value
                switch(CrohnsQ2AnswerID) {
                    case R.id.rbCrohnsQ2A1:
                        CrohnsQ2Answer = 1.0f;
                        break;
                    case R.id.rbCrohnsQ2A2:
                        CrohnsQ2Answer = 0.0f;
                        break;
                    default:
                        CrohnsQ2Answer = 0.0f;
                        break;
                }

                //set the score depending on the selected value
                switch(CrohnsQ3AnswerID) {
                    case R.id.rbCrohnsQ3A1:
                        CrohnsQ3Answer = 0.0f;
                        break;
                    case R.id.rbCrohnsQ3A2:
                        CrohnsQ3Answer = 1.0f;
                        break;
                    case R.id.rbCrohnsQ3A3:
                        CrohnsQ3Answer = 2.0f;
                        break;
                    case R.id.rbCrohnsQ3A4:
                        CrohnsQ3Answer = 3.0f;
                        break;
                    default:
                        CrohnsQ3Answer = 0.0f;
                        break;
                }

                //set the score depending on the selected value
                switch(CrohnsQ4AnswerID) {
                    case R.id.rbCrohnsQ4A1:
                        CrohnsQ4Answer = 0.0f;
                        break;
                    case R.id.rbCrohnsQ4A2:
                        CrohnsQ4Answer = 1.0f;
                        break;
                    case R.id.rbCrohnsQ4A3:
                        CrohnsQ4Answer = 2.0f;
                        break;
                    case R.id.rbCrohnsQ4A4:
                        CrohnsQ4Answer = 3.0f;
                        break;
                    case R.id.rbCrohnsQ4A5:
                        CrohnsQ4Answer = 4.0f;
                        break;
                    default:
                        CrohnsQ4Answer = 0.0f;
                        break;
                }

                //multiply the size of the list by 20 to get the final score
                CrohnsQ5Answer = CrohnsQ5AnswerID.size();

                //convert the selected chip IDS into a string - removing the []
                String CrohnsQ5IDString = CrohnsQ5AnswerID.toString();
                CrohnsQ5IDString = CrohnsQ5IDString.substring(1, CrohnsQ5IDString.length()-1);

                //set the score depending on the selected value
                switch(CrohnsQ6AnswerID) {
                    case R.id.rbCrohnsQ6A1:
                        CrohnsQ6Answer = 0.0f;
                        break;
                    case R.id.rbCrohnsQ6A2:
                        CrohnsQ6Answer = 20.0f;
                        break;
                    case R.id.rbCrohnsQ6A3:
                        CrohnsQ6Answer = 50.0f;
                        break;
                    default:
                        CrohnsQ6Answer = 0.0f;
                        break;
                }

                //create a new surbey response with all of the data
                CrohnsSurveyResponse response = new CrohnsSurveyResponse(CrohnsQ1Answer,
                        CrohnsQ2Answer,
                        CrohnsQ3Answer,
                        CrohnsQ4Answer,
                        CrohnsQ5Answer,
                        CrohnsQ6Answer,
                        CrohnsQ7Answer,
                        CrohnsQ2AnswerID,
                        CrohnsQ3AnswerID,
                        CrohnsQ4AnswerID,
                        CrohnsQ6AnswerID,
                        CrohnsQ5IDString,
                        weight);

                //update the date
                response.setDate(currentDateString);

                //check if the response exists in the response database
                boolean exists = CrohnsResponseRepository.getRepository(getApplicationContext()).exists(response);

                //if it exists, update the record, if not add it to the db
                if(exists) {
                    CrohnsResponseRepository.getRepository(getApplicationContext()).updateResponse(response);
                }
                else {
                    CrohnsResponseRepository.getRepository(getApplicationContext()).storeCrohnsResponse(response);
                }
            }
        }

        //if the previous day button was pressed
        else if(view.getId() == R.id.btnPreviousDay) {
            //subtract one day from the current day, update the current day string
            currentDate = currentDate.minusDays(1);
            currentDateString = currentDate.toString();

            //get the list of all survey responses
            List<CrohnsSurveyResponse> responseList = CrohnsResponseRepository.getRepository(getApplicationContext()).getAllResponses();

            //find a survey with the current date
            CrohnsSurveyResponse response = responseList.stream()
                    .filter(r -> currentDateString.equals(r.getDate()))
                    .findAny()
                    .orElse(null);

            //if there is nothing in saved instances and if there is a response for the date, update view to display answers
            if(response !=null) {
                updateSurveyWithResponse(response);
            }
            //otherwise, create a blank survey for the new date
            else {
                updateSurvey("", R.id.rbCrohnsQ2A1, R.id.rbCrohnsQ3A1, R.id.rbCrohnsQ4A1, "", R.id.rbCrohnsQ6A1, "");
            }
        }

        //if the next day button was pressed
        else if(view.getId() == R.id.btnNextDay) {
            //add one day to current date and update string
            currentDate = currentDate.plusDays(1);
            currentDateString = currentDate.toString();

            //get the list of all survey responses
            List<CrohnsSurveyResponse> responseList = CrohnsResponseRepository.getRepository(getApplicationContext()).getAllResponses();

            //find a survey with the current date
            CrohnsSurveyResponse response = responseList.stream()
                    .filter(r -> currentDateString.equals(r.getDate()))
                    .findAny()
                    .orElse(null);

            //if there is a response update the view
            if(response !=null) {
                updateSurveyWithResponse(response);
            }

            //if there is no response, and the new date is after the system date, undo changes to date and display error
            else if(currentDate.isAfter(LocalDate.now())) {
                currentDate = currentDate.minusDays(1);
                currentDateString = currentDate.toString();

                showToast("You can't go past this date.", Toast.LENGTH_LONG);
            }

            //otherwise updaye the layout
            else {
                updateSurvey("", R.id.rbCrohnsQ2A1, R.id.rbCrohnsQ3A1, R.id.rbCrohnsQ4A1, "", R.id.rbCrohnsQ6A1, "");
            }

        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        //find all of the questions from the view
        EditText CrohnsQ1 = findViewById(R.id.numberCrohnsQ1);
        RadioGroup CrohnsQ2 = findViewById(R.id.rgCrohnsQ2);
        RadioGroup CrohnsQ3 = findViewById(R.id.rgCrohnsQ3);
        RadioGroup CrohnsQ4 = findViewById(R.id.rgCrohnsQ4);
        ChipGroup CrohnsQ5 = findViewById(R.id.cgCrohnsQ5);
        RadioGroup CrohnsQ6 = findViewById(R.id.rgCrohnsQ6);
        EditText CrohnsQ7 = findViewById(R.id.numberCrohnsQ7);

        //get the string version of the free text inputs
        String CrohnsQ1AnswerString = String.valueOf(CrohnsQ1.getText());
        String CrohnsQ7AnswerString = String.valueOf(CrohnsQ7.getText());

        //get the IDs of all of the selected values from the radio groups
        int CrohnsQ2AnswerID = CrohnsQ2.getCheckedRadioButtonId();
        int CrohnsQ3AnswerID = CrohnsQ3.getCheckedRadioButtonId();
        int CrohnsQ4AnswerID = CrohnsQ4.getCheckedRadioButtonId();
        int CrohnsQ6AnswerID = CrohnsQ6.getCheckedRadioButtonId();

        //get the IDs of all of the selected items from the chip group
        List CrohnsQ5AnswerID = CrohnsQ5.getCheckedChipIds();

        //convert the selected chip IDS into a string - removing the []
        String CrohnsQ5IDString = CrohnsQ5AnswerID.toString();
        CrohnsQ5IDString = CrohnsQ5IDString.substring(1, CrohnsQ5IDString.length()-1);

        //save all details in the out state
        outState.putString(CROHNS_Q1_SAVE, CrohnsQ1AnswerString);
        outState.putString(CROHNS_Q7_SAVE, CrohnsQ7AnswerString);

        outState.putInt(CROHNS_Q2_SAVE, CrohnsQ2AnswerID);
        outState.putInt(CROHNS_Q3_SAVE, CrohnsQ3AnswerID);
        outState.putInt(CROHNS_Q4_SAVE, CrohnsQ4AnswerID);
        outState.putInt(CROHNS_Q6_SAVE, CrohnsQ6AnswerID);

        outState.putString(CROHNS_Q5_SAVE, CrohnsQ5IDString);
    }

    /**
     * a method to update the survey with given info
     * @param q1 the string for q1
     * @param q2 the checked option of q2
     * @param q3 the checked option of q3
     * @param q4 the checked option of q4
     * @param q5 the string format of all checked ids for q5
     * @param q6 the checked option of q6
     * @param q7 the string for q7
     */
    public void updateSurvey(String q1, int q2, int q3, int q4, String q5, int q6, String q7) {
        //find all of the questions from the view
        EditText CrohnsQ1 = findViewById(R.id.numberCrohnsQ1);
        RadioGroup CrohnsQ2 = findViewById(R.id.rgCrohnsQ2);
        RadioGroup CrohnsQ3 = findViewById(R.id.rgCrohnsQ3);
        RadioGroup CrohnsQ4 = findViewById(R.id.rgCrohnsQ4);
        ChipGroup CrohnsQ5 = findViewById(R.id.cgCrohnsQ5);
        RadioGroup CrohnsQ6 = findViewById(R.id.rgCrohnsQ6);
        EditText CrohnsQ7 = findViewById(R.id.numberCrohnsQ7);

        //set the edit text views with their respective answer
        CrohnsQ1.setText(q1);
        CrohnsQ7.setText(q7);

        //check the radio buttons
        CrohnsQ2.check(q2);
        CrohnsQ3.check(q3);
        CrohnsQ4.check(q4);
        CrohnsQ6.check(q6);

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
                CrohnsQ5.check(i);
            }
        }
        //if the string is empty, uncheck all
        else {
            CrohnsQ5.clearCheck();
        }

        //update the date text view
        TextView tvSurveyDate = findViewById(R.id.tvSurveyDate);
        tvSurveyDate.setText("Response for: " + currentDateString);
    }

    /**
     * Update the layout using a response object
     * @param response
     */
    public void updateSurveyWithResponse(CrohnsSurveyResponse response) {
        //get all of the info neccessary to update the view from the response
        String q1 = String.valueOf(response.getCrohnsQ1());
        int q2 = response.getCrohnsQ2ID();
        int q3 = response.getCrohnsQ3ID();
        int q4 = response.getCrohnsQ4ID();
        String q5 = response.getCrohnsQ5ID();
        int q6 = response.getCrohnsQ6ID();
        String q7 = String.valueOf(response.getWeight());

        //update the layout
        updateSurvey(q1, q2, q3, q4, q5 ,q6, q7);
    }

    /**
     * A method to create and display a toast
     * @param message the message to be shown
     * @param length the length of the toast
     */
    public void showToast(String message, int length) {
        //create a toast and display it
        Toast toast = Toast.makeText(getApplicationContext(), message, length);
        toast.show();
    }
}