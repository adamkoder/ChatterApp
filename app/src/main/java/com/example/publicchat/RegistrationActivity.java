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
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class RegistrationActivity extends AppCompatActivity {

    private ArrayList<User> listOfIds = new ArrayList<>();
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

        getIdListFromSharedPrefs();
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

            else if (listOfIds == null) {
                listOfIds = new ArrayList<>();
                listOfIds.add(new User(Integer.toString(listOfIds.size() + 1), currentUserName, currentPassword));
                canCreateNewUser = false;
                createNewUser();
            }

            else {
                for (int i = 0; i < listOfIds.size(); i++) {
                    if (listOfIds.get(i).getUsername().toLowerCase().equals(currentUserName.toLowerCase())) {
                        ((EditText) findViewById(R.id.registrationUsername)).setText("");
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.registrationUsername), "Username already exists", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        canCreateNewUser = false;
                        break;
                    }
                }
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
        listOfIds.add(new User(Integer.toString(listOfIds.size() + 1), currentUserName, currentPassword));

        mAuth.createUserWithEmailAndPassword(currentEmail, currentPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Successful registration", Toast.LENGTH_LONG).show();
                            updateUI();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Workn't", Toast.LENGTH_LONG).show();
                            System.out.println("PA NE RADI");
                        }
                    }
                });
    }

    private void updateUI(){
        Intent intent = new Intent(this, ChatRoomActivity.class);
        intent.putExtra(IntentKeys.USER, fromObjToString(listOfIds.get(listOfIds.size() - 1)));
        mEditor.putString("currentUsername", fromObjToString(listOfIds.get(listOfIds.size() - 1)));
        setIdListToSharedPrefs();
        startActivity(intent);
    }

    //Converts User objects to JSON
    private String fromObjToString(User userObj){
        return gson.toJson(userObj);
    }

    //Sets the current list of users to Shared Preferences under the key listOfIds
    private void setIdListToSharedPrefs(){
        String json = gson.toJson(listOfIds);
        mEditor.putString("listOfIds", json).apply();
    }

    private void getIdListFromSharedPrefs(){
        String json = mPreferences.getString("listOfIds","");
        listOfIds = gson.fromJson(json,new TypeToken<ArrayList<User>>(){}.getType());
    }
}