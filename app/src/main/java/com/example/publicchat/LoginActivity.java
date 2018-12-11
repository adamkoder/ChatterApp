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

public class LoginActivity extends AppCompatActivity {

    private User currentUser;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private Gson gson = new Gson();
    private ArrayList<User> listOfIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        this.listOfIds = gson.fromJson(intent.getStringExtra(IntentKeys.LIST_OF_USERS), new TypeToken<ArrayList<User>>(){}.getType());

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



        if(getUsername.getText().toString().length() > 2) {

            if(listOfIds == null)
                getIdListFromSharedPrefs();

            if(listOfIds.size() <= 0 ) {
                listOfIds.add(new User(Integer.toString(listOfIds.size() + 1), getUsername.getText().toString()));
                currentUser = listOfIds.get(listOfIds.size() - 1);
            }

            for(int i = 0; i < listOfIds.size(); i++){
                if(listOfIds.get(i).getUsername().equals(getUsername.getText().toString())){
                    currentUser = listOfIds.get(i);
                    break;
                }
                else if(i == listOfIds.size()){
                    listOfIds.add(new User(Integer.toString(listOfIds.size()+1), getUsername.getText().toString()));
                    currentUser = listOfIds.get(listOfIds.size() - 1);
                }
            }

            mEditor.putString("currentUsername", fromObjToString(currentUser)).apply();
            intent.putExtra(IntentKeys.USER, fromObjToString(currentUser));
            setIdListToSharedPrefs();
            startActivity(intent);
        }
        else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.enterRoomButton),"Enter a name with 3 or more characters", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    //adds the list of all user to the class list from the listOfIds key
    private void getIdListFromSharedPrefs(){
        String json = mPreferences.getString("listOfIds","");
        listOfIds = gson.fromJson(json,new TypeToken<ArrayList<User>>(){}.getType());
    }

    //Sets the current list of users to Shared Preferences under the key listOfIds
    private void setIdListToSharedPrefs(){
        String json = gson.toJson(listOfIds);
        mEditor.putString("listOfIds", json).apply();
    }

    //Converts Objects to strings
    private String fromObjToString(User userObj){
        return gson.toJson(userObj);
    }
}