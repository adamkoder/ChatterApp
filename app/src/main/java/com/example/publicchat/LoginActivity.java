package com.example.publicchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private CheckBox rememberCheckBox;

    EditText getEmail, getPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rememberCheckBox = (CheckBox) findViewById(R.id.rememberCheckBox);

        mAuth = FirebaseAuth.getInstance();

        getEmail = (EditText) findViewById(R.id.login_username);
        getPassword = (EditText) findViewById(R.id.login_password);

        Intent intent = getIntent();

    }

    /*
        Method thats called by the ENTER CHAT ROOM button
        It starts the Chat room activity and sends it the entered username
    */
    public void enterChat(View view){

        String email = getEmail.getText().toString().trim();
        String password = getPassword.getText().toString().trim();

        if(getEmail.getText().toString().isEmpty()){
            Snackbar.make(findViewById(R.id.login_username), "Please enter a valid email address", Snackbar.LENGTH_SHORT).show();
        }
        else if(getPassword.getText().toString().isEmpty()){
            Snackbar.make(findViewById(R.id.login_password), "Please enter a valid password", Snackbar.LENGTH_SHORT).show();
        }
        else if(getPassword.getText().toString().length() < 6){
            Snackbar.make(findViewById(R.id.login_password), "Password must be longer than 6 characters", Snackbar.LENGTH_SHORT).show();
        }
        else{
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        System.out.println(getEmail.getText().toString());
                        User.getInstance(getEmail.getText().toString());
                        startActivity(new Intent(LoginActivity.this, ChatRoomActivity.class));
                    }else{
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void toRegistrationActivity(View view){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){}
}