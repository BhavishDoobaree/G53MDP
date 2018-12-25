package com.bhavishdoobaree.exercisetracker;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    //buttons used
    Button startTrackingButton, viewHistoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //define UI components
        startTrackingButton = findViewById(R.id.start_tracking_activity);
        viewHistoryButton = findViewById(R.id.view_history_activity);


        //change activity using buttons
        startTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Intent i = new Intent(getApplicationContext(), StartTrackingActivity.class);
                startActivity(i);
            }
        });

        viewHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                Intent i = new Intent(getApplicationContext(), ViewPastListActivity.class);
                startActivity(i);
            }
        });

        locationCheck();



    }

    //making sure user allows for permission fo location
    public boolean locationCheck() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
            return false;
        } else {

            return true;
        }
    }




}
