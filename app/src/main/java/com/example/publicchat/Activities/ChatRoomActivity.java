package com.example.publicchat.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.publicchat.Adapters.ChatBubbleAdapter;
import com.example.publicchat.Models.CurrentUserModel;
import com.example.publicchat.Common.DbKeys;
import com.example.publicchat.Models.MessageModel;
import com.example.publicchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    public static final String TAG = "TAG";

    private ArrayList<MessageModel> chatHistory;
    private ChatBubbleAdapter adapter;
    private RecyclerView recyclerView;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    private MessageModel chatBubbleInfo;

    private EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //sets toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child(DbKeys.chatRoom);
        mAuth = FirebaseAuth.getInstance();

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

        setTitle("Logged in as : " + CurrentUserModel.getInstance().getUsername());
    }
    

     /*
        Method called by the Send button
        Adds the message from the Plaint text field to the chatHistory onClick
     */
    public void sendMessage(View view) {
        message = (EditText) findViewById(R.id.messageText);

        //Adds the chatbubble to the chatHistory if message entered
        if (message.length() > 0) {
            System.out.println(CurrentUserModel.getInstance().getUsername());
            chatBubbleInfo = new MessageModel(message.getText().toString(), CurrentUserModel.getInstance().getUsername(), Calendar.getInstance().getTime());
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Removes the currentUsername key from Shared Preferences and logsout the user
    public void logoutButton(){
        mAuth.signOut();
        CurrentUserModel.clearUser();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    //Disables the back button action
    @Override
    public void onBackPressed(){}

    //Gets the chat history from real time database
    public void getChatHistoryFromDatabase(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatHistory.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot chatBubble : dataSnapshot.getChildren()){
                    keys.add(chatBubble.getKey());
                    MessageModel messageModel = chatBubble.getValue(MessageModel.class);
                    chatHistory.add(messageModel);
                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
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
       myRef.push().setValue(chatBubbleInfo);
    }
}