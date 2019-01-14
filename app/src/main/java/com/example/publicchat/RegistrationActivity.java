package com.example.publicchat;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class RegistrationActivity extends AppCompatActivity {

    private Gson gson = new Gson();
    private boolean canCreateNewUser;

    private FirebaseAuth mAuth;

    private String currentUserName;
    private String currentPassword;
    private String currentEmail;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        mAuth = FirebaseAuth.getInstance();

        canCreateNewUser = true;

    }

    public void registerUser(View view){
        EditText getUsername = (EditText) findViewById(R.id.registrationUsername);
        EditText getPassword = (EditText) findViewById(R.id.registrationPassword);
        EditText getPasswordConfirm = (EditText) findViewById(R.id.registrationPasswordConfirm);
        EditText getEmail = (EditText) findViewById(R.id.registrationEmail);

        this.currentUserName = getUsername.getText().toString();
        this.currentPassword = getPassword.getText().toString();
        this.currentEmail = getEmail.getText().toString();

        if(!currentPassword.equals(getPasswordConfirm.getText().toString()))
        {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.registrationUsername), "Passwords don't match", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else if(currentPassword.length() > 6 || currentUserName.length() > 3) {
            if (currentUserName.isEmpty() || currentPassword.isEmpty()) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.registrationUsername), "Please fill in all the information", Snackbar.LENGTH_LONG);
                snackbar.show();
                canCreateNewUser = false;
            }
            if (canCreateNewUser) {
                System.out.println("PA JESAM!");
                createNewUser();
            }
        }
        else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.registrationUsername), "Password must be longer than 6 characters", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }

    //Adds a new user to the list and starts the ChatRoomActivity with that User
    private void createNewUser(){
        mAuth.createUserWithEmailAndPassword(currentEmail, currentPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Successful registration", Toast.LENGTH_LONG).show();
                            updateUI();
                        }
                        else{
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(getApplicationContext(), "Email already in use", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void updateUI(){
        startActivity(new Intent(this, ChatRoomActivity.class));
    }
}