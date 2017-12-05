package com.ucla.canu.lochat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import models.ChatMessage;

/**
 * Created by nishsab on 12/2/17.
 */

public class MessageClassAdapter extends ArrayAdapter<ChatMessage> {
    public MessageClassAdapter(Context context, ArrayList<ChatMessage> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }
        TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
        TextView text2 = (TextView) convertView.findViewById(android.R.id.text2);
        ChatMessage msg = getItem(position);
        text1.setText(msg.getUserEmail());
        text2.setText(msg.getText());

        return convertView;
    }
}
