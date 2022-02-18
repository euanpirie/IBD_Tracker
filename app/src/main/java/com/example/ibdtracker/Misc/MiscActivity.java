package com.example.ibdtracker.Misc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ibdtracker.AppSettingsActivity;
import com.example.ibdtracker.Colitis.ColitisDashboardActivity;
import com.example.ibdtracker.Colitis.ColitisSurveyActivity;
import com.example.ibdtracker.Crohns.CrohnsDashboardActivity;
import com.example.ibdtracker.Crohns.CrohnsSurveyActivity;
import com.example.ibdtracker.Data.Reminder.IBDReminder;
import com.example.ibdtracker.Data.Reminder.ReminderRepository;
import com.example.ibdtracker.MainActivity;
import com.example.ibdtracker.R;
import com.example.ibdtracker.SelectorActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

public class MiscActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences sharedPreferences; //the users saved preferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide the app name bar
        setContentView(R.layout.activity_misc);

        //Get the shared preferences file
        sharedPreferences = getSharedPreferences(SelectorActivity.SHARED_PREF_FILE, MODE_PRIVATE);

        //get the IBD type stored in shared preferences
        String ibdType = sharedPreferences.getString(MainActivity.IBD_TYPE_KEY, "");

        //add on click to the fab
        FloatingActionButton fabAddReminder = findViewById(R.id.fabAddReminder);
        fabAddReminder.setOnClickListener(this);

        //add on click listener to text view
        TextView tvResources = findViewById(R.id.tvResources);
        tvResources.setOnClickListener(this);

        //get a list of all reminder
        List<IBDReminder> reminders = ReminderRepository.getRepository(getApplicationContext()).getAllReminders();

        RecyclerView rvReminders = findViewById(R.id.rvReminders); //get the recycler view
        RecyclerView.Adapter adapter = new ReminderRecyclerViewAdapter(getApplicationContext(), reminders); //create a new adapter for tge list
        rvReminders.setAdapter(adapter); //set the recycler view adapter
        rvReminders.setLayoutManager(new LinearLayoutManager(getApplicationContext())); //set the layout manager of the recycler view

        //Bottom navigation bar set up
        BottomNavigationView bottomNav = findViewById(R.id.bnvNavigation); //initialising and assigning the bottomNav variable
        bottomNav.setSelectedItemId(R.id.navMisc); //setting the selected item in the nav bar

        //set the click listeners for each of the nav bar items
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //switch for each of the menu options
                switch(item.getItemId()) {
                    //misc page
                    case R.id.navMisc:
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
                    case R.id.navSettings:
                        startActivity(new Intent(getApplicationContext(), AppSettingsActivity.class)); //start settings activity
                        finish(); //finish the current activity
                        return true;


                }
                return false; //if not in switch statement something has gone very wrong
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        //get a list of all reminder
        List<IBDReminder> reminders = ReminderRepository.getRepository(getApplicationContext()).getAllReminders();

        //update the adapter
        RecyclerView rvReminders = findViewById(R.id.rvReminders); //get the recycler view
        RecyclerView.Adapter adapter = new ReminderRecyclerViewAdapter(getApplicationContext(), reminders); //create a new adapter for tge list
        rvReminders.setAdapter(adapter); //set the recycler view adapter
        rvReminders.setLayoutManager(new LinearLayoutManager(getApplicationContext())); //set the layout manager of the recycler view
    }

    @Override
    public void onRestart() {
        super.onRestart();

        //get a list of all reminder
        List<IBDReminder> reminders = ReminderRepository.getRepository(getApplicationContext()).getAllReminders();

        //update the adapter
        RecyclerView rvReminders = findViewById(R.id.rvReminders); //get the recycler view
        RecyclerView.Adapter adapter = new ReminderRecyclerViewAdapter(getApplicationContext(), reminders); //create a new adapter for tge list
        rvReminders.setAdapter(adapter); //set the recycler view adapter
        rvReminders.setLayoutManager(new LinearLayoutManager(getApplicationContext())); //set the layout manager of the recycler view
    }

    @Override
    public void onClick(View view) {
        //if the resources text was pressed
        if(view.getId() == R.id.tvResources) {
            //set the uri
            String uri = "https://www.crohnsandcolitis.org.uk";
            //start an activity with action view intent
            startActivity(new Intent(
                    Intent.ACTION_VIEW
            ).setData(Uri.parse(uri))); //set the uri
        }
        //if the fab was pressed
        else if(view.getId() == R.id.fabAddReminder) {
            //start a new add reminder activity
            startActivity(new Intent(getApplicationContext(), AddReminderActivity.class));
        }
    }

    private void createRepeatingAlarm(Context context) {

    }
}