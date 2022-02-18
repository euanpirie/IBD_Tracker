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
import android.widget.Toast;

import com.example.ibdtracker.AI.RuleBasedSystem;
import com.example.ibdtracker.AppSettingsActivity;
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
import com.jjoe64.graphview.series.Series;

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

        Button btnWeightGraph = findViewById(R.id.btnWeightGraph);
        btnWeightGraph.setOnClickListener(this);

        Button btnStoolGraph = findViewById(R.id.btnStoolsGraph);
        btnStoolGraph.setOnClickListener(this);

        Button btnPainGraph = findViewById(R.id.btnPainGraph);
        btnPainGraph.setOnClickListener(this);

        Button btnWellbeingGraph = findViewById(R.id.btnWellbeingGraph);
        btnWellbeingGraph.setOnClickListener(this);

        //get the list of all survey responses sorted by date
        List<CrohnsSurveyResponse> responseList = CrohnsResponseRepository.getRepository(getApplicationContext()).getAllResponsesSorted();

        //create arrays/lists for the neccessary data points
        List<Float> weights = new ArrayList<Float>();
        String[] dates = getDates();

        //for every response
        for(CrohnsSurveyResponse r : responseList) {
            //get the necessary details and add to the correct array
            weights.add(r.getWeight());
        }

        //get the graph view and set the scroll container
        GraphView graphCrohns = findViewById(R.id.graphCrohns);
        graphCrohns.setScrollContainer(true);

        //create a label formatter for the graph
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphCrohns);

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
            graphCrohns.addSeries(series); //add the data points
            graphCrohns.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter); //render the new labels
            graphCrohns.getGridLabelRenderer().setHorizontalLabelsAngle(105); //rotate labels
            graphCrohns.getGridLabelRenderer().setHorizontalAxisTitle("Date"); //set axis titles
            graphCrohns.getGridLabelRenderer().setVerticalAxisTitle("Weight");
            graphCrohns.getGridLabelRenderer().setNumHorizontalLabels(7); //set max number of labels to avoid clustering
            graphCrohns.getGridLabelRenderer().setPadding(50); // add some padding
        }

        //make the graph scrollable and scalable
        graphCrohns.getViewport().setScalable(true);
        graphCrohns.getViewport().setScrollable(true);

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
        //get all responses sorted by date
        List<CrohnsSurveyResponse> responses = CrohnsResponseRepository.getRepository(getApplicationContext()).getAllResponsesSorted();

        //if the predict button was pressed
        if(view.getId() == R.id.btnPredictCrohns) {
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
            //update the header
            updateHeader(getResources().getString(R.string.dashboard_weight));

            //get the stool graph view and set the scroll container
            GraphView graphCrohns = (GraphView) findViewById(R.id.graphCrohns);
            graphCrohns.setScrollContainer(true);

            //remove the current series
            graphCrohns.removeAllSeries();

            //create arrays/lists for the neccessary data points
            List<Float> weights = new ArrayList<Float>();
            String[] dates = getDates();

            //for every response
            for(CrohnsSurveyResponse r : responses) {
                weights.add(r.getWeight());
            }

            //create a label formatter for the graph
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphCrohns);

            //create a series of data points from the arrays
            LineGraphSeries<DataPoint> series = floatToSeries(weights);

            //if there are not at least 3 data points
            if(dates.length < 2) {
               showDataError();
            }
            //othwerwise
            else {
                staticLabelsFormatter.setHorizontalLabels(dates); //set the horizontal labels to the dates
                series.setAnimated(true); //set the graph to be animated
                graphCrohns.addSeries(series); //add the data points
                graphCrohns.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter); //render the new labels
                graphCrohns.getGridLabelRenderer().setHorizontalLabelsAngle(105); //rotate labels
                graphCrohns.getGridLabelRenderer().setHorizontalAxisTitle("Date"); //set axis titles
                graphCrohns.getGridLabelRenderer().setVerticalAxisTitle("Weight");
                graphCrohns.getGridLabelRenderer().setNumHorizontalLabels(7); //set max number of labels to avoid clustering
            }
        }

        //if the stools button was pressed
        else if(view.getId() == R.id.btnStoolsGraph) {
            //update the header
            updateHeader(getResources().getString(R.string.dashboard_soft_stools));

            //get the stool graph view and set the scroll container
            GraphView graphCrohns = (GraphView) findViewById(R.id.graphCrohns);
            graphCrohns.setScrollContainer(true);

            //remove the current series
            graphCrohns.removeAllSeries();

            //create arrays/lists for the neccessary data points
            List<Integer> stools = new ArrayList<Integer>();
            String[] dates = getDates();

            //for every response
            for(CrohnsSurveyResponse r : responses) {
                stools.add(r.getCrohnsQ1());
            }

            //create a label formatter for the graph
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphCrohns);

            //create a series of data points from the arrays
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

            //create a lop index
            int index = 0;
            //for every float in the array
            for(Integer i : stools) {
                DataPoint d = new DataPoint(index, i); //add the value as a datapoint
                series.appendData(d, true, stools.size()); //add the datapoint to the series

                index++; //increase the index
            }

            //if there are not at least 3 data points
            if(dates.length < 2) {
                showDataError();
            }
            //othwerwise
            else {
                staticLabelsFormatter.setHorizontalLabels(dates); //set the horizontal labels to the dates
                series.setAnimated(true); //set the graph to be animated
                graphCrohns.addSeries(series); //add the data points
                graphCrohns.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter); //render the new labels
                graphCrohns.getGridLabelRenderer().setHorizontalLabelsAngle(105); //rotate labels
                graphCrohns.getGridLabelRenderer().setHorizontalAxisTitle("Date"); //set axis titles
                graphCrohns.getGridLabelRenderer().setVerticalAxisTitle("Soft stools per day");
                graphCrohns.getGridLabelRenderer().setNumHorizontalLabels(7); //set max number of labels to avoid clustering
            }
        }

        //if the pain button was pressed
        else if(view.getId() == R.id.btnPainGraph) {
            //update the heading
            updateHeader(getResources().getString(R.string.dashboard_abdominal_pain));

            //get the stool graph view and set the scroll container
            GraphView graphCrohns = (GraphView) findViewById(R.id.graphCrohns);
            graphCrohns.setScrollContainer(true);

            //remove the current series
            graphCrohns.removeAllSeries();

            //create arrays/lists for the neccessary data points
            List<Float> painRatings = new ArrayList<Float>();
            String[] dates = getDates();
            String[] painLabels = {"None", "Mild", "Moderate", "Severe"};

            //for every response
            for(CrohnsSurveyResponse r : responses) {
                painRatings.add(r.getCrohnsQ3());
            }

            //create a label formatter for the graph
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphCrohns);

            //create a series of data points from the arrays
            LineGraphSeries<DataPoint> series = floatToSeries(painRatings);

            //if there are not at least 3 data points
            if(dates.length < 2) {
                showDataError();
            }
            //otherwise
            else {
                staticLabelsFormatter.setHorizontalLabels(dates); //set the horizontal labels to the dates
                staticLabelsFormatter.setVerticalLabels(painLabels); //set the vertical labels
                series.setAnimated(true); //set the graph to be animated
                graphCrohns.addSeries(series); //add the data points
                graphCrohns.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter); //render the new labels
                graphCrohns.getGridLabelRenderer().setHorizontalLabelsAngle(105); //rotate labels
                graphCrohns.getGridLabelRenderer().setHorizontalAxisTitle("Date"); //set axis titles
                graphCrohns.getGridLabelRenderer().setVerticalAxisTitle("Pain rating");
                graphCrohns.getGridLabelRenderer().setNumHorizontalLabels(7); //set max number of labels to avoid clustering
            }
        }

        //if the general well-being button was pressed
        else if(view.getId() == R.id.btnWellbeingGraph) {
            //update the textview
            updateHeader(getResources().getString(R.string.dashboard_crohns_wellbeing));

            //get the stool graph view and set the scroll container
            GraphView graphCrohns = (GraphView) findViewById(R.id.graphCrohns);
            graphCrohns.setScrollContainer(true);

            //remove the current series
            graphCrohns.removeAllSeries();

            //create arrays/lists for the neccessary data points
            List<Float> wellbeingRatings = new ArrayList<Float>();
            String[] dates = getDates();
            String[] wellbeingLabels = {"Well", "Subpar", "Poor", "Very Poor", "Terrible"};

            //for every response
            for(CrohnsSurveyResponse r : responses) {
                wellbeingRatings.add(r.getCrohnsQ4());
            }

            //create a label formatter for the graph
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphCrohns);

            //create a series of data points from the arrays
            LineGraphSeries<DataPoint> series = floatToSeries(wellbeingRatings);

            //if there are not at least 3 data points
            if(dates.length < 2) {
                showDataError();
            }
            //otherwise
            else {
                staticLabelsFormatter.setHorizontalLabels(dates); //set the horizontal labels to the dates
                staticLabelsFormatter.setVerticalLabels(wellbeingLabels); //set the vertical labels
                series.setAnimated(true); //set the graph to be animated
                graphCrohns.addSeries(series); //add the data points
                graphCrohns.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter); //render the new labels
                graphCrohns.getGridLabelRenderer().setHorizontalLabelsAngle(105); //rotate labels
                graphCrohns.getGridLabelRenderer().setHorizontalAxisTitle("Date"); //set axis titles
                graphCrohns.getGridLabelRenderer().setVerticalAxisTitle("Well-being");
                graphCrohns.getGridLabelRenderer().setNumHorizontalLabels(7); //set max number of labels to avoid clustering
            }
        }
    }

    /**
     * A method to get an array of all dates sorted
     * @return the array of dates
     */
    public String[] getDates() {
        //get the list of all survey responses sorted by date
        List<CrohnsSurveyResponse> responseList = CrohnsResponseRepository.getRepository(getApplicationContext()).getAllResponsesSorted();

        //create an array to store all the dates
        String[] dates = new String[responseList.size()];

        //create a loop index
        int index = 0;

        //for every response
        for(CrohnsSurveyResponse r : responseList) {
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