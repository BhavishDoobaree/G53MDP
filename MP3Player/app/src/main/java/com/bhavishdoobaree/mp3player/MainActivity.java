package com.bhavishdoobaree.mp3player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;

import com.bhavishdoobaree.mp3player.PlayerService.playerServiceBinder;

public class MainActivity extends AppCompatActivity {

    //bound
    private PlayerService playerService;
    private boolean BoolBound =  false;

    //music track info
    private ListView trackList;
    private SeekBar trackSeek;
    private TextView trackNameSelected;
    private TextView trackArtistSelected;
    private TextView timeProgress;
    private TextView totalTime;

    //threads
    private Thread threadTrack;
    Boolean SeekBarBoolean = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniWidgets();

        Intent intent = new Intent(this, PlayerService.class);

        if (!BoolBound)
        {
            this.startService(intent);
            bindService(intent, trackServiceConnection, Context.BIND_AUTO_CREATE);
        }
        Log.d("MainActivity", "onCreate" + playerService);

    }

    protected void onDestroyed()
    {
        super.onDestroy();
        if (BoolBound){
            unbindService(trackServiceConnection);
        }
    }

    //play button config
    public void playButtonPlayer(View view)
    {
        playerService.getMp3Player().play();
    }

    //pause button
    public void pauseButtonPlayer(View view)
    {
        playerService.getMp3Player().pause();
    }

    //stop track method
    public void stopButtonPlayer(View view)
    {
        stopFunction();
    }

    //stop button function
    private void stopFunction()
    {
        //stop track
        playerService.getMp3Player().stop();

        //clear parameters
        trackNameSelected.setText("");
        trackArtistSelected.setText("");
        timeProgress.setText("00:00");
        totalTime.setText("00:00");

    }

    //oncreate initialize

    private void iniWidgets()
    {
        File trackDirectory;
        File[] ListTrack;

        trackList = (ListView)findViewById(R.id.trackList);
        trackSeek = (SeekBar)findViewById(R.id.seekBar);
        trackNameSelected = (TextView)findViewById(R.id.trackTitle);
        trackArtistSelected = (TextView)findViewById(R.id.trackArtist);
        timeProgress = (TextView)findViewById(R.id.timeProgressed);
        totalTime = (TextView)findViewById(R.id.timeTotal);

        try
        {
            trackDirectory = new File(Environment.getExternalStorageDirectory().getPath()+"/Music/");
            Log.d("Initializing tracks ", Environment.getExternalStorageDirectory().getPath()+"/Music/");
            ListTrack = trackDirectory.listFiles();
            ListAdapter playAdapter = new PlayerAdapter(this, ListTrack);

            trackList.setAdapter(playAdapter);
            trackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> myAdapt, View Myview, int Myposition, long Myid) {
                    File listselect = (File)(trackList.getItemAtPosition(Myposition));
                    Log.d("MP3Player", listselect.getAbsolutePath());
                    trackLoader(listselect.getAbsolutePath());
                }
            });
        }catch (NullPointerException e)
        {
            Log.e("Error iniWidget", e.toString());
        }

        trackSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timeUpdate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                SeekBarBoolean = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playerService.getMp3Player().setProgressBar(seekBar.getProgress()*1000);
                SeekBarBoolean = false;
            }
        });
    }

    //load track

    private void trackLoader(String pathFile)
    {
        playerService.getMp3Player().stop();
        playerService.getMp3Player().load(pathFile);
        if (playerService.getMp3Player().getState() != MP3Player.MP3PlayerState.ERROR)
        {
            iniTrackInfo();
        }
    }


    //get track info and load

    private void iniTrackInfo()
    {
        try
        {
            if(playerService.getMp3Player().getFilePath() !=null) {
                Mp3File trackInfoDisp = new Mp3File(playerService.getMp3Player().getFilePath());
                trackSeek.setMax((int) trackInfoDisp.getLengthInSeconds());

                int secs = (int) trackInfoDisp.getLengthInSeconds() % 60;
                int mins = (int) trackInfoDisp.getLengthInSeconds() / 60;
                totalTime.setText(String.format("%02d:%02d", mins, secs));

                if (trackInfoDisp.hasId3v1Tag()) {
                    trackNameSelected.setText(trackInfoDisp.getId3v1Tag().getTitle());
                    trackArtistSelected.setText(trackInfoDisp.getId3v1Tag().getArtist());
                } else if (trackInfoDisp.hasId3v2Tag()) {
                    trackNameSelected.setText(trackInfoDisp.getId3v2Tag().getTitle());
                    trackArtistSelected.setText(trackInfoDisp.getId3v2Tag().getArtist());
                } else {
                    trackNameSelected.setText("UnknownTrack");
                    trackArtistSelected.setText("UnknownArtist");
                }
            }
        }catch (IOException | UnsupportedTagException | InvalidDataException | NullPointerException e)
        {
            Log.e("Initialise Track", e.toString());
        }
        catch (Exception e1)
        {
            Log.e("Initialise Track", e1.toString());
        }
    }

    private ServiceConnection trackServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            playerServiceBinder binder = (playerServiceBinder) service;
            playerService = binder.getService();
            Log.d("trackServiceConnection", playerService.toString() + "starting");
            BoolBound = true;
            progressBarThread();
            iniTrackInfo();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            BoolBound = false;
        }
    };

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (!SeekBarBoolean){
                trackSeek.setProgress(playerService.getMp3Player().getProgress() /1000);
                timeUpdate();
            }
            return true;
        }
    });

    private void timeUpdate()
    {
        int secs = (trackSeek.getProgress()) % 60;
        int mins = (trackSeek.getProgress()) / 60;
        timeProgress.setText(String.format("%02d:%02d", mins, secs));
    }

    private void progressBarThread()
    {
        Runnable runThread = new Runnable() {
            @Override
            public void run() {
                while (BoolBound)
                {
                    try
                    {
                        Thread.sleep(500);
                        handler.sendEmptyMessage(0);

                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        if(threadTrack == null)
        {
            threadTrack = new Thread(runThread);
            threadTrack.start();
        }
        Log.d("trackThread", "Progress: " + threadTrack.isAlive());
    }
}
