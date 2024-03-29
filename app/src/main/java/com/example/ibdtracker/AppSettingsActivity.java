package com.example.ibdtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ibdtracker.Colitis.ColitisDashboardActivity;
import com.example.ibdtracker.Colitis.ColitisSurveyActivity;
import com.example.ibdtracker.Crohns.CrohnsDashboardActivity;
import com.example.ibdtracker.Crohns.CrohnsSurveyActivity;
import com.example.ibdtracker.Data.Colitis.ColitisResponseRepository;
import com.example.ibdtracker.Data.Crohns.CrohnsResponseRepository;
import com.example.ibdtracker.Data.Crohns.CrohnsSurveyResponse;
import com.example.ibdtracker.Data.Reminder.IBDReminder;
import com.example.ibdtracker.Data.Reminder.ReminderRepository;
import com.example.ibdtracker.Misc.AlarmReceiver;
import com.example.ibdtracker.Misc.MiscActivity;
import com.example.ibdtracker.Misc.ReminderRecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

public class AppSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPreferences; //the users saved preferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide app name bar
        setContentView(R.layout.activity_app_settings);

        //Get the shared preferences file
        sharedPreferences = getSharedPreferences(SelectorActivity.SHARED_PREF_FILE, MODE_PRIVATE);

        //update the current weight text view
        TextView tvCurrentWeight = findViewById(R.id.tvCurrentWeight);
        tvCurrentWeight.setText("Current weight stored is: " + String.valueOf(sharedPreferences.getFloat(MainActivity.TYPICAL_WEIGHT_KEY, 0)));

        //add on click listeners to the buttons
        Button btnUpdateWeight = findViewById(R.id.btnUpdateWeight);
        btnUpdateWeight.setOnClickListener(this);

        Button btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this);

        //get the IBD type stored in shared preferences
        String ibdType = sharedPreferences.getString(MainActivity.IBD_TYPE_KEY, "");

        //Bottom navigation bar set up
        BottomNavigationView bottomNav = findViewById(R.id.bnvNavigation); //initialising and assigning the bottomNav variable
        bottomNav.setSelectedItemId(R.id.navSettings); //setting the selected item in the nav bar

        //set the click listeners for each of the nav bar items
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //switch for each of the menu options
                switch(item.getItemId()) {
                    //misc page
                    case R.id.navSettings:
                        return true; //do nothing
                    //dashboard page
                    case R.id.navDashboard:
                        //if crohns is being tracked
                        // finish current activity
                        if(ibdType.equals("Crohns")) {
                            startActivity(new Intent(getApplicationContext(), CrohnsDashboardActivity.class)); //start crohns dashboard activity
                        }
                        //otherwise it is colitis
                        else{
                            startActivity(new Intent(getApplicationContext(), ColitisDashboardActivity.class)); //start crohns dashboard activity
                        }
                        finish(); // finish current activity
                        return true;

                    //survey pages
                    case R.id.navSurvey:
                        //if crohns is being tracked
                        // finish current activity
                        if(ibdType.equals("Crohns")) {
                            startActivity(new Intent(getApplicationContext(), CrohnsSurveyActivity.class)); //start crohns survey activity
                        }
                        //otherwise it is colitis
                        else{
                            startActivity(new Intent(getApplicationContext(), ColitisSurveyActivity.class)); //start crohns survey activity
                        }
                        finish(); // finish current activity
                        return true;
                    //settings page
                    case R.id.navMisc:
                        startActivity(new Intent(getApplicationContext(), MiscActivity.class)); //start misc activity
                        finish(); //finish the activity
                        return true;
                }
                return false; //if not in switch statement something has gone very wrong
            }
        });

    }

    @Override
    public void onClick(View view) {
        //if the button pressed was update weight button
        if(view.getId() == R.id.btnUpdateWeight) {
            //get the view of the edit text
            EditText numberWeightUpdate = findViewById(R.id.numberWeightUpdate);

            //check if the value entered is not null
            String weightString = String.valueOf(numberWeightUpdate.getText());

            //if the string is invalid, display error
            if(weightString == null || weightString == "" || weightString.isEmpty()) {
                Toast toast = Toast.makeText(this.getApplicationContext(), "Please enter a number", Toast.LENGTH_SHORT);
                toast.show();
            }
            //otherwise
            else {
                float newWeight = Float.parseFloat(weightString); //parse string to double

                //if the number is not valid, display message to user
                if (newWeight <= 0) {
                    Toast toast = Toast.makeText(this.getApplicationContext(), "Please enter a valid number", Toast.LENGTH_SHORT);
                    toast.show();
                }
                //otherwise
                else {
                    //get the SharedPreference editors and add the values
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putFloat(MainActivity.TYPICAL_WEIGHT_KEY, newWeight);

                    //apply the changes
                    editor.apply();

                    //update the current weight text view
                    TextView tvCurrentWeight = findViewById(R.id.tvCurrentWeight);
                    tvCurrentWeight.setText("Current weight stored is: " + String.valueOf(sharedPreferences.getFloat(MainActivity.TYPICAL_WEIGHT_KEY, 0)));
                }
            }
        }

        //if the button pressed was the reset button
        if(view.getId() == R.id.btnReset) {
            //create an alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(AppSettingsActivity.this);

            //add the messages to the dialog
            builder.setMessage(R.string.settings_dialog_message)
                    //set the postive button string and on click
                    .setPositiveButton(R.string.settings_dialog_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //reset the values in shared preferences, clear the databases
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString(SelectorActivity.IBD_TYPE_KEY, "");
                            editor.putFloat(MainActivity.TYPICAL_WEIGHT_KEY, 0);
                            editor.apply();

                            //get a list of all reminders
                            List<IBDReminder> reminders = ReminderRepository.getRepository(getApplicationContext()).getAllReminders();

                            //for every reminder
                            for(IBDReminder reminder : reminders) {
                                //create a new calendar object
                                Calendar calendar = Calendar.getInstance();

                                //set the time of the calendar to the reminder time - reminder will start at this time
                                calendar.set(Calendar.HOUR_OF_DAY, reminder.getReminderHour());
                                calendar.set(Calendar.MINUTE, reminder.getReminderMinute());

                                //create a new alarm reciever intent
                                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);

                                //put the id and reminder text as extras
                                intent.putExtra(ReminderRecyclerViewAdapter.EXTRA_REMINDER_ID, reminder.getId());
                                intent.putExtra(ReminderRecyclerViewAdapter.EXTRA_REMINDER_TEXT, reminder.getReminderText());

                                //create a pending indent from the previous intent - flag immutable as per documentation
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), reminder.getId(), intent, PendingIntent.FLAG_IMMUTABLE);

                                //create an alarm manager to handle repeating alarms
                                AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);

                                //cancel alarm
                                alarmManager.cancel(pendingIntent);
                            }

                            //clear the databases
                            CrohnsResponseRepository.getRepository(getApplicationContext()).deleteAll();
                            ColitisResponseRepository.getRepository(getApplicationContext()).deleteAll();
                            ReminderRepository.getRepository(getApplicationContext()).deleteAll();

                            //close the app
                            finishAffinity();
                        }
                    })
                    //set the negative button string, null will dismiss the dialog on click
                    .setNegativeButton(R.string.settings_dialog_cancel, null);

            //finish creation, show the alert
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}