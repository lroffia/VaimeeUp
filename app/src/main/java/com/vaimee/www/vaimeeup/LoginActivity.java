package com.vaimee.www.vaimeeup;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;

import it.unibo.arces.wot.sepa.apps.chat.IUsersListener;
import it.unibo.arces.wot.sepa.apps.chat.UsersListener;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAPropertiesException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAProtocolException;
import it.unibo.arces.wot.sepa.pattern.ApplicationProfile;


public class LoginActivity  {

    private UsersListener usersListener;
    private ApplicationProfile appProfile;

    private ArrayList<String> users = new ArrayList<>();
    private String user = null;

    public static final String USER = "com.vaimee.www.vaimeeup.USER";

    public static ArrayAdapter<String> usersAdapter;

    protected void onCreate(Bundle savedInstanceState) {
       /*
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);

        users.add("Luca");
        users.add("Carlo");

        usersAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, users);
        //Spinner spinner = (Spinner) findViewById(R.id.spinnerUsers);
        usersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(usersAdapter);
        spinner.setOnItemSelectedListener(this);

        try {
            appProfile = new ApplicationProfile(getAssets().open("chat.jsap"));
        } catch (IOException | SEPAPropertiesException e) {
            Log.d("FATAL", "Failed to load JSAP");
            Snackbar.make(findViewById(R.id.loginLayout),"Failed to load JSAP",Snackbar.LENGTH_SHORT).show();
            return;
        }

        try {
            usersListener = new UsersListener(appProfile,this);
            new Thread() {
                public void run() {
                    while(!usersListener.joinChat()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    Snackbar.make(findViewById(R.id.loginLayout),"Joined!",Snackbar.LENGTH_SHORT).show();
                }
            }.run();
        } catch (SEPAProtocolException e) {
            Log.d("FATAL", "Failed to create users listener");
            return;
        }*/
    }

    public void onLogin(View view) {
        if (user == null) {
            Snackbar.make(view, R.string.selectUserWarning,
                    Snackbar.LENGTH_SHORT)
                    .show();

            return;
        }
        //Intent intent = new Intent(this, ChatMainActivity.class);
        users.remove(user);
        //intent.putExtra(USER,user);
        //startActivity(intent);
    }
}
