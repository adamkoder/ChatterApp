package com.example.publicchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class LoadingScreen extends AppCompatActivity {


    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private Gson gson = new Gson();

    private ArrayList<User> listOfIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        getIdListFromSharedPrefs();
        if(mPreferences.getString("currentUsername", "").equals("")){
            final Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(IntentKeys.LIST_OF_USERS, fromListToJSON(listOfIds));
            timer(intent);
        }

        else{
            final Intent intent = new Intent(this, ChatRoomActivity.class);
            intent.putExtra(IntentKeys.USER, mPreferences.getString("currentUsername", "not working!"));
            timer(intent);
        }
    }

    @Override
    public void onBackPressed(){}

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
    private String fromListToJSON(ArrayList list){
        return gson.toJson(list);
    }
}
