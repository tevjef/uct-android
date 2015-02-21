package com.tevinjeffrey.rutgersct;

import com.orm.SugarApp;
import com.splunk.mint.Mint;

public class MyApplication extends SugarApp {
    @Override
    public void onCreate() {
        super.onCreate();
        //Ion.getDefault(getApplicationContext()).configure().setLogging("Ion", Log.VERBOSE);
        if (BuildConfig.DEBUG) {
            Mint.initAndStartSession(getApplicationContext(), "2110a7f1");
        } else {
            Mint.initAndStartSession(getApplicationContext(), "2974ff7f");
        }
        Mint.enableDebug();
        Mint.enableLogging(true);
        Mint.setLogging(400);
    }
}
