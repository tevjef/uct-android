package com.tevinjeffrey.rutgersct;

import android.util.Log;

import com.koushikdutta.ion.Ion;
import com.orm.SugarApp;

public class MyApplication extends SugarApp {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Ion.getDefault(getApplicationContext()).configure().setLogging("Ion", Log.VERBOSE);


        } else {

        }
    }
}
