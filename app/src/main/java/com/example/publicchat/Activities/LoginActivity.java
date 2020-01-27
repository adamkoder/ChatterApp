package com.example.publicchat.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.publicchat.Common.DbKeys;
import com.example.publicchat.Models.CurrentUserModel;
import com.example.publicchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private CheckBox checkBox;

    EditText getEmail, getPassword;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkBox = (CheckBox) findViewById(R.id.rememberCheckBox);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child(DbKeys.users);

        getEmail = (EditText) findViewById(R.id.login_username);
        getPassword = (EditText) findViewById(R.id.login_password);
    }


    /**
     *  Method thats called by the ENTER CHAT ROOM button
     *   It starts the Chat room activity and sends it the entered username
     */
    public void enterChat(View view) {

        String email = getEmail.getText().toString().trim();
        String password = getPassword.getText().toString().trim();

        if (getEmail.getText().toString().isEmpty()) {
            Snackbar.make(findViewById(R.id.login_username), "Please enter a valid email address", Snackbar.LENGTH_SHORT).show();
        } else if (getPassword.getText().toString().isEmpty()) {
            Snackbar.make(findViewById(R.id.login_password), "Please enter a valid password", Snackbar.LENGTH_SHORT).show();
        } else if (getPassword.getText().toString().length() < 6) {
            Snackbar.make(findViewById(R.id.login_password), "Password must be longer than 6 characters", Snackbar.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        getUsernameFromDb();
                        startActivity(new Intent(LoginActivity.this, ChatRoomActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "UserModel not found", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    public void getUsernameFromDb() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot i : dataSnapshot.getChildren()) {
                    if (mAuth.getCurrentUser().getUid().equals(i.getKey())) {
                        String user = i.child(DbKeys.username).getValue().toString();
                        CurrentUserModel.setInstance(user, i.getKey());
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

    public void toRegistrationActivity(View view) {
        startActivity(new Intent(this, RegistrationActivity.class));
    }

    @Override
    public void onBackPressed() {
    }
}