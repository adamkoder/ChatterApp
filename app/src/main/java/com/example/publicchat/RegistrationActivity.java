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

import java.util.ArrayList;

public class RegistrationActivity extends AppCompatActivity {

    private ArrayList<User> listOfIds = new ArrayList<>();
    private Gson gson = new Gson();
    private boolean canCreateNewUser;

    private String currentUserName;
    private String currentPassword;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        canCreateNewUser = true;

        Intent intent = new Intent();
        this.listOfIds = gson.fromJson(intent.getStringExtra(IntentKeys.LIST_OF_USERS), new TypeToken<ArrayList<User>>(){}.getType());
    }

    public void registerUser(View view){
        EditText getUsername = (EditText) findViewById(R.id.registrationUsername);
        EditText getPassword = (EditText) findViewById(R.id.registrationPassword);

        this.currentUserName = getUsername.getText().toString();
        this.currentPassword = getPassword.getText().toString();

        if(currentPassword.length() > 6 || currentUserName.length() > 3) {
            if (currentUserName.isEmpty() || currentPassword.isEmpty()) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.registrationButton), "Please fill in all the information", Snackbar.LENGTH_LONG);
                snackbar.show();
                canCreateNewUser = false;
            }

            else if (listOfIds == null) {
                listOfIds = new ArrayList<>();
                listOfIds.add(new User(Integer.toString(listOfIds.size() + 1), currentUserName, currentPassword));
                canCreateNewUser = false;
                createNewUser();
            }

            else {
                for (int i = 0; i < listOfIds.size(); i++) {
                    if (listOfIds.get(i).getUsername().equals(currentUserName)) {
                        ((EditText) findViewById(R.id.registrationUsername)).setText("");
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.registrationButton), "Username already exists", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        this.canCreateNewUser = false;
                        break;
                    }
                }
            }
            if (canCreateNewUser) {
                createNewUser();
            }
        }
        else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.registrationButton), "Password must be longer than 6 characters", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }

    //Adds a new user to the list and starts the ChatRoomActivity with that User
    private void createNewUser(){
        listOfIds.add(new User(Integer.toString(listOfIds.size() + 1), currentUserName, currentPassword));
        Intent intent = new Intent(this, ChatRoomActivity.class);
        intent.putExtra(IntentKeys.USER, fromObjToString(listOfIds.get(listOfIds.size() - 1)));
        mEditor.putString("currentUsername", fromObjToString(listOfIds.get(listOfIds.size() - 1)));
        setIdListToSharedPrefs();
        startActivity(intent);
    }

    //Converts User objects to JSON
    private String fromObjToString(User userObj){
        return gson.toJson(userObj);
    }

    //Sets the current list of users to Shared Preferences under the key listOfIds
    private void setIdListToSharedPrefs(){
        String json = gson.toJson(listOfIds);
        mEditor.putString("listOfIds", json).apply();
    }
}