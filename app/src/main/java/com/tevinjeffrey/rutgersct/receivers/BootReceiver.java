package com.tevinjeffrey.rutgersct.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.tevinjeffrey.rutgersct.R;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d("BootReceiver: ", "Caught android.intent.action.BOOT_COMPLETED");

            setAlarm(context);
        }
    }

    private void setAlarm(Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent wakefulBroadcastReceiverIntent = new Intent(context,
                AlarmWakefulReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 1234, wakefulBroadcastReceiverIntent,
                0);

        alarmMgr.cancel(alarmIntent);
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000,
                getInterval(context), alarmIntent);
    }

    long getInterval(Context context) {
        int index = PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(context.getResources().getString(R.string.sync_interval), 1);
        if (index == 0) {
            return 5 * 60 * 1000;
        } else if (index == 1) {
            return 15 * 60 * 1000;
        } else if (index == 2) {
            return 60 * 60 * 1000;
        } else if (index == 3) {
            return 3 * 60 * 60 * 1000;
        } else {
            return 6 * 60 * 60 * 1000;
        }
    }
}
