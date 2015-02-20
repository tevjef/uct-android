package com.tevinjeffrey.rutgerssoc;

import android.util.Log;

import com.koushikdutta.ion.Ion;
import com.orm.SugarApp;
import com.splunk.mint.Mint;

@SuppressWarnings("ALL")
public class MyApplication extends SugarApp {
    @Override
    public void onCreate() {
        super.onCreate();
        Ion.getDefault(getApplicationContext()).configure().setLogging("Ion", Log.VERBOSE);
        Mint.initAndStartSession(getApplicationContext(), "2110a7f1");
        Mint.enableDebug();
        Mint.enableLogging(true);
        Mint.setLogging(400);
    }
}
