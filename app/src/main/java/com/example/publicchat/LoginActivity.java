package com.example.publicchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private User currentUser;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private CheckBox rememberCheckBox;

    private Gson gson = new Gson();
    private ArrayList<User> listOfIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rememberCheckBox = (CheckBox) findViewById(R.id.rememberCheckBox);

        Intent intent = getIntent();
        this.listOfIds = gson.fromJson(intent.getStringExtra(IntentKeys.LIST_OF_USERS), new TypeToken<ArrayList<User>>(){}.getType());

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        getIdListFromSharedPrefs();
    }

    /*
        Method thats called by the ENTER CHAT ROOM button
        It starts the Chat room activity and sends it the entered username
    */
    public void enterChat(View view){
        //Gets the username typed in by the user
        EditText getUsername = (EditText) findViewById(R.id.login_username);
        EditText getPassword = (EditText) findViewById(R.id.login_password);

        boolean canShow = true;

        if(getUsername.getText().toString().length() > 2) {
            for(int i = 0; i < listOfIds.size(); i++){
                if(listOfIds.get(i).getUsername().equals(getUsername.getText().toString())){
                    if(listOfIds.get(i).getPassword().equals(getPassword.getText().toString())){
                    currentUser = listOfIds.get(i);

                    Intent intent = new Intent(this, ChatRoomActivity.class);

                    if(this.rememberCheckBox.isChecked())
                        mEditor.putString("currentUsername", fromObjToString(currentUser)).apply();

                    intent.putExtra(IntentKeys.USER, fromObjToString(currentUser));
                    startActivity(intent);
                    }
                    else{
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.enterRoomButton),"Invalid password", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        canShow = true;
                        break;
                    }
                }
            }

            if(currentUser == null && canShow){
                Snackbar snackbar = Snackbar.make(findViewById(R.id.enterRoomButton),"Username not found, please register", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
        else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.enterRoomButton),"Enter a name with 3 or more characters", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    public void toRegistrationActivity(View view){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){}

    //adds the list of all user to the class list from the listOfIds key
    private void getIdListFromSharedPrefs(){
        String json = mPreferences.getString("listOfIds","");
        listOfIds = gson.fromJson(json,new TypeToken<ArrayList<User>>(){}.getType());
    }

    //Converts Objects to strings
    private String fromObjToString(User userObj){
        return gson.toJson(userObj);
    }

    //Converts List to JSON
    private String fromListToJSON(ArrayList list){
        return gson.toJson(list);
    }
}