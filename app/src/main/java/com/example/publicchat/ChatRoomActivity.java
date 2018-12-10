package com.example.publicchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatRoomActivity extends AppCompatActivity {

    private ArrayList<Message> chatHistory;
    private ChatBubbleAdapter adapter;
    private ListView listView;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private Gson gson = new Gson();

    private Intent intent;
    private User currentUser;
    private EditText message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //sets toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        intent = getIntent();
        System.out.println(intent.getExtras());
        currentUser = fromStringToObj(intent.getStringExtra(LoginActivity.USER));        //<------------------------

        getChatListFromSharedPrefs();
        if(chatHistory == null) {
            chatHistory = new ArrayList<Message>();
            chatHistory.add(new Message("Test me", currentUser, Calendar.getInstance().getTime()));
        }

        adapter = new ChatBubbleAdapter(this, R.layout.adapter_view_layout, chatHistory);
        listView = (ListView) findViewById(R.id.chat);
        listView.setAdapter(adapter);
    }
     /*
        Method called by the Send button
        Adds the message from the Plaint text field to the chatHistory onClick
     */
    public void sendMessage(View view) {
        message = (EditText) findViewById(R.id.messageText);

        //Adds the chatbubble to the chatHistory if message entered
        if (message.length() > 0) {
            Message chatBubbleInfo = new Message(message.getText().toString(), currentUser, Calendar.getInstance().getTime());
            chatHistory.add(chatBubbleInfo);
//            setChatListToSharedPrefs();
            message.setText(null);

        }

        //Display snackbar popup if message wasnt entered
        else if (message.length() == 0) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.messageText), "Please enter a message", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

        adapter.notifyDataSetChanged();
        listView.smoothScrollToPosition(chatHistory.size() - 1);
    }

    //Removes the currentUsername key from Shared Preferences and logsout the user
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
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    //Converts string to objects
    private User fromStringToObj(String json){
        return gson.fromJson(json,User.class);
    }

    //Gets the chat history from the shared preferences
    public void getChatListFromSharedPrefs(){
        String json = mPreferences.getString("chatHistory","");
        chatHistory = gson.fromJson(json,new TypeToken<ArrayList<Message>>(){}.getType());
    }

    //Adds the chat history to shared preferences
    public void setChatListToSharedPrefs(){
        String json = gson.toJson(chatHistory);
        mEditor.putString("chatHistory", json).apply();
    }
}