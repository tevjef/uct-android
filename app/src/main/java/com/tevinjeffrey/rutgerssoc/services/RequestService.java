package com.tevinjeffrey.rutgerssoc.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.splunk.mint.Mint;
import com.splunk.mint.MintLogLevel;
import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.model.Course;
import com.tevinjeffrey.rutgerssoc.model.Request;
import com.tevinjeffrey.rutgerssoc.model.TrackedSections;
import com.tevinjeffrey.rutgerssoc.receivers.AlarmWakefulReceiver;
import com.tevinjeffrey.rutgerssoc.ui.MainActivity;
import com.tevinjeffrey.rutgerssoc.utils.UrlUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RequestService extends Service {
    public RequestService() {
    }

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Construct a list of requests by semester.
        //List<Request> requests = new ArrayList<>();
        Mint.transactionStart("startRequestService");
        for (final Iterator<TrackedSections> allTrackedSections = TrackedSections.findAll(TrackedSections.class); allTrackedSections.hasNext(); ) {
            TrackedSections ts = allTrackedSections.next();
            final Request r = new Request(ts.getSubject(), ts.getSemester(), ts.getLocations(), ts.getLevels(), ts.getIndexNumber());

            getCourse(allTrackedSections, r);
        }
        Mint.transactionStop("startRequestService");
        AlarmWakefulReceiver.completeWakefulIntent(intent);
        return START_NOT_STICKY;
    }

    private void getCourse(final Iterator<TrackedSections> allTrackedSections, final Request r) {
        String url = UrlUtils.getCourseUrl(UrlUtils.buildParamUrl(r));
        Ion.with(this)
                .load(url)
                .as(new TypeToken<List<Course>>() {
                })
                .setCallback(new FutureCallback<List<Course>>() {
                    @Override
                    public void onCompleted(Exception e, List<Course> courses) {
                        if (e == null && courses.size() > 0) {
                            for (final Course c : courses) {
                                for (final Course.Sections s : c.getSections()) {
                                    if (s.getIndex().equals(r.getIndex()) && s.isOpenStatus()) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                makeNotification(c, s, r);
                                                if (!allTrackedSections.hasNext()) {
                                                    stopSelf();
                                                }
                                            }
                                        }, 8000);
                                    }
                                }
                            }
                        } else {
                            Mint.transactionCancel("startRequestService", (e != null ? e.getMessage() : null));

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("Request", r.toString());
                            map.put("Error", (e != null ? e.getMessage() : "An error occurred"));
                            Mint.logExceptionMap(map, e);
                            if(e != null && e.getMessage() != null) Toast.makeText(RequestService.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void makeNotification(Course c, Course.Sections s, Request r) {
        String courseTitle = c.getTrueTitle();
        String sectionNumber = s.getNumber();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Section " + sectionNumber + " of " + courseTitle
                                        + " has opened")
                                .setBigContentTitle("A section has opened"))
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setWhen(System.currentTimeMillis())
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentTitle("A section has opened")
                        .setContentText("Section " + sectionNumber + " of " + courseTitle
                                + " has opened");

        //Intent to start web browser
        Intent openInBrowser = new Intent(Intent.ACTION_VIEW);
        openInBrowser.setData(Uri.parse("https://sims.rutgers.edu/webreg/"));
        PendingIntent pOpenInBrowser = PendingIntent.getActivity(RequestService.this, 0, openInBrowser, 0);
        mBuilder.addAction(0, "Open WebReg", pOpenInBrowser);


        //Intent open tracked sections.
        Intent openTracked = new Intent(RequestService.this, MainActivity.class);
        PendingIntent pOpenTracked = PendingIntent.getActivity(RequestService.this, 0, openTracked, 0);
        mBuilder.addAction(0, "Delete", pOpenTracked);


        mBuilder.setContentIntent(pOpenInBrowser);

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(Integer.valueOf(r.getIndex()), mBuilder.build());

        Mint.logEvent("Courses Sniped", MintLogLevel.Info);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
