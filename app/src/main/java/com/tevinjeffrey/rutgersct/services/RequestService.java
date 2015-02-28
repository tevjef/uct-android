package com.tevinjeffrey.rutgersct.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import static android.app.PendingIntent.FLAG_ONE_SHOT;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.tevinjeffrey.rutgersct.MyApplication;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.model.Course;
import com.tevinjeffrey.rutgersct.model.Request;
import com.tevinjeffrey.rutgersct.model.TrackedSections;
import com.tevinjeffrey.rutgersct.receivers.AlarmWakefulReceiver;
import com.tevinjeffrey.rutgersct.ui.MainActivity;
import com.tevinjeffrey.rutgersct.utils.UrlUtils;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CancellationException;

import timber.log.Timber;

public class RequestService extends Service {
    public RequestService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Construct a list of requests by semester.
        //List<Request> requests = new ArrayList<>();
        Timber.i("Request Service started at %s", MyApplication.getTimeNow());
        final List<TrackedSections> sectionList = TrackedSections.listAll(TrackedSections.class);
        Timber.i("Request Service getting %s sections", sectionList.size());
        for (final Iterator<TrackedSections> allTrackedSections = sectionList.iterator(); allTrackedSections.hasNext(); ) {
            TrackedSections ts = allTrackedSections.next();
            final Request r = new Request(ts.getSubject(), ts.getSemester(), ts.getLocations(), ts.getLevels(), ts.getIndexNumber());
            getCourse(allTrackedSections, r);
        }
        AlarmWakefulReceiver.completeWakefulIntent(intent);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Timber.i("Request Service ended at %s", MyApplication.getTimeNow());
        super.onDestroy();
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
                        } else if (!(e instanceof CancellationException)) {
                            Timber.e(e, "Crash while attempting to complete request in %s to %s"
                                    , RequestService.this.toString(), r.toString());
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
                        .setSmallIcon(R.drawable.ic_notification)
                        .setWhen(System.currentTimeMillis())
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setColor(getResources().getColor(R.color.green))
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentTitle("A section has opened!")
                        .setContentText("Section " + sectionNumber + " of " + courseTitle
                                + " has opened");

        //Intent to start web browser
        Intent openInBrowser = new Intent(Intent.ACTION_VIEW);
        openInBrowser.setData(Uri.parse("https://sims.rutgers.edu/webreg/"));
        PendingIntent pOpenInBrowser = PendingIntent.getActivity(RequestService.this, 0, openInBrowser, 0);
        mBuilder.addAction(0, "Open WebReg", pOpenInBrowser);

        //Intent open tracked sections.
        Intent openTracked = new Intent(RequestService.this, MainActivity.class);

        PendingIntent pOpenTracked = PendingIntent.getActivity(RequestService.this, 0, openTracked, FLAG_ONE_SHOT);
        mBuilder.addAction(0, "Stop tracking", pOpenTracked);


        mBuilder.setContentIntent(pOpenInBrowser);

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(Integer.valueOf(r.getIndex()), mBuilder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
