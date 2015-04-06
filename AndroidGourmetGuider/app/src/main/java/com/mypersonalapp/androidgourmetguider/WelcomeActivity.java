package com.mypersonalapp.androidgourmetguider;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;


public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        final Intent myIntent = new Intent(this,MainActivity.class);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
        @Override
        public void run() {
            startActivity(myIntent);
        }
        };
        timer.schedule(task, 1000*3);//jump to the mainActivity after 2 sec
    }
}
