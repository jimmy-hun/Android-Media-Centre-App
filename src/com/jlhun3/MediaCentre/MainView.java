package com.jlhun3.MediaCentre;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainView extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
    }

    public void startAudioActivity(View v) {
        Intent i = new Intent(this, PlayList.class);
        startActivity(i);
    }

    public void startVideoActivity(View v) {
        Intent i = new Intent(this, VideoList.class);
        startActivity(i);
    }

    public void startStreamActivity(View v) {
        Intent i = new Intent(this, StreamInput.class);
        startActivity(i);
    }

    @Override
    public void onDestroy() {
        finish();
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
