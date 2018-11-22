package com.example.publicchat;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    EditText message;
    String sentMessage;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        list = new ArrayList<String>();

        Intent intent = getIntent();
        username = intent.getStringExtra(LoginActivity.USER_NAME);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        ListView listView = (ListView) findViewById(R.id.chat);
        listView.setAdapter(adapter);
    }

    public void sendMessage(View view){
        message = (EditText) findViewById(R.id.messageText);
        message.setVisibility(View.VISIBLE);

        sentMessage = message.getText().toString();
        String addedText = username + " : " + sentMessage;
        list.add(addedText);
    }
}