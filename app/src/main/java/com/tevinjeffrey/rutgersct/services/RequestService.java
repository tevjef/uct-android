package com.tevinjeffrey.rutgersct.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.tevinjeffrey.rutgersct.MyApplication;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.database.Updater;
import com.tevinjeffrey.rutgersct.model.Course;
import com.tevinjeffrey.rutgersct.model.Request;
import com.tevinjeffrey.rutgersct.receivers.AlarmWakefulReceiver;
import com.tevinjeffrey.rutgersct.receivers.DatabaseReceiver;

import java.util.List;
import java.util.concurrent.CancellationException;

import timber.log.Timber;

public class RequestService extends Service {
    Intent mIntent;

    public RequestService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Hold the refence to the intent so that I can use it outside the scope of this method.
        mIntent = intent;
        Timber.i("Request Service started at %s", MyApplication.getTimeNow());

        Updater.with(this).setOnCompleteListener(new Updater.OnCompleteListener() {
            @Override
            public void onSuccess(Request request, Course course) {
                if (course.getSections().get(0).isOpenStatus())
                    makeNotification(course, request);
            }

            @Override
            public void onError(Throwable t, Request r) {
                if (t != null && !(t instanceof CancellationException)) {
                    //If an error occured while completing the request. Send it to crash reporting.
                    Timber.e(t, "Crash while attempting to complete request in %s to %s"
                            , RequestService.this.toString(), r.toString());
                }

            }

            @Override
            public void onDone(List<Course> mappedValues) {
                stopSelf();
            }
        }).start();
        return START_NOT_STICKY;
    }

    //Creates a notfication of the Android system.

    private void makeNotification(Course c, Request r) {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("app_notification", true)) {
            String courseTitle = c.getTrueTitle();
            Course.Sections sections = c.getSections().get(0);
            String sectionNumber = sections.getNumber();

            //Builds a notification
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("Section " + sectionNumber + " of " + courseTitle
                                            + " has opened")
                                    .setBigContentTitle(r.getSemester().toString() + " - " + courseTitle))
                            .setSmallIcon(R.drawable.ic_notification)
                            .setWhen(System.currentTimeMillis())
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setColor(getResources().getColor(R.color.green))
                            .setAutoCancel(true)
                            .setSound(getSound())
                            .setContentTitle("A section has opened!")
                            .setContentText("Section " + sectionNumber + " of " + courseTitle
                                    + " has opened");

            //Intent to start web browser
            Intent openInBrowser = new Intent(Intent.ACTION_VIEW);
            openInBrowser.setData(Uri.parse("https://sims.rutgers.edu/webreg/"));
            PendingIntent pOpenInBrowser = PendingIntent.getActivity(RequestService.this, 0, openInBrowser, 0);
            mBuilder.addAction(0, "Open Webreg", pOpenInBrowser);

            //Intent open the app.
            Intent openTracked = new Intent(RequestService.this, DatabaseReceiver.class);
            openTracked.putExtra(MyApplication.REQUEST, r);
            openTracked.putExtra(MyApplication.SELECTED_COURSE, c);
            PendingIntent pOpenTracked = PendingIntent.getBroadcast(RequestService.this, Integer.valueOf(r.getIndex()), openTracked, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.addAction(0, "Stop Tracking", pOpenTracked);

            //When you click on the notification itself.
            mBuilder.setContentIntent(pOpenInBrowser);

            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            //Builds the intent and sends it to notification manager with a unique ID.
            // The id is the index number of the section since those are also unique.
            // It also allows me to easily update the notication in the future.
            Notification n = mBuilder.build();
            mNotifyMgr.notify(Integer.valueOf(r.getIndex()), n);
        }
    }

    private Uri getSound() {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("sound", true)) {
            return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        } else {
            return null;
        }
    }


    //Androd boilerplate code
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Timber.i("Request Service ended at %s", MyApplication.getTimeNow());

        //Tells wakeful reciever to release wakelock and allow the the CPU to goto sleep.
        AlarmWakefulReceiver.completeWakefulIntent(mIntent);
        super.onDestroy();
    }

    @Override
    public String toString() {
        return "RequestService";
    }
}
