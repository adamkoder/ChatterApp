package com.example.publicchat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatRoomActivity extends AppCompatActivity {

    private ArrayList<Message> chatHistory;
    private ChatBubbleAdapter adapter;
    private ListView listView;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private Gson gson = new Gson();

    private Intent intent;
    public static User currentUser;
    private EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //sets toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        intent = getIntent();
        currentUser = fromStringToObj(intent.getStringExtra(IntentKeys.USER));

        getChatListFromSharedPrefs();
        if(chatHistory == null) {
            chatHistory = new ArrayList<Message>();
        }

        adapter = new ChatBubbleAdapter(this, R.layout.other_users_chat_bubble, chatHistory);
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
            setChatListToSharedPrefs();
            message.setText(null);
        }

        //Display snackbar popup if message wasnt entered
        else if (message.length() <= 0) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.messageText), "Please enter a message", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

        adapter.notifyDataSetChanged();
        listView.smoothScrollToPosition(chatHistory.size() - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_logout:
                logoutButton();
                return true;

            case R.id.action_clearChat:
                clearChat();
                return true;

            case R.id.action_search:
                showSearchPopupWindow();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showSearchPopupWindow(){
        LayoutInflater inflater = (LayoutInflater) ChatRoomActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.search_popup_window, null);

//        float density = ChatRoomActivity.this.getResources().getDisplayMetrics().density;
        final PopupWindow popupWindow = new PopupWindow(layout, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, true);

//        ((ImageButton) layout.findViewById(R.id.popup_search_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view){
//                popupWindow.dismiss();
//            }
//        });
        popupWindow.showAtLocation(layout, Gravity.TOP,0, 0);
    }

//    Finds the given keyword in the chatHistory list
//    public void searchChatHistory(String){
//        for(int i = 0; i < chatHistory.size(); i++){
//            if(chatHistory.get(i).getText().toLowerCase().contains(""))
//        }
//    }

    //Removes the currentUsername key from Shared Preferences and logsout the user
    public void logoutButton(){
        mPreferences.edit().remove("currentUsername").apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //Removes all the Nodes from the chatHistory list
    public void clearChat(){
        chatHistory.clear();
        setChatListToSharedPrefs();
        adapter.notifyDataSetChanged();
    }

    //Converts string to objects
    private User fromStringToObj(String json){
        return gson.fromJson(json, User.class);
    }

    //Gets the chat history from the shared preferences
    public void getChatListFromSharedPrefs(){
        chatHistory = gson.fromJson(mPreferences.getString("chatHistory",""),new TypeToken<ArrayList<Message>>(){}.getType());
    }

    //Adds the chat history to shared preferences
    public void setChatListToSharedPrefs(){
        mEditor.putString("chatHistory", gson.toJson(chatHistory)).apply();
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}