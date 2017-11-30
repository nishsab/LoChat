package com.ucla.canu.lochat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import android.location.LocationListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import enums.DownloadCompleteEnum;
import enums.EndpointsEnum;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


public class LoginScreen extends AppCompatActivity implements DownloadCompleteListener {

    public static final String EMAIL = "com.ucla.canu.lochat.EMAIL";
    public static final String PASSWORD = "com.ucla.canu.lochat.PASSWORD";
    public static final String LAT = "com.ucla.canu.lochat.LAT";
    public static final String LON = "com.ucla.canu.lochat.LON";
    public static final String ROOMS_LIST = "com.ucla.canu.lochat.ROOMS_LIST";
    private String lat;
    private String lon;

    private FusedLocationProviderClient mFusedLocationClient;
    LocationManager locationManager = null;

    @Override
    public void downloadComplete(String response, String error) {
        if (!response.equals("")) {
                /*final Intent intent = new Intent(this, HomeScreen.class);
                //intent.putExtra(EMAIL, "test");
                intent.putExtra(LAT, this.lat);
                intent.putExtra(LON, this.lon);
                startActivity(intent);*/
            //getRooms(response);
            getLatLon(response);
        } else {
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
    public void getRoomsComplete(String message) {
        //final Intent intent = new Intent(this, HomeScreen.class);
        final Intent intent = new Intent(this, ListRoomsScreen.class);
        //intent.putExtra(EMAIL, "test");
        intent.putExtra(ROOMS_LIST, message);
        intent.putExtra(LAT, this.lat);
        intent.putExtra(LON, this.lon);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
    }

    public void getLatLon(final String message) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //lat = "34.068921";
        //lon = "-118.445181";
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.

                        if (location == null) {
                            String locationProvider = LocationManager.NETWORK_PROVIDER;
// Or use LocationManager.GPS_PROVIDER

                            if (ActivityCompat.checkSelfPermission(LoginScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoginScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
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
                        getRooms(message);
                    }
                });
    }

    public void sendMessage(View view) {
        String EMAIL = ((EditText)findViewById(R.id.email)).getText().toString();
        String PASSWORD = ((EditText)findViewById(R.id.password)).getText().toString();

        makeRequestWithOkHttp(EndpointsEnum.LOGIN.getValue());
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

    private void getRooms(String token) {
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
                LoginScreen.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getRoomsComplete(error);  // 5
                    }
                });
            }
        });
    }
}
