package com.example.publicchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LoadingScreen extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private Gson gson = new Gson();

    private ArrayList<User> listOfIds;
    private ArrayList<Message> chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        listOfIds = new ArrayList<User>();
        listOfIds.add(new User("1", "Adam"));

        chat = new ArrayList<Message>();
        chat.add(new Message("Test message", listOfIds.get(0), Calendar.getInstance().getTime()));

        setChatListToSharedPrefs();
        setIdListToSharedPrefs();

        if(mPreferences.getString("currentUsername", "").equals("")){
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

    private void getIdListFromSharedPrefs(){
        String json = mPreferences.getString("listOfIds","");
        listOfIds = gson.fromJson(json,new TypeToken<ArrayList<User>>(){}.getType());
    }

    //Sets the current list of users to Shared Preferences under the key listOfIds
    private void setIdListToSharedPrefs(){
        String json = gson.toJson(listOfIds);
        mEditor.putString("listOfIds", json).apply();
    }

    public void getChatListFromSharedPrefs(){
        String json = mPreferences.getString("chat","");
        chat = gson.fromJson(json,new TypeToken<ArrayList<Message>>(){}.getType());
    }

    public void setChatListToSharedPrefs(){
        String json = gson.toJson(chat);
        mEditor.putString("chat", json).apply();
    }
}
