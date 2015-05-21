package com.jlhun3.MediaCentre;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StreamInput extends Activity {
    private EditText userInput;
    private Button submit;
    private Button cancel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream_input_prompt);

        userInput = (EditText) findViewById(R.id.streamUserInput);

        // set up buttons
        submit = (Button) findViewById(R.id.submitButton);
        cancel = (Button) findViewById(R.id.cancelButton);

        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String streamLink = new String(userInput.getText().toString());  // save current state of user input as streamLink

                Intent intent = new Intent(getApplication(), Stream.class);  // open a new stream instance
                intent.putExtra("StreamThis", streamLink); //  set string for Stream to read

                // toast what gets played
                Toast.makeText(getApplicationContext(), "Now Playing: " + streamLink, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        // action listener for playlist
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }
}