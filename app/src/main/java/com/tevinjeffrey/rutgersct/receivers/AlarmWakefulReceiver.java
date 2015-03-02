package com.tevinjeffrey.rutgersct.receivers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.tevinjeffrey.rutgersct.services.RequestService;

//Called by the alarm manager.
public class AlarmWakefulReceiver extends WakefulBroadcastReceiver {
    public AlarmWakefulReceiver() {
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        // This is the Intent to deliver to our service.
        Intent service = new Intent(context, RequestService.class);
        startWakefulService(context, service);
    }
}
