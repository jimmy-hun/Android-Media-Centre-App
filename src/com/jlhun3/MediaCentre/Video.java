package com.jlhun3.MediaCentre;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

public class Video extends Activity {
    private static final String MEDIA_PATH = new String("/sdcard/Media/Video/");  // path to video folder
    public String intentString;  // this is to determine the path of the video playing
    VideoView mVideoView;  // creates a view to watch videos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);

        // Get VideoView instance and enable media controls
        mVideoView = (VideoView)findViewById(R.id.videoView);
        mVideoView.setMediaController(new MediaController(this));

        // Specify video location and set it for the View, some tests are given
        // Uri video = Uri.parse("/sdcard/Media/Video/Godzilla Cat.mp4");
        // Uri video = Uri.parse(MEDIA_PATH+intentString);

        Intent intent = getIntent();  // intent parsed from video list for getting video to play
        intentString = intent.getStringExtra("PlayThis");
        Uri video = Uri.parse(MEDIA_PATH+intentString);

        mVideoView.setVideoURI(video);

        // Focus VideoView in interface and start playing video
        mVideoView.requestFocus();
        mVideoView.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // terminate safely
    @Override
    public void onDestroy() {
        mVideoView.stopPlayback();
        finish();
        super.onDestroy();
        // android.os.Process.killProcess(android.os.Process.myPid());
    }
}
