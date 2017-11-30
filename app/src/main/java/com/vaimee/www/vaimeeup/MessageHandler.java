package com.vaimee.www.vaimeeup;

import android.app.Activity;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import it.unibo.arces.wot.sepa.apps.chat.Message;

/**
 * Created by luca on 27/10/17.
 */
public class MessageHandler extends Handler {
    private MessageArrayAdapter adapter;
    private ArrayList<Message> messages;
    private Activity activity;

    public MessageHandler(MessageArrayAdapter adapter,ArrayList<Message> messages,Activity activity){
        this.adapter = adapter;
        this.messages = messages;
        this.activity = activity;
    }

    private void onMessage(Message msg) {
        synchronized (messages) {
            if (msg.isReceived() || msg.isSent()) messages.add(msg);
            else {
                for (int i=messages.size()-1; i >= 0;i--) {
                    if (messages.get(i).toString().equals(msg.toString())) {
                        messages.get(i).setRead();
                        break;
                    }
                }
            }
            post(new Runnable() {
                public void run() {
                    adapter.notifyDataSetChanged();
                    ((ListView) activity.findViewById(R.id.messagesList)).setSelection(adapter.getCount() - 1);
                }
            });
        }
    }

    public void messageRead(Message msg) {
        msg.setRead();
        onMessage(msg);
    }

    public void messageReceived(Message msg) {
        msg.setReceived();
        onMessage(msg);
    }

    public void brokenConnection(){
        post(new Runnable() {
            public void run() {
                activity.findViewById(R.id.sendButton).setVisibility(View.INVISIBLE);
            }
        });
    }

    public void messageSent(Message msg) {
        msg.setSent();
       onMessage(msg);
    }

    public void joined() {
        post(new Runnable() {
            public void run() {
                activity.findViewById(R.id.sendButton).setVisibility(View.VISIBLE);
            }
        });
    }

    public void leave() {
        post(new Runnable() {
            public void run() {
                activity.findViewById(R.id.sendButton).setVisibility(View.INVISIBLE);
            }
        });
    }

    public void sent() {
        post(new Runnable() {
            public void run() {
                Toast.makeText(activity.getApplicationContext(), "Sent", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
