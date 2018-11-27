package com.example.publicchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoadingScreen extends AppCompatActivity {

    private static final String TAG = "LoadingScreen";

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        if(false){
            final Intent intent = new Intent(this, ChatRoomActivity.class);
            timer(intent);
        }

        else{
            final Intent intent = new Intent(this, LoginActivity.class);
            timer(intent);
        }
    }

    private void timer(final Intent intent){
        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(intent);
            }
        }.start();
    }
}
