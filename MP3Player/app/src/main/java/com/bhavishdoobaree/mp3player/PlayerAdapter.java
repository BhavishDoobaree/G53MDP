package com.bhavishdoobaree.mp3player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;

//module extracts info from track for display

public class PlayerAdapter extends ArrayAdapter {

     PlayerAdapter(Context context, File[] tracks)
    {
        super(context, R.layout.music_list, tracks);
    }

    @NonNull
    @Override
    public View getView(int pos, View changeView, ViewGroup parent)
    {
        LayoutInflater trackInflator = LayoutInflater.from(getContext());
        View trackView = trackInflator.inflate(R.layout.music_list, parent, false);

        ImageView albumArt = (ImageView)trackView.findViewById(R.id.albumArt);
        TextView trackTitle = (TextView)trackView.findViewById(R.id.trackTitle);
        TextView trackArtist = (TextView)trackView.findViewById(R.id.trackArtist);
        TextView trackDuration = (TextView)trackView.findViewById(R.id.trackLength);

        try{
            Mp3File mp3Track = new Mp3File((File) getItem(pos));
            if (mp3Track.hasId3v2Tag()){
                trackTitle.setText(mp3Track.getId3v2Tag().getTitle());
                trackArtist.setText(mp3Track.getId3v2Tag().getArtist());
                try{
                    albumArt.setImageBitmap(getArt(mp3Track));
                }catch (NullPointerException e){
                    Log.e("No album art found ", e.toString());
                }
            }else if(mp3Track.hasId3v1Tag()){
                trackTitle.setText(mp3Track.getId3v1Tag().getTitle());
                trackArtist.setText(mp3Track.getId3v1Tag().getArtist());
            }else {
                Log.d("Error","Meta data incompatible");
                trackTitle.setText("UnknownTrack");
                trackArtist.setText("UnknownArtist");
            }
            int secs = (int)mp3Track.getLengthInSeconds() % 60;
            int mins = (int)mp3Track.getLengthInSeconds() / 60;

            trackDuration.setText(String.format("%02d:%02d", mins, secs));
        }catch (IOException | InvalidDataException | UnsupportedTagException e)
        {
            e.printStackTrace();
        }
        return trackView;

    }

    private Bitmap getArt(Mp3File track)
    {
        byte[] trackImg = track.getId3v2Tag().getAlbumImage();
        return BitmapFactory.decodeByteArray(trackImg, 0, trackImg.length);
    }
}
