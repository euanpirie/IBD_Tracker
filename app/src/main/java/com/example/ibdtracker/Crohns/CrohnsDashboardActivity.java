package com.example.ibdtracker.Crohns;

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
import com.example.ibdtracker.Data.Crohns.CrohnsResponseRepository;
import com.example.ibdtracker.Data.Crohns.CrohnsSurveyResponse;
import com.example.ibdtracker.MainActivity;
import com.example.ibdtracker.R;
import com.example.ibdtracker.SelectorActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CrohnsDashboardActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences sharedPreferences; //the users saved preferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide app name bar
        setContentView(R.layout.activity_crohns_dashboard);

        //Get the shared preferences file
        sharedPreferences = getSharedPreferences(SelectorActivity.SHARED_PREF_FILE, MODE_PRIVATE);

        //set on click listener for the buttons
        Button btnPredictCrohns = findViewById(R.id.btnPredictCrohns);
        btnPredictCrohns.setOnClickListener(this);

        //get the list of all survey responses
        List<CrohnsSurveyResponse> responseList = CrohnsResponseRepository.getRepository(getApplicationContext()).getAllResponsesSorted();

        List<Float> weights = new ArrayList<Float>();
        String[] dates = new String[responseList.size()];
        int i = 0;
        for(CrohnsSurveyResponse r : responseList) {
            weights.add(r.getWeight());
            dates[i] = r.getDate();
            i++;
        }

        GraphView graph = (GraphView) findViewById(R.id.graphTest);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1.5),
                new DataPoint(1, 5.5),
                new DataPoint(2, 3.5),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });

        graph.setScrollContainer(true);





        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);

        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>();
        int index = 0;
        for(Float f : weights) {
            DataPoint d = new DataPoint(index, f.doubleValue());
            series2.appendData(d, true, weights.size());
            index++;
        }

        if(!(dates.length < 2)) {
            staticLabelsFormatter.setHorizontalLabels(dates);
            series2.setAnimated(true);
            graph.addSeries(series2);
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);


        }
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);

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
                        finish();
                        return true;
                    //dashboard page
                    case R.id.navDashboard:
                        return true; //do nothing
                    //survey page
                    case R.id.navSurvey:
                        startActivity(new Intent(getApplicationContext(), CrohnsSurveyActivity.class)); //start survey activity
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
        if(view.getId() == R.id.btnPredictCrohns) {
            //get the text view
            TextView tvPrediction = findViewById(R.id.tvPrediction);

            //get the response
            List<CrohnsSurveyResponse> responses = CrohnsResponseRepository.getRepository(getApplicationContext()).getAllResponsesSorted();

            //create the rule based system
            RuleBasedSystem rbs = new RuleBasedSystem(sharedPreferences.getString(MainActivity.IBD_TYPE_KEY, ""));

            //make the prediction
            String prediction = rbs.predictStatus(responses);

            //update the text view
            tvPrediction.setText(prediction);
        }
    }
}