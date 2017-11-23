package com.vaimee.www.vaimeeup;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;

import it.unibo.arces.wot.sepa.apps.chat.BasicChatClient;
import it.unibo.arces.wot.sepa.apps.chat.Message;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAPropertiesException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAProtocolException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;
import it.unibo.arces.wot.sepa.pattern.ApplicationProfile;

public class ChatMainActivity extends Activity implements OnItemSelectedListener {
    // The application
    private BasicChatClient client;
    private ApplicationProfile appProfile;
    private boolean joined = false;

    // Sender and receiver
    private String sender = "Luca";
    private String receiver = "Nino";

    // Users
    private ArrayList<String> users = new ArrayList<>();

    // Messages
    private ArrayList<Message> messages = new ArrayList<>();
    private MessageHandler messageHandler;
    private MessageArrayAdapter adapter;

    Spinner spinnerR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        adapter = new MessageArrayAdapter(this, R.layout.message, messages);
        ((ListView) findViewById(R.id.messagesList)).setAdapter(adapter);
        messageHandler = new MessageHandler(adapter,messages,this);

        try {
            appProfile = new ApplicationProfile(getAssets().open("chat.jsap"));
        } catch (IOException | SEPAPropertiesException e) {
            Log.d("FATAL", "Failed to load JSAP");
            return;
        }

        findViewById(R.id.message).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((EditText) findViewById(R.id.message)).setText("");
            }
        });

        findViewById(R.id.sendButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        if (client.sendMessage(receiver, ((EditText) findViewById(R.id.message)).getText().toString()))
                            messageHandler.sent();
                    }
                }.start();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.users, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerR = (Spinner) findViewById(R.id.receiver);
        spinnerR.setAdapter(adapter);
        spinnerR.setOnItemSelectedListener(this);

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        if (client.sendMessage(receiver, ((EditText) findViewById(R.id.message)).getText().toString()))
                            messageHandler.sent();
                    }
                }.start();
            }
        });

        joinTheChat();
    }

        private void joinTheChat() {
        new Thread() {
            @Override
            public void run() {
                try {
                    //Join
                    if (client == null)
                        client = new BasicChatClient(sender, appProfile) {
                            @Override
                            public void onMessageRead(Message msg) {
                                messageHandler.messageRead(msg);
                            }

                            @Override
                            public void onMessageReceived(Message msg) {
                                messageHandler.messageReceived(msg);
                            }

                            @Override
                            public void onMessageSent(final Message msg) {
                                messageHandler.messageSent(msg);
                            }

                            @Override
                            public void onBrokenConnection() {
                                messageHandler.brokenConnection();
                                joinTheChat();
                            }

                            @Override
                            public void onAddedUsers(ArrayList<String> added) {
                                users.addAll(added);
                            }

                            @Override
                            public void onRemovedUsers(ArrayList<String> removed) {
                                users.removeAll(removed);
                            }
                        };
                    while (!joined) {
                        joined = client.joinChat();
                        Thread.sleep(1000);
                    }
                    if (joined) {
                        messageHandler.joined();
                    }
                } catch (SEPAProtocolException | SEPASecurityException | SEPAPropertiesException | InterruptedException e) {
                    Log.e("EXCEPTION", e.getMessage());
                } finally {

                }
            }
        }.start();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        receiver = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
