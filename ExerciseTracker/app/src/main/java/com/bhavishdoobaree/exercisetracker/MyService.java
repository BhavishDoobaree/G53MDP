package com.bhavishdoobaree.exercisetracker;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class MyService extends Service implements LocationListener {



    private final Binder mBind = new mBinder();


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBind;
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //init all components
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        MyService locationListener = new MyService();


        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 5, locationListener);
        } catch (SecurityException e) {
        }
        return START_STICKY;
    }

    //when location change is detected
    @Override
    public void onLocationChanged(Location location) {
        //broadcast new location
        Intent i = new Intent("LBR");
        i.putExtra("loc", location);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public class mBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }
}
