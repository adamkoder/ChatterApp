package com.example.publicchat;

import android.content.Intent;
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

    public void enterChat(View view){
        Intent intent = new Intent(this, ChatRoomActivity.class);
        EditText getUsername = (EditText) findViewById(R.id.username);
        String username = getUsername.getText().toString();
        intent.putExtra(USER_NAME, username);
        startActivity(intent);
    }
}
