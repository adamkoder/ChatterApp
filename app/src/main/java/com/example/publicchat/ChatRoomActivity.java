package com.example.publicchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {
    private static final String TAG = "ChatBubbleAdapter";

    private ArrayList<ChatBubbleInfo> list;
    private ChatBubbleAdapter adapter;
    private ListView listView;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private EditText message;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //sets toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d(TAG, "on create started.");

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        list = new ArrayList<ChatBubbleInfo>();

        adapter = new ChatBubbleAdapter(this, R.layout.adapter_view_layout, list);
        listView = (ListView) findViewById(R.id.chat);
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        username = intent.getStringExtra(LoginActivity.USER_NAME);

        if(username == null)
            username = mPreferences.getString("currentUsername", "something went wrong");
    }
     /*
        Method called by the Send button
        Adds the message from the Plaint text field to the list onClick
     */
    public void sendMessage(View view) {
        message = (EditText) findViewById(R.id.messageText);

        //Adds the chatbubble to the list if message entered
        if (message.length() > 0) {
            ChatBubbleInfo chatBubbleInfo = new ChatBubbleInfo(username, message.getText().toString());
            list.add(chatBubbleInfo);
            message.setText(null);

        }

        //Display snackbar popup if message wasnt entered
        else if (message.length() == 0) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.messageText), "Please enter a message", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

        adapter.notifyDataSetChanged();
        listView.smoothScrollToPosition(list.size() - 1);
    }

    public void logoutButton(){
        mPreferences.edit().remove("currentUsername").apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_logout:
                logoutButton();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }
}