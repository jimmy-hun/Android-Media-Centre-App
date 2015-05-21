package com.jlhun3.MediaCentre;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.*;

public class PlayList extends ListActivity {
    private static final String MEDIA_PATH = new String("/sdcard/Media/Music/");  // path to music folder
    public ArrayAdapter<String> songList;  // holds songs

    private static final String[] extensions = {".mp3", ".mid", ".wav"};  // extensions for checking files
    private File music_path;  // for storing music file path
    public List<String> trackNames = new ArrayList<String>();  // for storing music names, also made public so Music can access it
    private int currentTrack;  // id of current track

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.songlist);

        currentTrack = 0;

        // get music files and add to array
        addMusic(getFiles());
    }

    // get files by adding to string array
    private String[] getFiles(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                || Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            // reads from /sdcard/Media/Music onlys
            music_path = new File(MEDIA_PATH);
            String[] tempList = music_path.list();
            return tempList;
        }

        else {
            Toast.makeText(getBaseContext(), "SD Card is mounted or unusable", Toast.LENGTH_LONG).show();
        }

        return null;
    }

    // Adds the playable files to the trackNames List
    private void addMusic(String[] tempList){
        if(tempList != null){
            for(int i = 0; i < tempList.length; i++){
                // Only accept files that have one of the extensions in the extensions array
                if (trackChecker(tempList[i])) {
                    trackNames.add(tempList[i]);
                }
            }

            Toast.makeText(getBaseContext(), "Loaded " + Integer.toString(trackNames.size()) + " Tracks", Toast.LENGTH_SHORT).show();

            // songList is public so you can get the list from Music
            songList = new ArrayAdapter<String>(this, R.layout.list_item, trackNames);
            setListAdapter(songList);
        }
    }

    //Checks to make sure that the track to be loaded has a correct extension
    private boolean trackChecker(String trackToTest){
        for(int j = 0; j < extensions.length; j++){
            if (trackToTest.contains(extensions[j])){
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        currentTrack = position;
        playSong(trackNames.get(position));  // get position of tracks in the array and send to playSong
        // playSong(MEDIA_PATH + songs.get(position));   // this also shows extension
    }

    public void playSong(String playThisFile) {
        Intent intent = new Intent(getApplication(), Music.class);  // open a new music instance
        intent.putExtra("PlayThis", playThisFile); //  set string for Music to read
        startActivity(intent);
    }

    // shut down process
    @Override
    public void onDestroy() {
        finish();
        super.onDestroy();
        // android.os.Process.killProcess(android.os.Process.myPid());
    }
}