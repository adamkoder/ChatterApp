package com.example.publicchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoadingScreen extends AppCompatActivity {

    private static final String TAG = "LoadingScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        SharedPreferences mPreferences;

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(mPreferences.getString("currentUsername", "defaultString").equals("defaultString")){
            final Intent intent = new Intent(this, LoginActivity.class);
            timer(intent);
        }

        else{
            final Intent intent = new Intent(this, ChatRoomActivity.class);
            timer(intent);
        }
    }

    private void timer(final Intent intent){
        new CountDownTimer(2000, 1000) {
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
