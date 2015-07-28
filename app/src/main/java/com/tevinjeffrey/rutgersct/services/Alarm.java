package com.tevinjeffrey.rutgersct.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.tevinjeffrey.rutgersct.utils.PreferenceUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import timber.log.Timber;


public class Alarm {
    private final Context mContext;
    private PreferenceUtils mPreferenceUtils;

    @Inject
    public Alarm(PreferenceUtils preferenceUtils, Context context) {
        this.mPreferenceUtils = preferenceUtils;
        this.mContext = context;
    }

    public void setAlarm() {
        AlarmManager alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        Intent service = new Intent(mContext, RequestService.class);;

        //Package that intent into a pending intent.
        PendingIntent alarmIntent = PendingIntent.getService(mContext, 1234, service,
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
        Timber.d("Alarm set: " + getInterval());
    }

    private long getInterval() {
        int index = mPreferenceUtils.getSyncInterval();
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
