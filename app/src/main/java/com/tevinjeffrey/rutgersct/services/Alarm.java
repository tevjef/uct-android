package com.tevinjeffrey.rutgersct.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.tevinjeffrey.rutgersct.receivers.AlarmWakefulReceiver;
import com.tevinjeffrey.rutgersct.utils.PreferenceUtils;

import java.sql.Time;
import java.util.concurrent.TimeUnit;


public class Alarm {
    private final Context mContext;

    public Alarm(Context context) {
        this.mContext = context;
    }

    public void setAlarm() {
        AlarmManager alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        //Intent to a wakeful receiver. Rather than to the service directly.
        Intent wakefulBroadcastReceiverIntent = new Intent(mContext,
                AlarmWakefulReceiver.class);

        //Package that intent into a pending intent.
        PendingIntent alarmIntent = PendingIntent.getBroadcast(mContext, 1234, wakefulBroadcastReceiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //Cancel all previous alarms of the same pending intent.
        alarmMgr.cancel(alarmIntent);

        //Set the alarm to the alarm manager.
        alarmMgr.setInexactRepeating(
                //Time since the device booted.
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                getInterval(),
                alarmIntent);
    }

    private long getInterval() {
        int index = PreferenceUtils.getSyncInterval();
        if (index == 0) {
            return TimeUnit.MINUTES.toMillis(5);
        } else if (index == 1) {
            return TimeUnit.MINUTES.toMillis(15);
        } else if (index == 2) {
            return TimeUnit.HOURS.toMillis(1);
        } else if (index == 3) {
            return TimeUnit.HOURS.toMillis(3);
        } else {
            return TimeUnit.HOURS.toMillis(6);
        }
    }
}
