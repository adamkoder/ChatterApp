package com.example.publicchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class LoadingScreen extends AppCompatActivity {

    public static final String TAG = "TAG";

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private FirebaseDatabase database;
    private DatabaseReference dbRefListOfUsers;
    private Gson gson = new Gson();

    private ArrayList<User> listOfIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        database = FirebaseDatabase.getInstance();
        dbRefListOfUsers = database.getReference("listOfUsers");

        getIdListFromDatabase();
        if(mPreferences.getString("currentUsername", "").equals("")){
            final Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(IntentKeys.LIST_OF_USERS, fromListToJSON(listOfIds));
            timer(intent);
        }

        else{
            final Intent intent = new Intent(this, ChatRoomActivity.class);
            intent.putExtra(IntentKeys.USER, mPreferences.getString("currentUsername", "not working!"));
            timer(intent);
        }
    }

    @Override
    public void onBackPressed(){}

    private void timer(final Intent intent){
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(intent);
            }
        }.start();
    }

    private void getIdListFromDatabase(){
        dbRefListOfUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listOfIds = dataSnapshot.getValue(ArrayList.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
    private String fromListToJSON(ArrayList list){
        return gson.toJson(list);
    }
}
