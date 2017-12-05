package com.ucla.canu.lochat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import enums.EndpointsEnum;
import models.ChatMessage;
import models.Room;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class ChatRoom extends AppCompatActivity  implements DownloadCompleteListener, View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {

    private String chatRoomName;
    private String lat;
    private String lon;
    private String id;
    ListView list;
    private ArrayList<ChatMessage> chatMsgs = new ArrayList<>();
    MessageClassAdapter adapter;
    String lastUpdate = "1970-01-01T00:00Z";

    @Override public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                getMessages();
            }
        }, 5000);
    }

    @Override
    public void downloadComplete(String response, String error) {
    }

    @Override
    public void getMessagesComplete(String message, Integer statusCode) {
        ((SwipeRefreshLayout) findViewById(R.id.swiperefresh)).setRefreshing(false);
        System.out.println(message);
        if (statusCode == 200) {
            lastUpdate = getIso(true);
            getMsgsFromJson(message);
            /*for (ChatMessage msg : chatMsgs) {
                System.out.println(msg.getId());
                System.out.println(msg.getText());
            }*/
            list = (ListView)findViewById(R.id.listview);
            ArrayList<ChatMessage> chatMsgsAdapter = new ArrayList<>();
            adapter = new MessageClassAdapter(this, chatMsgsAdapter);
            list.setAdapter(adapter);
            createListView();
        }
        else if (statusCode == 401) {
            buildError("Please sign in again!");
            final Intent intent = new Intent(ChatRoom.this, LoginScreen.class);
            startActivity(intent);
        }
        else {
            buildError(message);
        }
    }

    @Override
    public void createRoomsComplete(String message, Integer statusCode) {
        if (statusCode == 201) {
            System.out.println(message);
        }
        else if (statusCode == 401) {
            buildError("Please sign in again!");
            final Intent intent = new Intent(ChatRoom.this, LoginScreen.class);
            startActivity(intent);
        }
        else {
            buildError(message);
        }
    }


    @Override
    public void createMessagesComplete(String message, Integer statusCode) {
        if (statusCode == 201) {
            getMessages();
        }
        else if (statusCode == 401) {
            buildError("Please sign in again!");
            final Intent intent = new Intent(ChatRoom.this, LoginScreen.class);
            startActivity(intent);
        }
        else {
            buildError(message);
        }
    }

    private void createListView()
    {
        for (ChatMessage msg : chatMsgs) {
            adapter.add(msg);
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
            {
            }
        });
        list.setSelection(adapter.getCount()-1);
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

    @Override
    public void getRoomsComplete(String message) {
        System.out.println(message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Intent intent = getIntent();
        chatRoomName = intent.getStringExtra(ListRoomsScreen.CHAT_ROOM_NAME);
        lat = intent.getStringExtra(ListRoomsScreen.LAT);
        lon = intent.getStringExtra(ListRoomsScreen.LON);
        id = intent.getStringExtra(ListRoomsScreen.ID);
        ((EditText)findViewById(R.id.enterMsg)).setBackgroundColor(Color.WHITE);
        (findViewById(R.id.title)).setBackgroundColor(Color.WHITE);
        //System.out.println(getMsgRequest());

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.title);
        textView.setText(chatRoomName);
        SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(this);
        //((Button) findViewById(R.id.send)).setOnClickListener(this);
        getMessages();

    }
    @Override
    public void onClick(View v) {
        ((EditText) findViewById(R.id.enterMsg)).setText("");
    }

    private ArrayList<ChatMessage> getMsgsFromJson(String json) {
        //ArrayList<ChatMessage> msgs = new ArrayList<>();
        try {
            JSONObject Jobject = new JSONObject(json);
            if (!Jobject.isNull("messages")) {
                JSONArray jArray = Jobject.getJSONArray("messages");
                int length = jArray.length();
                for (int i = 0; i < length; i++) {
                    JSONObject jsonObj = jArray.getJSONObject(i);
                    ChatMessage msg = utils.JsonToMessage.jsonToMessage(jsonObj); //TODO: Save room?
                    chatMsgs.add(msg);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getIso(boolean now) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String nowAsISO;
        if (now) {
            nowAsISO=df.format(new Date());
        }
        else {
            nowAsISO=df.format(new Date(0L));
        }
        return nowAsISO;
    }

    private String getMsgRequest() {
        String iso = lastUpdate;
        /*if (current == null) {
            iso = getIso(false);
        }
        else {
            iso = getIso(true);
        }*/
        return String.format(EndpointsEnum.GET_MESSAGES.getValue(),id,iso,lat,lon);
    }

    public void sendMessages(View view) {
        String message = ((EditText)findViewById(R.id.enterMsg)).getText().toString();
        ((EditText)findViewById(R.id.enterMsg)).setText("");
        if (message == null || message.equals("")) {
            buildError("Please enter a message!");
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
            jObject.put("text",message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),jObject.toString().getBytes());
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder().url(String.format(EndpointsEnum.SEND_MESSAGE.getValue(),id)).post(requestBody)
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
                    //if (statusCode != 201 && statusCode != 401) {
                        message = Jobject.get("message").toString();
                    //}
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String error = message;
                final Integer status = statusCode;
                ChatRoom.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        createMessagesComplete(error, status);  // 5
                    }
                });
            }
        });
    }

    private void getMessages() {
        MyApplication mApp = ((MyApplication)getApplicationContext());
        String token = mApp.getToken();
        System.out.println(token);
        //RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),jObject.toString().getBytes());
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder().url(getMsgRequest())
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
                String message = response.body().string();
                /*try {
                    JSONObject Jobject = new JSONObject(response.body().string());
                    if (response.code() == 200) {
                        token = Jobject.get("token").toString();
                        MyApplication mApp = ((MyApplication)getApplicationContext());
                        mApp.setToken(Jobject.get("token").toString());
                    }
                    else {
                        message = Jobject.get("message").toString();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String result = token;//response.body().//.string();  // 4*/
                final int statusCode = response.code();
                final String resp = message;
                ChatRoom.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //getRoomsComplete(resp);  // 5
                        getMessagesComplete(resp,statusCode);
                    }
                });
            }
        });
    }
}
