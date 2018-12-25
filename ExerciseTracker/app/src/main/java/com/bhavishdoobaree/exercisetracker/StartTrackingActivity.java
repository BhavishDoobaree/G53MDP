package com.bhavishdoobaree.exercisetracker;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StartTrackingActivity extends AppCompatActivity implements OnMapReadyCallback {

    //actions buttons and stats display
    TextView distance_display, time_display;
    Button bStart, bStop, bPause;

    //services and other variables
    MyService myService;
    Boolean isBound;
    Boolean trackUser, boolStatus = false;
    String date;
    private NotificationManager notificationManager;

    //maps display variables
    Location location, sLocation;
    SupportMapFragment supportMapFragment;
    GoogleMap googMap;
    Intent intent;
    Context contxt = this;

    //time recorders
    float dist, total_dist = (float) 0.00;
    long milliSecs, startTime, incTime, timeTemp = 0L;
    int seconds, minutes, milliSeconds;
    Handler handler;

    //runnable to update timings
    public Runnable runnableTime = new Runnable() {

        public void run() {
            milliSecs = SystemClock.uptimeMillis() - startTime; //update time
            incTime = timeTemp + milliSecs; //update total time
            seconds = (int) (incTime / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;
            milliSeconds = (int) (incTime % 1000);

            //update UI with time
            time_display.setText("" + minutes + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%03d", milliSeconds));

            handler.postDelayed(this, 0);
        }

    };

    //ServiceConnection for service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.mBinder binder = (MyService.mBinder) service;
            myService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_tracking);

        //handler and UI components initialise
        handler = new Handler();
        bStart = findViewById(R.id.startExe);
        bStop = findViewById(R.id.stopExe);
        bPause = findViewById(R.id.pauseExe);
        distance_display = findViewById(R.id.distanceDisplay);
        time_display = findViewById(R.id.timeDisplay);

        //assign notification manger
        assignNotifManager();

        //show only start for UI purposes and single option
        bStart.setVisibility(View.VISIBLE);
        bStop.setVisibility(View.INVISIBLE);
        bPause.setVisibility(View.INVISIBLE);


    }

    @Override
    protected void onStart(){
        super.onStart();

        intent = new Intent(this, MyService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        //check for location

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapsFrag);
        supportMapFragment.getMapAsync(this);

        //broadcast when location is updated
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        location = intent.getExtras().getParcelable("loc");
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        updateCamera(latLng, 20f);
                        try {
                            //UI update on tracking
                            if (trackUser) {
                                dist = sLocation.distanceTo(location);
                                sLocation = location;
                                total_dist = dist + total_dist;
                                String distance = String.format("%.2f", total_dist);
                                distance_display.setText(distance + "m");
                            }
                        } catch (Exception e) {

                        }
                    }
                    }
                    , new IntentFilter("LBR"));
        startService(intent);



    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //setup googMaps
        googMap = googleMap;
        googMap.setMyLocationEnabled(true);
        googMap.getUiSettings().setZoomControlsEnabled(true);
        stopService(intent);
    }

    //Maps views change
    public void updateCamera(LatLng latLng, float zoom)
    {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        googMap.animateCamera(cameraUpdate);
    }

    //sending notifs
    public void sendNotification(){
        Intent intent = getIntent();
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel("exerciseTracker", "ExerciseTracker", notificationManager.IMPORTANCE_LOW);
//            notificationManager.createNotificationChannel(notificationChannel);
//        }

        Notification notif = new NotificationCompat.Builder(this, "exerciseTracker")
                .setContentTitle("ExerciseTracker")
                .setContentText(trackUser ? "Exercise" : "Paused")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();

        notificationManager.notify(1, notif);
    }

    public void assignNotifManager()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            notificationManager = getSystemService(NotificationManager.class);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        handler.removeCallbacks(runnableTime);
        stopService(intent);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        moveTaskToBack(true);
    }


    //reset interface after stop button pressed
    public void resetUIAfterStop()
    {
        bStop.setVisibility(View.INVISIBLE);
        bPause.setVisibility(View.INVISIBLE);
        bStart.setVisibility(View.VISIBLE);

        notificationManager.cancel(1);

        time_display.setText("");
        distance_display.setText("");

        trackUser = false;
        boolStatus = false;

        total_dist = (float) 0.00;
        seconds = 0;
        minutes = 0;
        milliSeconds = 0;
        milliSecs = 0L;
        startTime = 0L;
        incTime = 0L;
        timeTemp = 0L;


    }

    //functionality for play button
    public void onClickPlay(View view)
    {
        startTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnableTime, 0);
        trackUser =  true;

        //set visibility for UI
        bStart.setVisibility(View.INVISIBLE);
        bStop.setVisibility(View.VISIBLE);
        bPause.setVisibility(View.VISIBLE);

        //if not in PAUSE mode
        if (!boolStatus) {
            SimpleDateFormat SDF = new SimpleDateFormat("HH:mm dd-MMM-yyyy");
            date = SDF.format(new Date());
            distance_display.setText("0.0m");
            boolStatus = true;
        }
        //send notifs and location update
        //sendNotification();
        sLocation = location;
    }

    //pause button functionality
    public void onClickPause(View view)
    {
        timeTemp += milliSecs; //save time in temp
        handler.removeCallbacks(runnableTime); //stop runnable

        trackUser = false; //stop tracking

        //sendNotification(); //update on notif

        //UI
        bStart.setVisibility(View.VISIBLE);
        bPause.setVisibility(View.INVISIBLE);
    }

    //Stop functionality
    public void onClickStop(View view)
    {
        handler.removeCallbacks(runnableTime);
        myDBHandler dbHandler =  new myDBHandler(getBaseContext(), null, null, 1);
        exeLog exelog = new exeLog(date, distance_display.getText().toString(), incTime);
        dbHandler.addLogRecord(exelog);
        //add new log to DB

        //inform that new record added
        new AlertDialog.Builder(contxt)
                .setTitle("New Time")
                .setMessage("New record added")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create()
                .show();

        resetUIAfterStop();
    }




}
