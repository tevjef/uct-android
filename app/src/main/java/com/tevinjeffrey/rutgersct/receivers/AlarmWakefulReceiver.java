package com.tevinjeffrey.rutgersct.receivers;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.tevinjeffrey.rutgersct.services.RequestService;

public class AlarmWakefulReceiver extends WakefulBroadcastReceiver {
    public AlarmWakefulReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // This is the Intent to deliver to our service.
        Intent service = new Intent(context, RequestService.class);

        // Start the service, keeping the device awake while it is launching.
        Log.d("AlarmWakefulReceiver", "Starting service @ " + SystemClock.elapsedRealtime());
        startWakefulService(context, service);
    }
}
