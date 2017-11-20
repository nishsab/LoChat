package com.ucla.canu.lochat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import models.Room;

/**
 * Created by nishsab on 11/19/17.
 */

public class RoomClassAdapter extends ArrayAdapter<Room> {
    public RoomClassAdapter(Context context, ArrayList<Room> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }
        TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
        TextView text2 = (TextView) convertView.findViewById(android.R.id.text2);
                Room room = getItem(position);
        text1.setText(room.getName());
        text2.setText(String.format("(%f,%f) Radius: %.2f",room.getLat(),room.getLon(),room.getRadius()));

        return convertView;
    }
}
