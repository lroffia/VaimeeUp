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

    public void messageRead(Message msg) {
        synchronized (messages) {
            msg.setRead();
            msg.setSent();
            if (messages.contains(msg)) {
                int index = messages.indexOf(msg);
                messages.get(index).setRead();
            }
            else messages.add(msg);
            post(new Runnable() {
                public void run() {
                    adapter.notifyDataSetChanged();
                    ((ListView) activity.findViewById(R.id.messagesList)).setSelection(adapter.getCount() - 1);
                }
            });
            Log.d("READ", msg.toString());
        }
    }

    public void messageReceived(Message msg) {
        synchronized (messages) {
            msg.setReceived();
            messages.add(msg);
            post(new Runnable() {
                public void run() {
                    adapter.notifyDataSetChanged();
                    ((ListView) activity.findViewById(R.id.messagesList)).setSelection(adapter.getCount() - 1);
                }
            });
            Log.d("RECEIVED", msg.toString());
        }
    }

    public void brokenConnection(){
        post(new Runnable() {
            public void run() {
                activity.findViewById(R.id.sendButton).setVisibility(View.INVISIBLE);
            }
        });
    }

    public void messageSent(Message msg) {
        synchronized (messages) {
            msg.setSent();
            messages.add(msg);
            post(new Runnable() {
                public void run() {
                    adapter.notifyDataSetChanged();
                    ((ListView) activity.findViewById(R.id.messagesList)).setSelection(adapter.getCount() - 1);
                }
            });
            Log.d("SENT", msg.toString());
        }
    }

    public void joined() {
        post(new Runnable() {
            public void run() {
                activity.findViewById(R.id.sendButton).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.chatButton).setVisibility(View.INVISIBLE);
                activity.findViewById(R.id.sender).setEnabled(false);
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
