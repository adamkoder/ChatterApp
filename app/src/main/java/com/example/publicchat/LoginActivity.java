package com.example.publicchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {

    public static final String USER_NAME = "com.example.publicchat.USERNAME";

    private ArrayList<User> listOfUsers = new ArrayList<User>();
    private Iterator<User> iterator = listOfUsers.iterator();

    private String key;

    private User currentUser;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    /*
        Method thats called by the ENTER CHAT ROOM button
        It starts the Chat room activity and sends it the entered username
     */
    public void enterChat(View view){
        Intent intent = new Intent(this, ChatRoomActivity.class);

        //Gets the username typed in by the user
        EditText getUsername = (EditText) findViewById(R.id.username);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //Loop the listOfUsers looking for existing username, makes new one if username not found

        if(listOfUsers.isEmpty()){
            listOfUsers.add(new User(String.valueOf(listOfUsers.size()), getUsername.getText().toString()));
//            mEditor.putString(String.valueOf(listOfUsers.size()), getUsername.getText().toString());
//            mEditor.apply();
            this.key = listOfUsers.get(listOfUsers.size() - 1).getID();
            this.currentUser = listOfUsers.get(listOfUsers.size() - 1);
        }

        while(iterator.hasNext()){

            if(!iterator.hasNext() || listOfUsers.isEmpty()){
                listOfUsers.add(new User(String.valueOf(listOfUsers.size()), getUsername.getText().toString()));
//                mEditor.putString(String.valueOf(listOfUsers.size()), getUsername.getText().toString());
//                mEditor.apply();
                this.key = iterator.next().getID();
                this.currentUser = iterator.next();
                break;
            }

            else if(iterator.next().getUsername().equals(getUsername.getText().toString())) {
                this.key = iterator.next().getID();
                this.currentUser = iterator.next();
                break;
            }
        }

        if(this.currentUser.getUsername().length() > 2) {
            intent.putExtra(USER_NAME, mPreferences.getString(key, "Something went wrong"));
            startActivity(intent);
        }
        else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.enterRoomButton),"Enter a name with 3 or more characters", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}