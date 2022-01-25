package com.example.ibdtracker;

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
import android.widget.Toast;

import com.example.ibdtracker.Data.CrohnsResponseRepository;
import com.example.ibdtracker.Data.CrohnsSurveyResponse;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide app name bar
        setContentView(R.layout.activity_crohns_survey);

        //Get the shared preferences file
        sharedPreferences = getSharedPreferences(SelectorActivity.SHARED_PREF_FILE, MODE_PRIVATE);

        //get the list of all survey responses
        List<CrohnsSurveyResponse> responseList = CrohnsResponseRepository.getRepository(getApplicationContext()).getAllResponses();

        //find a survey with todays date
        CrohnsSurveyResponse response = responseList.stream()
                .filter(test -> LocalDate.now().toString().equals(test.getDate()))
                .findAny()
                .orElse(null);

        //if there is nothing in saved instances and if there is a response for the date, update view to display answers
        if(savedInstanceState == null && response !=null) {
            //find all of the questions from the view
            EditText CrohnsQ1 = findViewById(R.id.numberCrohnsQ1);
            RadioGroup CrohnsQ2 = findViewById(R.id.rgCrohnsQ2);
            RadioGroup CrohnsQ3 = findViewById(R.id.rgCrohnsQ3);
            RadioGroup CrohnsQ4 = findViewById(R.id.rgCrohnsQ4);
            ChipGroup CrohnsQ5 = findViewById(R.id.cgCrohnsQ5);
            RadioGroup CrohnsQ6 = findViewById(R.id.rgCrohnsQ6);
            EditText CrohnsQ7 = findViewById(R.id.numberCrohnsQ7);

            //set the edit text fields to their respective saved values
            CrohnsQ1.setText(String.valueOf(response.getCrohnsQ1()));
            CrohnsQ7.setText(String.valueOf(response.getWeight()));

            //check all of the saved radio buttons for each radio group
            CrohnsQ2.check(response.getCrohnsQ2ID());
            CrohnsQ3.check(response.getCrohnsQ3ID());
            CrohnsQ4.check(response.getCrohnsQ4ID());
            CrohnsQ6.check(response.getCrohnsQ6ID());

            //get the string format of all the checked chip IDs
            String CrohnsQ5IDString = response.getCrohnsQ5ID();

            //if the string isn't empty or null
            if(!CrohnsQ5IDString.isEmpty() && !CrohnsQ5IDString.equals(null)) {
                //seperate the string into a list of strings, splitting by comma and space
                List<String> q5StringList = new ArrayList<String>(Arrays.asList(response.getCrohnsQ5ID().split(", ")));

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
        }

        //if there is something saved in saved instances
        if(savedInstanceState !=null) {
            //find all of the questions from the view
            EditText CrohnsQ1 = findViewById(R.id.numberCrohnsQ1);
            RadioGroup CrohnsQ2 = findViewById(R.id.rgCrohnsQ2);
            RadioGroup CrohnsQ3 = findViewById(R.id.rgCrohnsQ3);
            RadioGroup CrohnsQ4 = findViewById(R.id.rgCrohnsQ4);
            ChipGroup CrohnsQ5 = findViewById(R.id.cgCrohnsQ5);
            RadioGroup CrohnsQ6 = findViewById(R.id.rgCrohnsQ6);
            EditText CrohnsQ7 = findViewById(R.id.numberCrohnsQ7);

            //set the edit text views with their respective answer
            CrohnsQ1.setText(savedInstanceState.getString(CROHNS_Q1_SAVE));
            CrohnsQ7.setText(savedInstanceState.getString(CROHNS_Q7_SAVE));

            //check the radio buttons stored in saved instances
            CrohnsQ2.check(savedInstanceState.getInt(CROHNS_Q2_SAVE));
            CrohnsQ3.check(savedInstanceState.getInt(CROHNS_Q3_SAVE));
            CrohnsQ4.check(savedInstanceState.getInt(CROHNS_Q4_SAVE));
            CrohnsQ6.check(savedInstanceState.getInt(CROHNS_Q6_SAVE));

            //get the string format of all the checked chip IDs
            String CrohnsQ5IDString = savedInstanceState.getString(CROHNS_Q5_SAVE);

            //if the string isn't empty or null
            if(!CrohnsQ5IDString.isEmpty() && !CrohnsQ5IDString.equals(null)) {
                //seperate the string into a list of strings, splitting by comma and space
                List<String> q5StringList = new ArrayList<String>(Arrays.asList(response.getCrohnsQ5ID().split(", ")));

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
        }

        //Bottom navigation bar set up
        BottomNavigationView bottomNav = findViewById(R.id.bnvNavigation); //initialising and assigning the bottomNav variable
        bottomNav.setSelectedItemId(R.id.navSurvey); //setting the selected item in the nav bar

        //add on click listener to the save button
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

            //if the strings are invalid, show an error message
            if(CrohnsQ1AnswerString.isEmpty() || CrohnsQ1AnswerString == null || CrohnsQ7AnswerString.isEmpty() || CrohnsQ7AnswerString == null) {
                Toast toast =  Toast.makeText(this.getApplicationContext(), "Please answer every question", Toast.LENGTH_SHORT);
                toast.show();
            }

            //otherwise
            else {
                //convert the 2 strings to their respective answers
                float CrohnsQ1Answer = Float.parseFloat(CrohnsQ1AnswerString);
                float weight = Float.parseFloat(CrohnsQ7AnswerString);

                //calculate the deviation from the standard weight
                //get the typical weight from shared preferences
                float typicalWeight = sharedPreferences.getFloat(MainActivity.TYPICAL_WEIGHT_KEY, 0.0f);
                float CrohnsQ7Answer = (weight - typicalWeight)/(typicalWeight * 100);

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
                        CrohnsQ2Answer = 30.0f;
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
                        CrohnsQ3Answer = 35.0f;
                        break;
                    case R.id.rbCrohnsQ3A3:
                        CrohnsQ3Answer = 70.0f;
                        break;
                    case R.id.rbCrohnsQ3A4:
                        CrohnsQ3Answer = 105.0f;
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
                        CrohnsQ4Answer = 49.0f;
                        break;
                    case R.id.rbCrohnsQ4A3:
                        CrohnsQ4Answer = 98.0f;
                        break;
                    case R.id.rbCrohnsQ4A4:
                        CrohnsQ4Answer = 147.0f;
                        break;
                    case R.id.rbCrohnsQ4A5:
                        CrohnsQ4Answer = 196.0f;
                        break;
                    default:
                        CrohnsQ4Answer = 0.0f;
                        break;
                }

                //multiply the size of the list by 20 to get the final score
                CrohnsQ5Answer = CrohnsQ5AnswerID.size() * 20.0f;

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
}