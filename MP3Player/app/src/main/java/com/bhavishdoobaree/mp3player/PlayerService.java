package com.bhavishdoobaree.mp3player;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


//module manages services and notifications

public class PlayerService extends Service {

    private final IBinder PlayerServiceBinder = new playerServiceBinder();
    private static String LOGCAT_TAG = "BoundPlayerService";
    private final static int notifID = 62327846;  //arbitrary number used
    NotificationCompat.Builder notif;
    NotificationManager notifManager;
    private MP3Player mp3Player;

    public PlayerService() {
        mp3Player = new MP3Player();
    }

    //Binder
    public class playerServiceBinder extends Binder {
        PlayerService getService()
        {
            return PlayerService.this;
        }
    }

    public MP3Player getMp3Player()
    {
        return mp3Player;
    }

    public NotificationManager getNotifManager()
    {
        return notifManager;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        notifSetUp();
        notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifManager.notify(notifID, notif.build());
        return PlayerServiceBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.v(LOGCAT_TAG, "onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(LOGCAT_TAG, "onUnbind");
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOGCAT_TAG, "onDestroy");
    }

    private void notifSetUp()
    {
        Intent endIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,endIntent, PendingIntent.FLAG_UPDATE_CURRENT );

        //notification display info
        notif = new NotificationCompat.Builder(this);
        notif.setTicker("MP3Player");
        notif.setContentTitle("Mp3Player");
        notif.setContentText("Touch to open MP3Player");
        notif.setContentIntent(pendingIntent);
        notif.setSmallIcon(R.drawable.ic_launcher_background);
    }


}
