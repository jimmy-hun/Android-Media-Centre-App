package com.jlhun3.MediaCentre;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class Music extends Activity implements OnCompletionListener {
    // for action listener (change play to pause)
    private ImageButton btnPlaylist;
    private ImageButton btnPlay;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageButton btnRepeat;
    private ImageButton btnShuffle;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private MediaPlayer mMediaPlayer;

    private static final String MEDIA_PATH = new String("/sdcard/Media/Music/");  // path to music folder
    public String intentString;  // so all can access intentString (name of playing audio)

    private PlayList playlist = new PlayList();  // for getting track list from Playlist class
    private int currentTrack;

    SeekBar seekBar;
    Handler seekHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player);

        // the intent parsed from playlist to get the song to be played and also for showing title of mp3 played
        Intent intent = getIntent();
        intentString = intent.getStringExtra("PlayThis");

        // if playlist intent passed attribute is null...
        if (intentString==null) {
            // Load an audio resource under /res/raw/audio.mp3 if not play list option is selected
            mMediaPlayer = MediaPlayer.create(this, R.raw.audio);
            mMediaPlayer.setOnCompletionListener(this);  // Set listener when play through completes
            mMediaPlayer.start();
        }

        // else play the file specified by the parsed intent
        else {
            // pass intentString from Playlist intentString to URI
            mMediaPlayer = MediaPlayer.create(this, Uri.parse(MEDIA_PATH+intentString));
            mMediaPlayer.setOnCompletionListener(this);  // Set listener when play through completes
            mMediaPlayer.start();
        }

        // action listeners
        btnPlaylist = (ImageButton) findViewById(R.id.btnPlaylist);  // button to open playlist
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);  // button play AND pause
        // Changing button image to pause button because you have to select from playlist for starters
        // and playback starts immediately
            btnPlay.setImageResource(R.drawable.pause);

        btnNext = (ImageButton) findViewById(R.id.btnNext);  // button for next
        btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);  // button for previous track
        btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);  // button to repeat track
        btnRepeat.setImageResource(R.drawable.repeat_close_button); // set repeat button to closed
        btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);  // button to shuffle

        seekBar = (SeekBar) findViewById(R.id.songProgressBar);  // stick seek bar action listener on this later

        // action listener for playlist
        btnPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // mMediaPlayer.stop();
                android.os.Process.killProcess(android.os.Process.myPid());

                Intent i = new Intent(getApplicationContext(), PlayList.class);
                startActivity(i);
            }
        });

        // action listener for play and pause
        btnPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                // check for already playing
                if (mMediaPlayer.isPlaying()) {
                    if (mMediaPlayer!=null) {
                        mMediaPlayer.pause();
                        Toast.makeText(getBaseContext(), "Paused", Toast.LENGTH_SHORT).show();

                        // Changing button image to play button
                        btnPlay.setImageResource(R.drawable.play);
                    }
                }

                else {
                    // Resume song
                    if (mMediaPlayer!=null) {
                        mMediaPlayer.start();
                        Toast.makeText(getBaseContext(), "Now playing: " + intentString, Toast.LENGTH_SHORT).show();

                        // Changing button image to pause button
                        btnPlay.setImageResource(R.drawable.pause);
                    }
                }
            }
        });

        // not working
        // action listener to go to previous track
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0){
                currentTrack--;
                if(currentTrack < 0) {
                    currentTrack = playlist.trackNames.size() - 1;
                }
            }
        });

        // not working
        // action listener to next track
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0){
                currentTrack++;
                if(currentTrack > playlist.trackNames.size()-1){
                    currentTrack = 0;
                }

            }
        });

        // action listener to enable repeat
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (isRepeat){
                    isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    btnRepeat.setImageResource(R.drawable.repeat_close_button);
                }

                else {
                    // make repeat to true
                    isRepeat = true;
                    Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isShuffle = false;
                    btnRepeat.setImageResource(R.drawable.repeat_button);
                    btnShuffle.setImageResource(R.drawable.shuffle_button);
                }
            }
        });

        // not working
        // action listener to enable shuffle
        btnShuffle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                if (isShuffle){
                    isShuffle = false;
                    Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    btnShuffle.setImageResource(R.drawable.shuffle_button);
                }

                else {
                    // make repeat to true
                    isShuffle= true;
                    Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isRepeat = false;
                    btnShuffle.setImageResource(R.drawable.shuffle_pressed_button);
                    btnRepeat.setImageResource(R.drawable.repeat_close_button);
                }
            }
        });

        // add action listener to seek bar
        seekBar.setOnSeekBarChangeListener(new SeekBarAction());
        // enable real time seek bar actions
        getInit();
        seekUpdation();

        // start audio playback one all is set and show notification
        currentTrack = 0; // set current track
        mMediaPlayer.start();
        Toast.makeText(getBaseContext(), "Now playing: " + intentString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // Changing button image to play button
        btnPlay.setImageResource(R.drawable.play);
    }

    // the following is for seek bar functionality
    public void getInit(){
        seekBar = (SeekBar) findViewById(R.id.songProgressBar);
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        seekBar.setMax(mMediaPlayer.getDuration());
    }

    Runnable run = new Runnable(){

        @Override
        public void run() {
            seekUpdation();
        }
    };

    public void seekUpdation() {
        seekBar.setProgress(mMediaPlayer.getCurrentPosition());
        seekHandler.postDelayed(run, 1);
    }

    class SeekBarAction implements OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            mMediaPlayer.seekTo(seekBar.getProgress());
        }
    }

    @Override
    public void onDestroy() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }
}