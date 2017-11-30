package com.ucla.canu.lochat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import models.Room;

public class ListRoomsScreen extends AppCompatActivity {

    ListView list;
    private ArrayList<Room> Rooms;
    RoomClassAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rooms_screen);
        Rooms =new ArrayList<Room>();
        list = (ListView)findViewById(R.id.listview);
        adapter = new RoomClassAdapter(this, Rooms);
        list.setAdapter(adapter);
        Intent intent = getIntent();
        String lat = intent.getStringExtra(LoginScreen.LAT);
        String lon = intent.getStringExtra(LoginScreen.LON);
        System.out.println(String.format("Lat: %s Lon: %s",lat,lon));
        String jsonRooms = intent.getStringExtra(LoginScreen.ROOMS_LIST);

        CreateListView(jsonRooms);
    }

    private void CreateListView(String jsonRooms)
    {
       try {
            JSONObject Jobject = new JSONObject(jsonRooms);
            if (!Jobject.isNull("rooms")) {
                JSONArray jArray = Jobject.getJSONArray("rooms");
                int length = jArray.length();
                for (int i = 0; i < length; i++) {
                    JSONObject jsonObj = jArray.getJSONObject(i);
                    Room room = utils.JsonToRoom.jsonToRoom(jsonObj); //TODO: Save room?
                    adapter.add(room);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        list.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
            {
                //TODO: Set links to chat rooms here
                //arg2 is the index in the list
                Object listItem = list.getItemAtPosition(arg2);
                final Intent intent = new Intent(ListRoomsScreen.this, LoginScreen.class);
                //intent.putExtra(EMAIL, "test");
                startActivity(intent);
            }
        });
    }

    public void signOut(View view) {
        final Intent intent = new Intent(ListRoomsScreen.this, LoginScreen.class);
        startActivity(intent);
    }
}
