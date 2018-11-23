package com.example.publicchat;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    public static final String USER_NAME = "com.example.publicchat.USERNAME";

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
        EditText getUsername = (EditText) findViewById(R.id.username);
        String username = getUsername.getText().toString();
        if(username.length() > 2) {
            intent.putExtra(USER_NAME, username);
            startActivity(intent);
        }
        else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.enterRoomButton),"Enter a name with 3 or more characters", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}