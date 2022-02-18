package com.example.ibdtracker.Misc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ibdtracker.Data.Reminder.IBDReminder;
import com.example.ibdtracker.Data.Reminder.ReminderRepository;
import com.example.ibdtracker.R;

public class AddReminderActivity extends AppCompatActivity implements View.OnClickListener {

    //instance state key setup
    private static final String REMINDER_TEXT_KEY = "reminderText";
    private static final String REMINDER_HOUR_KEY = "reminderHour";
    private static final String REMINDER_MINUTE_KEY = "reminderMinute";
    private static final String REMINDER_INTERVAL_KEY = "reminderInterval";
    private static final String REMINDER_INTERVAL_ID_KEY = "reminderIntervalID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide app title
        setContentView(R.layout.activity_add_reminder);

        //add on click listener to the save button
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        //check the intent that started the activity
        Intent launcher = getIntent();

        //if there is something in the saved state
        if(savedInstanceState != null) {
            //get all the info from the saved instance state
            String reminderText = savedInstanceState.getString(REMINDER_TEXT_KEY);
            int hour = savedInstanceState.getInt(REMINDER_HOUR_KEY);
            int minute = savedInstanceState.getInt(REMINDER_MINUTE_KEY);
            int interval = savedInstanceState.getInt(REMINDER_INTERVAL_KEY);
            int intervalID = savedInstanceState.getInt(REMINDER_INTERVAL_ID_KEY);

            //update the layout
            updateLayout(reminderText, hour, minute, interval, intervalID);
        }
        //check if the launcher has extras
        else if(launcher.hasExtra(ReminderRecyclerViewAdapter.EXTRA_REMINDER_TEXT)) {
            //retrieve the info from the intent
            String reminderText = launcher.getStringExtra(ReminderRecyclerViewAdapter.EXTRA_REMINDER_TEXT);
            int hour = launcher.getIntExtra(ReminderRecyclerViewAdapter.EXTRA_REMINDER_HOUR, 12);
            int minute = launcher.getIntExtra(ReminderRecyclerViewAdapter.EXTRA_REMINDER_MINUTE, 0);
            int interval = launcher.getIntExtra(ReminderRecyclerViewAdapter.EXTRA_REMINDER_INTERVAL, 1);
            int intervalID = launcher.getIntExtra(ReminderRecyclerViewAdapter.EXTRA_REMINDER_INTERVAL_TYPE_ID, R.id.rbFrequencyHours);

            //update the layout
            updateLayout(reminderText, hour, minute, interval, intervalID);
        }
    }

    @Override
    public void onClick(View view) {
        //if the save button was pressed
        if(view.getId() == R.id.btnSave) {
            //get the text value
            EditText etReminderTitle = findViewById(R.id.etReminderTitle);
            String reminderText = etReminderTitle.getText().toString();

            //get the hour and minute from the time picker
            TimePicker timePicker = findViewById(R.id.timePicker);
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            //get the reminder interval
            EditText etInterval = findViewById(R.id.etInterval);
            String intervalString = etInterval.getText().toString();

            //get the selected id from the radio group
            RadioGroup rgIntervalType = findViewById(R.id.rgReminderType);
            int intervalTypeID = rgIntervalType.getCheckedRadioButtonId();

            //if any of the strings are empty
            if(reminderText.isEmpty() || reminderText.equals(null) || intervalString.isEmpty() || intervalString.equals(null)) {
                showToast("Please answer all questions.", Toast.LENGTH_LONG);
            }
            //if the interval is a negative number or zero
            else if(Integer.valueOf(intervalString) <=0 ) {
                showToast("Please enter a valid number.", Toast.LENGTH_LONG);
            }
            //otherwise answers are valid
            else {
                //convert the interval to int
                int interval = Integer.valueOf(intervalString);

                //create interval type string
                String intervalType = "";

                //switch radio group
                switch (intervalTypeID) {
                    case R.id.rbFrequencyHours:
                        intervalType = "Hours"; //set interval string
                        break;
                    case R.id.rbFrequencyDays:
                        intervalType = "Days";//set interval string
                        break;
                    case R.id.rbFrequencyWeeks:
                        intervalType = "Weeks"; //set interval string
                        break;
                    case R.id.rbFrequencyMonths:
                        intervalType = "Months"; //set interval string
                        break;
                    default:
                        intervalType = "Days";
                        break;
                }

                //create a reminder object
                IBDReminder reminder = new IBDReminder(
                        reminderText,
                        hour,
                        minute,
                        interval,
                        intervalType,
                        intervalTypeID,
                        false
                        );

                //if the activity was launched from a view adapter
                if(getIntent().hasExtra(ReminderRecyclerViewAdapter.EXTRA_REMINDER_ID)) {
                    //set the ID and the active value
                    reminder.setId(getIntent().getIntExtra(ReminderRecyclerViewAdapter.EXTRA_REMINDER_ID, 0));
                    reminder.setReminderStatus(getIntent().getBooleanExtra(ReminderRecyclerViewAdapter.EXTRA_REMINDER_STATUS, false));
                    //update the reminder in the do
                    ReminderRepository.getRepository(getApplicationContext()).updateReminder(reminder);
                }
                //otherwise create a reminder
                else {
                    ReminderRepository.getRepository(getApplicationContext()).storeReminder(reminder);
                }

                //finish the activity
                finish();
            }

        }
    }

    public void showToast(String message, int length) {
        //create a toast and show it
        Toast toast = Toast.makeText(getApplicationContext(), message, length);
        toast.show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        //get all neccessary views
        EditText etReminderTitle = findViewById(R.id.etReminderTitle);
        TimePicker timePicker = findViewById(R.id.timePicker);
        EditText etInterval = findViewById(R.id.etInterval);
        RadioGroup rgReminderType = findViewById(R.id.rgReminderType);

        //get values from the edit texts
        String reminderText = etReminderTitle.getText().toString();
        int reminderInterval = Integer.valueOf(etInterval.getText().toString());

        //get the minute and hour from the time picker
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        //get the id of the checked radio button
        int radioID = rgReminderType.getCheckedRadioButtonId();

        //add values to out state
        outState.putString(REMINDER_TEXT_KEY,reminderText);
        outState.putInt(REMINDER_HOUR_KEY, hour);
        outState.putInt(REMINDER_MINUTE_KEY, minute);
        outState.putInt(REMINDER_INTERVAL_KEY, reminderInterval);
        outState.putInt(REMINDER_INTERVAL_ID_KEY, radioID);

    }

    public void updateLayout(String reminderText, int hour, int minute, int interval, int intervalID) {
        //get all neccessary views
        EditText etReminderTitle = findViewById(R.id.etReminderTitle);
        TimePicker timePicker = findViewById(R.id.timePicker);
        EditText etInterval = findViewById(R.id.etInterval);
        RadioGroup rgReminderType = findViewById(R.id.rgReminderType);

        //set the edit text with the relevant values
        etReminderTitle.setText(reminderText);
        etInterval.setText(String.valueOf(interval));

        //set the time picker hours and minutes
        timePicker.setHour(hour);
        timePicker.setMinute(minute);

        //cehck the radio group button
        rgReminderType.check(intervalID);
    }
}