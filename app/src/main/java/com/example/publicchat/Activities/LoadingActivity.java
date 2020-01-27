package com.example.publicchat.Activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.publicchat.Common.DbKeys;
import com.example.publicchat.Models.CurrentUserModel;
import com.example.publicchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoadingActivity extends AppCompatActivity {

    public static final String TAG = "TAG";

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child(DbKeys.users);

        if (mAuth.getCurrentUser() != null) {
            getUsernameFromDb();
            timer(new Intent(this, ChatRoomActivity.class));
        } else {
            timer(new Intent(this, LoginActivity.class));
        }

    }

    @Override
    public void onBackPressed() {
    }


    private void timer(final Intent intent) {
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

    public void getUsernameFromDb() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot i : dataSnapshot.getChildren()) {
                    if (mAuth.getCurrentUser().getUid().equals(i.getKey())) {
                        CurrentUserModel.setInstance(i.child(DbKeys.username).getValue().toString(), i.getKey());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Sum Ting Wong", Toast.LENGTH_LONG).show();
            }
        });
    }
}
