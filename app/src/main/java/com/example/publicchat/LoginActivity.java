package com.example.publicchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    public static final String USER_NAME = "com.example.publicchat.USERNAME";

    private User currentUser;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private ArrayList<User> listOfIds = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        listOfIds.add(new User("1", "John"));
        listOfIds.add(new User("2", "Mike"));
        listOfIds.add(new User("3", "Jevrozim"));
        listOfIds.add(new User("4", "Stevan"));

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();
    }

     /*
        Method thats called by the ENTER CHAT ROOM button
        It starts the Chat room activity and sends it the entered username
     */
    public void enterChat(View view){
        Intent intent = new Intent(this, ChatRoomActivity.class);


        //Gets the username typed in by the user
        EditText getUsername = (EditText) findViewById(R.id.username);

        User user = new User(Integer.toString(listOfIds.size() + 1), getUsername.getText().toString());

        if(listOfIds.size() <= 0) {
            listOfIds.add(new User(Integer.toString(listOfIds.size() + 1), getUsername.getText().toString()));
            currentUser = listOfIds.get(listOfIds.size());
        }

        else if(listOfIds.contains(user)){
            for (int i = 0; i < listOfIds.size(); i++) {
                User tempUser = listOfIds.get(i);
                if (tempUser.getUsername().equals(getUsername.getText().toString())) {
                    currentUser = tempUser;
                    break;
                }
            }
        }

        else {
            listOfIds.add(new User(Integer.toString(listOfIds.size()+1), getUsername.getText().toString()));
            currentUser = listOfIds.get(listOfIds.size() - 1);
        }

        if(this.currentUser.getUsername().length() > 2) {
//            intent.putExtra(USER_NAME, mPreferences.getString(key, "Something went wrong"));
            mEditor.putString("currentUsername", getUsername.getText().toString());
            mEditor.apply();
            intent.putExtra(USER_NAME, currentUser.getUsername());
            startActivity(intent);
        }
        else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.enterRoomButton),"Enter a name with 3 or more characters", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}