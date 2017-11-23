package com.vaimee.www.vaimeeup;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.unibo.arces.wot.sepa.apps.chat.Message;

/**
 * Created by luca on 27/10/17.
 */
public class MessageArrayAdapter extends ArrayAdapter{
    private Context context;
    private ArrayList<Message> messages;

    public MessageArrayAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);

        this.context = context;
        this.messages = (ArrayList<Message>) objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View messageView = inflater.inflate(R.layout.message, parent, false);

        TextView time = (TextView) messageView.findViewById(R.id.messageTime);
        TextView name = (TextView) messageView.findViewById(R.id.messageName);
        TextView text = (TextView) messageView.findViewById(R.id.messageText);
        CheckBox sent = (CheckBox) messageView.findViewById(R.id.messageSent);
        CheckBox read = (CheckBox) messageView.findViewById(R.id.messageRead);

        Message msg = messages.get(position);

        text.setText(msg.getText());
        time.setText(msg.getTime());

        int gravity = Gravity.RIGHT;

        if (msg.isReceived()) {
            name.setText(msg.getFrom());
            name.setTextColor(Color.parseColor("#4E94CF"));
            gravity = Gravity.LEFT;

            sent.setVisibility(View.INVISIBLE);
            read.setVisibility(View.INVISIBLE);

        }
        else {
            name.setText(msg.getTo());
            name.setTextColor(Color.parseColor("#698491"));
            if (msg.isRead()) {
                sent.setVisibility(View.VISIBLE);
                read.setVisibility(View.VISIBLE);
                sent.setChecked(true);
                read.setChecked(true);
            }
            else if (msg.isSent()) {
                sent.setVisibility(View.VISIBLE);
                read.setVisibility(View.INVISIBLE);
                sent.setChecked(true);
            }
        }

        name.setGravity(gravity);
        text.setGravity(gravity);
        ((LinearLayout) messageView.findViewById(R.id.timeLayout)).setGravity(gravity);

        return messageView;
    }
}
