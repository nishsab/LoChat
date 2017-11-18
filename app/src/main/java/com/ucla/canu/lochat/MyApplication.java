package com.ucla.canu.lochat;

import android.app.Application;

/**
 * Created by nishsab on 11/18/17.
 */

public class MyApplication extends Application {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
