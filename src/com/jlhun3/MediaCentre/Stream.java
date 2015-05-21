package com.jlhun3.MediaCentre;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

public class Stream extends Activity {
    private static final String MEDIA_PATH = new String("/sdcard/Media/Video/");  // path to video folder (testing purposes)
    private String intentString;  // so all can access intentString (name of playing audio)
    VideoView streamView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream_video);

        // Get VideoView instance and enable media controls
        streamView = (VideoView)findViewById(R.id.streamView);
        streamView.setMediaController(new MediaController(this));

        // Specify video location and set it for the View, some test values are given
        // Uri video = Uri.parse("http://www.youtube.com/watch?v=pDnxDQZ3ZYM");
        // Uri video = Uri.parse("/sdcard/Media/Video/Godzilla Cat.mp4");
        // Uri video = Uri.parse(MEDIA_PATH+intentString);

        Intent intent = getIntent();  // intent parsed from video list for getting video to play
        intentString = intent.getStringExtra("StreamThis");
        Uri video = Uri.parse(intentString);

        streamView.setVideoURI(video);

        // Focus VideoView in interface and start playing video
        streamView.requestFocus();
        streamView.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        streamView.stopPlayback();
        finish();
        super.onDestroy();
        // android.os.Process.killProcess(android.os.Process.myPid());
    }
}
