package com.tevinjeffrey.rutgersct;

import android.util.Log;

import com.koushikdutta.ion.Ion;
import com.orm.SugarApp;
import com.splunk.mint.Mint;

public class MyApplication extends SugarApp {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Ion.getDefault(getApplicationContext()).configure().setLogging("Ion", Log.VERBOSE);
            Mint.initAndStartSession(getApplicationContext(), "2110a7f1");
            Mint.enableDebug();
        } else {
            Mint.initAndStartSession(getApplicationContext(), "2974ff7f");
        }
    }
}
