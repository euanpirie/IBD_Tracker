package com.example.ibdtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WeightEntryActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences sharedPreferences; //the users saved preferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide app name bar
        setContentView(R.layout.activity_weight_entry);

        //Get the shared preferences file
        sharedPreferences = getSharedPreferences(SelectorActivity.SHARED_PREF_FILE, MODE_PRIVATE);

        //add on click listener to the save button
        Button btn = findViewById(R.id.btnSaveWeight);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSaveWeight) {

            String ibdType = sharedPreferences.getString(SelectorActivity.IBD_TYPE_KEY, ""); //get the ibd type stored in SP

            //get whatever has been entered into the number field
            EditText numberWeight = findViewById(R.id.numberWeight);

            //check if the value entered is not null
            String weightString = String.valueOf(numberWeight.getText());

            if(weightString == null || weightString == "" || weightString.isEmpty()) {
                Toast toast = Toast.makeText(this.getApplicationContext(), "Please enter a number", Toast.LENGTH_SHORT);
                toast.show();
            }
           else {
                float typicalWeight = Float.parseFloat(numberWeight.getText().toString()); //parse string to double

                //if the number is not valid, display message to user
                if (typicalWeight <= 0) {
                    Toast toast = Toast.makeText(this.getApplicationContext(), "Please enter a valid number", Toast.LENGTH_SHORT);
                    toast.show();
                }
                //otherwise
                else {
                    //get the SharedPreference editors and add the values
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putFloat(MainActivity.TYPICAL_WEIGHT_KEY, typicalWeight);

                    //apply the changes
                    editor.apply();

                    //if crohns is being tracked
                    if (ibdType.equals("Crohns")) {
                        startActivity(new Intent(getApplicationContext(), CrohnsDashboardActivity.class)); //start crohns dashboard activity
                        finish(); // finish current activity
                    }
                    //otherwise it is colitis
                    else {
                        startActivity(new Intent(getApplicationContext(), ColitisDashboardActivity.class)); //start crohns dashboard activity
                        finish(); // finish current activity
                    }
                }
            }
        }
    }
}