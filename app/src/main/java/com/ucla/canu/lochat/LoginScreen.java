package com.ucla.canu.lochat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


public class LoginScreen extends AppCompatActivity  implements DownloadCompleteListener {

    public static final String EMAIL = "com.ucla.canu.lochat.EMAIL";
    public static final String PASSWORD = "com.ucla.canu.lochat.PASSWORD";

    @Override
    public void downloadComplete(String response, String error) {
        if (! response.equals("")) {
            final Intent intent = new Intent(this, HomeScreen.class);
            //intent.putExtra(EMAIL, "test");
            //intent.putExtra(PASSWORD, response);
            startActivity(intent);
        }
        else {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage(error);
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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
    }

    public void sendMessage(View view) {
        String EMAIL = ((EditText)findViewById(R.id.email)).getText().toString();
        String PASSWORD = ((EditText)findViewById(R.id.password)).getText().toString();

        makeRequestWithOkHttp("https://lochat.codyleyhan.com/api/v1/auth/login");

//        Intent intent = new Intent(this, ChatPage.class);
//        startActivity(intent);
    }

    public void createUser(View view) {
        makeRequestWithOkHttp("https://lochat.codyleyhan.com/api/v1/auth/register");
    }

    private void makeRequestWithOkHttp(String url) {
        JSONObject jObject = new JSONObject();
        try {
            jObject.put("email","xyz@gmail.com");
            jObject.put("password","xyzabc");
//            jObject.put("email",EMAIL);
//            jObject.put("password",PASSWORD);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),jObject.toString().getBytes());
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).post(requestBody).build();  // 2

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
                final String result = token;//response.body().//.string();  // 4
                final String error = message;
                LoginScreen.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downloadComplete(result,error);  // 5
                    }
                });
            }
        });
    }
}
