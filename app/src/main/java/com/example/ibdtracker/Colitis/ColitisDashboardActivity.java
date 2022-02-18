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
import android.widget.Toast;

import com.example.ibdtracker.AI.RuleBasedSystem;
import com.example.ibdtracker.AppSettingsActivity;
import com.example.ibdtracker.Data.Colitis.ColitisResponseRepository;
import com.example.ibdtracker.Data.Colitis.ColitisSurveyResponse;
import com.example.ibdtracker.Data.Crohns.CrohnsResponseRepository;
import com.example.ibdtracker.Data.Crohns.CrohnsSurveyResponse;
import com.example.ibdtracker.MainActivity;
import com.example.ibdtracker.Misc.MiscActivity;
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

public class ColitisDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPreferences; //the users saved preferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide app name bar
        setContentView(R.layout.activity_colitis_dashboard);

        //Get the shared preferences file
        sharedPreferences = getSharedPreferences(SelectorActivity.SHARED_PREF_FILE, MODE_PRIVATE);

        //add on click to all the buttons
        Button btnPredict = findViewById(R.id.btnPredictColitis);
        btnPredict.setOnClickListener(this);

        Button btnWeightGraph = findViewById(R.id.btnWeightGraph);
        btnWeightGraph.setOnClickListener(this);

        Button btnWellbeingGraph = findViewById(R.id.btnWellbeingGraph);
        btnWellbeingGraph.setOnClickListener(this);

        Button btnDayGraph = findViewById(R.id.btnDayGraph);
        btnDayGraph.setOnClickListener(this);

        Button btnNightGraph = findViewById(R.id.btnNightGraph);
        btnNightGraph.setOnClickListener(this);

        Button btnBloodGraph = findViewById(R.id.btnBloodGraph);
        btnBloodGraph.setOnClickListener(this);

        //get a list of all responses
        List<ColitisSurveyResponse> responses = ColitisResponseRepository.getRepository(getApplicationContext()).getAllResponsesSorted();

        //create arrays/lists for the neccessary data points
        List<Float> weights = new ArrayList<Float>();
        String[] dates = getDates();

        //for every response
        for(ColitisSurveyResponse r : responses) {
            weights.add(r.getWeight());
        }

        //get the graph view and set the scroll container
        GraphView graphColitis = findViewById(R.id.graphColitis);
        graphColitis.setScrollContainer(true);

        //create a label formatter for the graph
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphColitis);

        //create a series of data points
        LineGraphSeries<DataPoint> series = floatToSeries(weights);

        //if there are not at least 3 data points
        if(dates.length < 2) {
            showDataError();
        }
        //othwerwise
        else {
            staticLabelsFormatter.setHorizontalLabels(dates); //set the horizontal labels to the dates
            series.setAnimated(true); //set the graph to be animated
            graphColitis.addSeries(series); //add the data points
            graphColitis.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter); //render the new labels
            graphColitis.getGridLabelRenderer().setHorizontalLabelsAngle(105); //rotate labels
            graphColitis.getGridLabelRenderer().setHorizontalAxisTitle("Date"); //set axis titles
            graphColitis.getGridLabelRenderer().setVerticalAxisTitle("Weight");
            graphColitis.getGridLabelRenderer().setNumHorizontalLabels(7); //set max number of labels to avoid clustering
            graphColitis.getGridLabelRenderer().setPadding(50); // add some padding
        }

        //make the graph scrollable and scalable
        graphColitis.getViewport().setScalable(true);
        graphColitis.getViewport().setScrollable(true);

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

    @Override
    public void onClick(View view) {
        //get every response
        List<ColitisSurveyResponse> responses = ColitisResponseRepository.getRepository(getApplicationContext()).getAllResponsesSorted();

        //if the predict button was pressed
        if(view.getId() == R.id.btnPredictColitis) {
            //get the text view
            TextView tvPrediction = findViewById(R.id.tvPrediction);

            //create the rule based system
            RuleBasedSystem rbs = new RuleBasedSystem(sharedPreferences.getString(MainActivity.IBD_TYPE_KEY, ""));

            //make the prediction
            String prediction = rbs.predictStatus(responses);

            //update the text view
            tvPrediction.setText(prediction);
        }

        //if the weight button was pressed
        else if(view.getId() == R.id.btnWeightGraph) {
            //update the heading
            updateHeader(getResources().getString(R.string.dashboard_weight));

            //get the graph view and reset it
            GraphView graphColitis = findViewById(R.id.graphColitis);
            graphColitis.removeAllSeries();

            //create arrays/lists for the neccessary data points
            List<Float> weights = new ArrayList<Float>();
            String[] dates = getDates();

            //for every response
            for(ColitisSurveyResponse r : responses) {
                weights.add(r.getWeight());
            }

            //create a label formatter for the graph
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphColitis);

            //create a series of data points
            LineGraphSeries<DataPoint> series = floatToSeries(weights);

            //if there are not at least 3 data points
            if(dates.length < 2) {
                showDataError();
            }
            //othwerwise
            else {
                staticLabelsFormatter.setHorizontalLabels(dates); //set the horizontal labels to the dates
                series.setAnimated(true); //set the graph to be animated
                graphColitis.addSeries(series); //add the data points
                graphColitis.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter); //render the new labels
                graphColitis.getGridLabelRenderer().setHorizontalLabelsAngle(105); //rotate labels
                graphColitis.getGridLabelRenderer().setHorizontalAxisTitle("Date"); //set axis titles
                graphColitis.getGridLabelRenderer().setVerticalAxisTitle("Weight");
                graphColitis.getGridLabelRenderer().setNumHorizontalLabels(7); //set max number of labels to avoid clustering
                graphColitis.getGridLabelRenderer().setPadding(50); // add some padding
            }
        }

        //if the wellbeing button was pressed
        else if(view.getId() == R.id.btnWellbeingGraph) {
            //update the header
            updateHeader(getResources().getString(R.string.dashboard_crohns_wellbeing));

            //get the graph view and reset it
            GraphView graphColitis = findViewById(R.id.graphColitis);
            graphColitis.removeAllSeries();

            //create arrays/lists for the neccessary data points
            List<Integer> wellbeingRatings = new ArrayList<Integer>();
            String[] dates = getDates();

            //for every response
            for(ColitisSurveyResponse r : responses) {
                wellbeingRatings.add(r.getColitisQ4());
            }

            //create a series of datapoints from the list of ints
            LineGraphSeries<DataPoint> series = intToSeries(wellbeingRatings);

            //create a label formatter for the graph
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphColitis);

            //if there are not at least 3 data points
            if(dates.length < 2) {
                showDataError();
            }
            //othwerwise
            else {
                staticLabelsFormatter.setHorizontalLabels(dates); //set the horizontal labels to the dates
                series.setAnimated(true); //set the graph to be animated
                graphColitis.addSeries(series); //add the data points
                graphColitis.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter); //render the new labels
                graphColitis.getGridLabelRenderer().setHorizontalLabelsAngle(105); //rotate labels
                graphColitis.getGridLabelRenderer().setHorizontalAxisTitle("Date"); //set axis titles
                graphColitis.getGridLabelRenderer().setVerticalAxisTitle("Well-being");
                graphColitis.getGridLabelRenderer().setNumHorizontalLabels(7); //set max number of labels to avoid clustering
                graphColitis.getGridLabelRenderer().setPadding(50); // add some padding
            }
        }

        //if the day button was pressed
        else if(view.getId() == R.id.btnDayGraph) {
            //update the header
            updateHeader(getResources().getString(R.string.dashboard_bowel_day));

            //get the graph view and reset it
            GraphView graphColitis = findViewById(R.id.graphColitis);
            graphColitis.removeAllSeries();

            //create arrays/lists for the neccessary data points
            List<Integer> dayStools = new ArrayList<Integer>();
            String[] dates = getDates();

            //for every response
            for(ColitisSurveyResponse r : responses) {
                dayStools.add(r.getColitisQ1());
            }

            //create a series of datapoints from the list of ints
            LineGraphSeries<DataPoint> series = intToSeries(dayStools);

            //create a label formatter for the graph
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphColitis);

            //if there are not at least 3 data points
            if(dates.length < 2) {
                showDataError();
            }
            //othwerwise
            else {
                staticLabelsFormatter.setHorizontalLabels(dates); //set the horizontal labels to the dates
                series.setAnimated(true); //set the graph to be animated
                graphColitis.addSeries(series); //add the data points
                graphColitis.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter); //render the new labels
                graphColitis.getGridLabelRenderer().setHorizontalLabelsAngle(105); //rotate labels
                graphColitis.getGridLabelRenderer().setHorizontalAxisTitle("Date"); //set axis titles
                graphColitis.getGridLabelRenderer().setVerticalAxisTitle("Movements during day");
                graphColitis.getGridLabelRenderer().setNumHorizontalLabels(7); //set max number of labels to avoid clustering
                graphColitis.getGridLabelRenderer().setPadding(50); // add some padding
            }
        }

        //if the night button was pressed
        else if(view.getId() == R.id.btnNightGraph) {
            //update the header
            updateHeader(getResources().getString(R.string.dashboard_bowel_night));

            //get the graph view and reset it
            GraphView graphColitis = findViewById(R.id.graphColitis);
            graphColitis.removeAllSeries();

            //create arrays/lists for the neccessary data points
            List<Integer> nightStools = new ArrayList<Integer>();
            String[] dates = getDates();

            //for every response
            for(ColitisSurveyResponse r : responses) {
                nightStools.add(r.getColitisQ2());
            }

            //create a series of datapoints from the list of ints
            LineGraphSeries<DataPoint> series = intToSeries(nightStools);

            //create a label formatter for the graph
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphColitis);

            //if there are not at least 3 data points
            if(dates.length < 2) {
                showDataError();
            }
            //othwerwise
            else {
                staticLabelsFormatter.setHorizontalLabels(dates); //set the horizontal labels to the dates
                series.setAnimated(true); //set the graph to be animated
                graphColitis.addSeries(series); //add the data points
                graphColitis.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter); //render the new labels
                graphColitis.getGridLabelRenderer().setHorizontalLabelsAngle(105); //rotate labels
                graphColitis.getGridLabelRenderer().setHorizontalAxisTitle("Date"); //set axis titles
                graphColitis.getGridLabelRenderer().setVerticalAxisTitle("Movements during night");
                graphColitis.getGridLabelRenderer().setNumHorizontalLabels(7); //set max number of labels to avoid clustering
                graphColitis.getGridLabelRenderer().setPadding(50); // add some padding
            }
        }

        //if the blood button was pressed
        if(view.getId() == R.id.btnBloodGraph) {
            //update the header
            updateHeader(getResources().getString(R.string.dashboard_blood));

            //get the graph view and reset it
            GraphView graphColitis = findViewById(R.id.graphColitis);
            graphColitis.removeAllSeries();

            //create arrays/lists for the neccessary data points
            List<Float> bloodLevels = new ArrayList<Float>();
            String[] bloodLabels = {"None", "Trace", "<50%", ">50%"};
            String[] dates = getDates();

            //for every response
            for(ColitisSurveyResponse r : responses) {
                bloodLevels.add(r.getColitisQ6());
            }

            //create a series from the list of floats
            LineGraphSeries<DataPoint> series = floatToSeries(bloodLevels);

            //create a label formatter for the graph
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphColitis);

            //if there are not at least 3 data points
            if(dates.length < 2) {
                showDataError();
            }
            //othwerwise
            else {
                staticLabelsFormatter.setHorizontalLabels(dates); //set the horizontal labels to the dates
                staticLabelsFormatter.setVerticalLabels(bloodLabels); //set the vertical labels
                series.setAnimated(true); //set the graph to be animated
                graphColitis.addSeries(series); //add the data points
                graphColitis.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter); //render the new labels
                graphColitis.getGridLabelRenderer().setHorizontalLabelsAngle(105); //rotate labels
                graphColitis.getGridLabelRenderer().setHorizontalAxisTitle("Date"); //set axis titles
                graphColitis.getGridLabelRenderer().setVerticalAxisTitle("Blood presence");
                graphColitis.getGridLabelRenderer().setNumHorizontalLabels(7); //set max number of labels to avoid clustering
                graphColitis.getGridLabelRenderer().setPadding(50); // add some padding
            }
        }
    }

    /**
     * A method to get an array of all dates sorted
     * @return the array of dates
     */
    public String[] getDates() {
        //get the list of all survey responses sorted by date
        List<ColitisSurveyResponse> responseList = ColitisResponseRepository.getRepository(getApplicationContext()).getAllResponsesSorted();

        //create an array to store all the dates
        String[] dates = new String[responseList.size()];

        //create a loop index
        int index = 0;

        //for every response
        for(ColitisSurveyResponse r : responseList) {
            dates[index] = r.getDate().substring(5,r.getDate().length()); //get the date and add it to the list
            index++; //increase the index
        }
        //return the dates
        return dates;
    }

    /**
     * A method to convert a list of floats into a series of data points
     * @param list the list of floats to convert into data points
     * @return the series of data points
     */
    public LineGraphSeries<DataPoint> floatToSeries (List<Float> list) {
        //create a datapoint series
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        //create a loop index
        int index = 0;

        //for every float in the array
        for(Float f : list) {
            DataPoint d = new DataPoint(index, f.doubleValue()); //add the value as a datapoint
            series.appendData(d, true, list.size()); //add the datapoint to the series

            index++; //increase the index
        }
        //return the series
        return series;
    }

    /**
     * A method to convert a list of ints into a series of data points
     * @param list the list of ints to convert into data points
     * @return the series of data points
     */
    public LineGraphSeries<DataPoint> intToSeries(List<Integer> list) {
        //create a series of data points from the arrays
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        //create a lop index
        int index = 0;
        //for every integer in the array
        for(Integer i : list) {
            DataPoint d = new DataPoint(index, i); //add the value as a datapoint
            series.appendData(d, true, list.size()); //add the datapoint to the series

            index++; //increase the index
        }
        return series;
    }

    /**
     * A method to update the graph header using a string
     * @param text the string to change the header to
     */
    public void updateHeader(String text) {
        TextView tvGraphHeader = findViewById(R.id.tvGraphHeader);
        tvGraphHeader.setText(text);
    }

    /**
     * A method to display a warning toast to the user
     */
    public void showDataError() {
        //create and display a toast
        Toast warning = Toast.makeText(getApplicationContext(), "There must be 3 days of data in order to plot the charts.", Toast.LENGTH_LONG);
        warning.show();
    }
}