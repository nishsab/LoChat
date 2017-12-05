package com.ucla.canu.lochat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import enums.EndpointsEnum;
import models.Room;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class ListRoomsScreen extends AppCompatActivity implements DownloadCompleteListener,SwipeRefreshLayout.OnRefreshListener {

    public static final String CHAT_ROOM_NAME = "com.ucla.canu.lochat.CHAT_ROOM_NAME";
    public static final String LAT = "com.ucla.canu.lochat.LAT";
    public static final String LON = "com.ucla.canu.lochat.LON";
    public static final String ID = "com.ucla.canu.lochat.ID";

    ListView list;
    private ArrayList<Room> Rooms;
    RoomClassAdapter adapter;
    String lat;
    String lon;
    private FusedLocationProviderClient mFusedLocationClient;
    LocationManager locationManager = null;
    String token;

    @Override
    public void downloadComplete(String response, String error) {
    }

    @Override
    public void getMessagesComplete(String message, Integer statusCode) {
    }
    @Override
    public void createMessagesComplete(String message, Integer statusCode) {
    }

    @Override
    public void getRoomsComplete(String message) {
        Rooms =new ArrayList<Room>();
        list = (ListView)findViewById(R.id.listview);
        adapter = new RoomClassAdapter(this, Rooms);
        list.setAdapter(adapter);
        System.out.println(String.format("Lat: %s Lon: %s",lat,lon));
        String jsonRooms = message;

        CreateListView(jsonRooms);
    }

    @Override
    public void createRoomsComplete(String message, Integer statusCode) {
        if (statusCode == 201) {
            final Intent intent = new Intent(this, ListRoomsScreen.class);
            startActivity(intent);
        }
        else if (statusCode == 401) {
            buildError("Please sign in again!");
            final Intent intent = new Intent(ListRoomsScreen.this, LoginScreen.class);
            startActivity(intent);
        }
        else {
            buildError(message);
        }
    }

    @Override public void onRefresh() {
        final Intent intent = new Intent(this, ListRoomsScreen.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rooms_screen);
        ((EditText)findViewById(R.id.chatName)).setBackgroundColor(Color.WHITE);
        ((EditText)findViewById(R.id.radius)).setBackgroundColor(Color.WHITE);

        SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(this);


        token = ((MyApplication)getApplicationContext()).getToken();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }

                    @Override
                    public void onLocationChanged(final Location location) {
                    }
                });
        getLatLon();
        //getRooms();



        /*Rooms =new ArrayList<Room>();
        list = (ListView)findViewById(R.id.listview);
        adapter = new RoomClassAdapter(this, Rooms);
        list.setAdapter(adapter);
        Intent intent = getIntent();
        lat = intent.getStringExtra(LoginScreen.LAT);
        lon = intent.getStringExtra(LoginScreen.LON);
        System.out.println(String.format("Lat: %s Lon: %s",lat,lon));
        String jsonRooms = intent.getStringExtra(LoginScreen.ROOMS_LIST);

        CreateListView(jsonRooms);*/
    }

    public void getLatLon() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            String locationProvider = LocationManager.NETWORK_PROVIDER;
                            if (ActivityCompat.checkSelfPermission(ListRoomsScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ListRoomsScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            locationManager.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                                        @Override
                                        public void onStatusChanged(String provider, int status, Bundle extras) {
                                        }

                                        @Override
                                        public void onProviderEnabled(String provider) {
                                        }

                                        @Override
                                        public void onProviderDisabled(String provider) {
                                        }

                                        @Override
                                        public void onLocationChanged(final Location location) {
                                        }
                                    });
                            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
                            if (lastKnownLocation == null) {
                                lat = "34.068921";
                                lon = "-118.445181";
                            }
                            else {
                                lat = String.valueOf(lastKnownLocation.getLatitude());
                                lon = String.valueOf(lastKnownLocation.getLongitude());
                            }
                        } else {
                            lat = String.format("%f",location.getLatitude());
                            lon = String.format("%f",location.getLongitude());
                        }
                        getRooms();
                    }
                });
    }

    private void getRooms() {
        JSONObject jObject = new JSONObject();
        JSONObject coords = new JSONObject();
        try {
            coords.put("lat",Double.parseDouble(this.lat));
            coords.put("lon",Double.parseDouble(this.lon));
            jObject.put("coordinates",coords);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),jObject.toString().getBytes());
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder().url(EndpointsEnum.SEARCH_ROOMS.getValue()).post(requestBody)
                .addHeader("authorization",String.format("Bearer %s",token))
                .build();  // 2

        client.newCall(request).enqueue(new okhttp3.Callback() { // 3
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response)
                    throws IOException {
                String token = "";
                String message = "";
                try {
                    JSONObject Jobject = new JSONObject(response.body().string());
                    if (response.code() == 200) {
                        message = Jobject.toString();
                        //token = Jobject.get("token").toString();
                        //MyApplication mApp = ((MyApplication)getApplicationContext());
                        //mApp.setToken(Jobject.get("token").toString());
                    }
                    else {
                        message = Jobject.get("message").toString();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String result = token;//response.body().//.string();  // 4
                final String error = message;
                ListRoomsScreen.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getRoomsComplete(error);  // 5
                    }
                });
            }
        });
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
                Room room = (Room) listItem;
                System.out.println("ISO");
                System.out.println(room.getCreated_at());
                System.out.println(room.getId());
                final Intent intent = new Intent(ListRoomsScreen.this, ChatRoom.class);
                intent.putExtra(CHAT_ROOM_NAME, room.getName());
                intent.putExtra(LAT, String.valueOf(room.getLat()));
                intent.putExtra(LON, String.valueOf(room.getLon()));
                intent.putExtra(ID, String.valueOf(room.getId()));
                startActivity(intent);
                //final Intent intent = new Intent(ListRoomsScreen.this, LoginScreen.class);
                //intent.putExtra(EMAIL, "test");
                //startActivity(intent);
            }
        });
    }

    public void signOut(View view) {
        final Intent intent = new Intent(ListRoomsScreen.this, LoginScreen.class);
        startActivity(intent);
    }

    public void buildError(String message) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle("Error!");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();

        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
    }

    public void createNewRoom(View view) {
        String radius = ((EditText)findViewById(R.id.radius)).getText().toString();
        String name = ((EditText)findViewById(R.id.chatName)).getText().toString();
        System.out.println(String.format("%s:%s",radius,name));
        if (radius == null || radius.isEmpty()) {
            buildError("Please enter a radius!");
            return;
        }
        if (name == null || name.isEmpty()) {
            buildError("Please enter a chat room name!");
            return;
        }
        MyApplication mApp = ((MyApplication)getApplicationContext());
        String token = mApp.getToken();
        String lat = this.lat;
        String lon = this.lon;
        JSONObject jObject = new JSONObject();
        JSONObject coords = new JSONObject();
        try {
            coords.put("lat",Double.parseDouble(lat));
            coords.put("lon",Double.parseDouble(lon));
            jObject.put("coordinates",coords);
            jObject.put("name",name);
            jObject.put("radius",Double.parseDouble(radius));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),jObject.toString().getBytes());
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder().url(EndpointsEnum.CREATE_ROOM.getValue()).post(requestBody)
                .addHeader("authorization",String.format("Bearer %s",token))
                .build();  // 2

        client.newCall(request).enqueue(new okhttp3.Callback() { // 3
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response)
                    throws IOException {
                String message = "";
                int statusCode = -1;
                try {
                    JSONObject Jobject = new JSONObject(response.body().string());
                    System.out.println(response.code());
                    statusCode = response.code();
                    if (statusCode != 201 && statusCode != 401) {
                        message = Jobject.get("message").toString();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String error = message;
                final Integer status = statusCode;
                ListRoomsScreen.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        createRoomsComplete(error, status);  // 5
                    }
                });
            }
        });
    }
}
