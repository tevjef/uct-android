package com.tevinjeffrey.rutgerssoc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d("BootReceiver: ", "Caught android.intent.action.BOOT_COMPLETED");

            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent wakefulBroadcastReceiverIntent = new Intent(context,
                    AlarmWakefulReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 1234, wakefulBroadcastReceiverIntent,
                    0);

            alarmMgr.cancel(alarmIntent);
            alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000,
                    AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);
        }
    }
}
