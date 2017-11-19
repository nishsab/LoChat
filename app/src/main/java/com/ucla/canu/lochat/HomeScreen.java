package com.ucla.canu.lochat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String token = ((MyApplication)getApplicationContext()).getToken();
        //String email = intent.getStringExtra(LoginScreen.EMAIL);
        //String password = intent.getStringExtra(LoginScreen.PASSWORD);
        String message = String.format("%s",token);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView);
        textView.setText(message);
    }

    public  String  GetText(JSONObject jObj) throws IOException {
        String query = "https://example.com";

        URL url = new URL(query);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");

        OutputStream os = conn.getOutputStream();
        os.write(jObj.toString().getBytes());
        os.close();

        // read the response
        InputStream in = new BufferedInputStream(conn.getInputStream());
        String result = in.toString();
        return result;
    }
}
