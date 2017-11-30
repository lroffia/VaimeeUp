package com.vaimee.www.vaimeeup;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.Snackbar;
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

public class MainActivity extends Activity implements IUsersListener, AdapterView.OnItemSelectedListener {
    private ArrayList<String> users = new ArrayList<>();

    public static ArrayAdapter<String> usersAdapter;
    public static final String USER_EXTRA = "com.vaimee.www.vaimeeup.USER_EXTRA";
    private String nickname = null;

    private Handler mHandler;
    private final int ADD_USERS_MESSAGE = 0;
    private final int REMOVE_USERS_MESSAGE = 1;
    private final int REMOVE_USER_MESSAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                switch (inputMessage.what) {
                    case ADD_USERS_MESSAGE:
                        Snackbar.make(findViewById(R.id.simpleLoginActivityLayout),"New nickname(s) available!", Snackbar.LENGTH_LONG).show();
                        ArrayList<String> newUsers =(ArrayList<String>)inputMessage.obj;
                        if (nickname != null) newUsers.remove(nickname);
                        users.addAll(newUsers);
                        usersAdapter.notifyDataSetChanged();
                        break;
                    case REMOVE_USERS_MESSAGE:
                        users.removeAll((ArrayList<String>)inputMessage.obj);
                        usersAdapter.notifyDataSetChanged();
                        break;
                    case REMOVE_USER_MESSAGE:
                        users.remove((String)inputMessage.obj);
                        usersAdapter.notifyDataSetChanged();
                        break;
                }
            }
        };

        usersAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, users);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(usersAdapter);
        spinner.setOnItemSelectedListener(this);

        try {
            ApplicationProfile appProfile = new ApplicationProfile(getAssets().open("chat.jsap"));
            final UsersListener usersListener = new UsersListener(appProfile,this);
            new Thread() {
                public void run() {
                    while(!usersListener.joinChat()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    Snackbar.make(findViewById(R.id.simpleLoginActivityLayout),"Listening for users",Snackbar.LENGTH_LONG).show();
                }
            }.run();
        } catch (IOException | SEPAPropertiesException | SEPAProtocolException e) {
            Log.d("FATAL", e.getMessage());
            Snackbar.make(findViewById(R.id.simpleLoginActivityLayout),e.getMessage(), Snackbar.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        nickname = usersAdapter.getItem(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d("debug","onNothingSelected");
    }

    @Override
    public void onAddedUsers(ArrayList<String> users) {
        Message message =
                mHandler.obtainMessage(ADD_USERS_MESSAGE, users);
        message.sendToTarget();
    }

    @Override
    public void onRemovedUsers(ArrayList<String> users) {
        Message message =
                mHandler.obtainMessage(REMOVE_USERS_MESSAGE, users);
        message.sendToTarget();
    }

    public void onLogin(View view) {
        if (nickname == null) {
            Snackbar.make(findViewById(R.id.simpleLoginActivityLayout),"Select a nickname first", Snackbar.LENGTH_LONG).show();
            return;
        }

        Message message =
                mHandler.obtainMessage(REMOVE_USER_MESSAGE, nickname);
        message.sendToTarget();

        Intent intent = new Intent(this, ChatMainActivity.class);
        intent.putExtra(USER_EXTRA,nickname);
        startActivity(intent);
    }
}
