package com.example.publicchat.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.publicchat.Common.DbKeys;
import com.example.publicchat.Models.CurrentUserModel;
import com.example.publicchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity {

    private boolean canCreateNewUser;


    private String currentUsername;
    private String currentPassword;
    private String currentEmail;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private DatabaseReference myRefUsernames;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        myRef = mFirebaseDatabase.getReference().child(DbKeys.users);

        canCreateNewUser = true;

    }

    public void registerUser(View view) {
        EditText getUsername = (EditText) findViewById(R.id.registrationUsername);
        EditText getPassword = (EditText) findViewById(R.id.registrationPassword);
        EditText getPasswordConfirm = (EditText) findViewById(R.id.registrationPasswordConfirm);
        EditText getEmail = (EditText) findViewById(R.id.registrationEmail);

        this.currentUsername = getUsername.getText().toString();
        this.currentPassword = getPassword.getText().toString();
        this.currentEmail = getEmail.getText().toString();

        myRefUsernames = mFirebaseDatabase.getReference().child(DbKeys.usernames).child(currentUsername);

        if (!currentPassword.equals(getPasswordConfirm.getText().toString())) {
            Snackbar.make(findViewById(R.id.registrationUsername), "Passwords don't match", Snackbar.LENGTH_LONG).show();
        } else if (currentPassword.length() > 6 || currentUsername.length() > 3) {
            if (currentUsername.isEmpty() || currentPassword.isEmpty()) {
                Snackbar.make(findViewById(R.id.registrationUsername), "Please fill in all the information", Snackbar.LENGTH_LONG).show();
                canCreateNewUser = false;
            }
            else doesUsernameExist();
        } else {
            Snackbar.make(findViewById(R.id.registrationUsername), "Password must be longer than 6 characters", Snackbar.LENGTH_LONG).show();
        }

    }

    //Adds a new user to the list and starts the ChatRoomActivity with that UserModel
    private void createNewUser() {
        mAuth.createUserWithEmailAndPassword(currentEmail, currentPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Successful registration", Toast.LENGTH_SHORT).show();
                            CurrentUserModel.setInstance(currentUsername, mAuth.getUid());
                            updateUI();
                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getApplicationContext(), "Email already in use", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUI() {
        myRef.child(mAuth.getCurrentUser().getUid()).setValue(CurrentUserModel.getInstance()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(RegistrationActivity.this, ChatRoomActivity.class));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Successn't");
            }
        });

        myRefUsernames.setValue(true);
    }

    public void doesUsernameExist() {
        myRefUsernames.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                } else createNewUser();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Sum Ting Wong", Toast.LENGTH_LONG).show();
            }
        });
    }
}