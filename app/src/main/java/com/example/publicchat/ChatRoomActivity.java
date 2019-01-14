package com.example.publicchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    public static final String TAG = "TAG";

    private ArrayList<Message> chatHistory;
    private ChatBubbleAdapter adapter;
    private RecyclerView recyclerView;

    private FirebaseDatabase database;
    private DatabaseReference dbRefChatHistory;
    private DatabaseReference pushedDbRefChatHistory;

    private FirebaseAuth mAuth;

    private Message chatBubbleInfo;

    private Gson gson = new Gson();

    private EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        mAuth = FirebaseAuth.getInstance();

        //sets toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        dbRefChatHistory = database.getReference().child(DbKeys.chatRoom);
        pushedDbRefChatHistory = dbRefChatHistory.push();

        getChatHistoryFromDatabase();
        if(chatHistory == null) {
            chatHistory = new ArrayList<>();
        }

        adapter = new ChatBubbleAdapter(chatHistory);
        recyclerView = findViewById(R.id.chat);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        setTitle("Logged in as : " + User.getUsername());
    }
    

     /*
        Method called by the Send button
        Adds the message from the Plaint text field to the chatHistory onClick
     */
    public void sendMessage(View view) {
        message = (EditText) findViewById(R.id.messageText);

        //Adds the chatbubble to the chatHistory if message entered
        if (message.length() > 0) {
            chatBubbleInfo = new Message(message.getText().toString(), User.getUsername(), Calendar.getInstance().getTime());
            chatHistory.add(chatBubbleInfo);
            setChatHistoryToDatabase();
            message.setText(null);

            adapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(chatHistory.size() - 1);
        }

        //Display snackbar popup if message wasnt entered
        else if (message.length() <= 0) {
            Snackbar.make(findViewById(R.id.messageText), "Can't send empty message", Snackbar.LENGTH_SHORT).show();
        }
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

        final PopupWindow popupWindow = new PopupWindow(layout, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(layout, Gravity.TOP,0, 0);
    }

    //Removes the currentUsername key from Shared Preferences and logsout the user
    public void logoutButton(){
        User.clearUser();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    //Removes all the Nodes from the chatHistory list
    public void clearChat(){
        chatHistory = new ArrayList<>();
        setChatHistoryToDatabase();
        adapter.notifyDataSetChanged();
    }

    //Disables the back button action
    @Override
    public void onBackPressed(){}

    //Gets the chat history from real time database
    public void getChatHistoryFromDatabase(){
        dbRefChatHistory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatHistory.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot chatBubble : dataSnapshot.getChildren()){
                    keys.add(chatBubble.getKey());
                    Message message = chatBubble.getValue(Message.class);
                    chatHistory.add(message);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Sum Ting Wong", Toast.LENGTH_LONG).show();
            }
        });
    }

    //Adds the chat history to shared preferences
    public void setChatHistoryToDatabase(){
       dbRefChatHistory.push().setValue(chatBubbleInfo);
    }
}