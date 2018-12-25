package com.bhavishdoobaree.mp3player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import java.io.IOException;
import com.mpatric.mp3agic.*;

/**
 * Created by pszmdf on 06/11/16.
 * Modified by Bhavish Doobaree khcy6dbb
 */
public class MP3Player {

    protected MediaPlayer mediaPlayer;
    protected MP3PlayerState state;
    protected String filePath;
    protected Mp3File musicTrack;

    public enum MP3PlayerState {
        ERROR,
        PLAYING,
        PAUSED,
        STOPPED
    }

    public MP3Player() {
        this.state = MP3PlayerState.STOPPED;
    }

    public MP3PlayerState getState() {
        return this.state;
    }

    public void load(String filePath) {
        this.filePath = filePath;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            musicTrack = new Mp3File(filePath);
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedTagException e) {
            e.printStackTrace();
        }

        this.state = MP3PlayerState.PLAYING;
        mediaPlayer.start();
    }

    public String getFilePath() {
        return this.filePath;
    }

    public int getProgress() {
        if(mediaPlayer!=null) {
            if(this.state == MP3PlayerState.PAUSED || this.state == MP3PlayerState.PLAYING)
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    //to be used by seekbar to get to a specific part of track
    public void setProgressBar(int progress)
    {
        if (mediaPlayer != null) {
            if (this.state == MP3PlayerState.PLAYING || this.state == MP3PlayerState.PAUSED) {
                mediaPlayer.seekTo(progress);
            }
        }
    }

    public void play() {
        if(this.state == MP3PlayerState.PAUSED) {
            mediaPlayer.start();
            this.state = MP3PlayerState.PLAYING;
        }
    }

    public void pause() {
        if(this.state == MP3PlayerState.PLAYING) {
            mediaPlayer.pause();
            state = MP3PlayerState.PAUSED;
        }
    }

    public void stop() {
        if(mediaPlayer!=null) {
            if(mediaPlayer.isPlaying())
                mediaPlayer.stop();
            state = MP3PlayerState.STOPPED;
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}