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
    Intent mIntent;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Hold the refence to the intent so that I can use it outside the scope of this method.
        mIntent = intent;
        Timber.i("Request Service started at %s", MyApplication.getTimeNow());

        //Gets a list of all tracked sections from the database.
        final List<TrackedSections> sectionList = TrackedSections.listAll(TrackedSections.class);
        Timber.i("Request Service getting %s sections", sectionList.size());

        //Iterate through the list to create a request object which is then passed to getCourses() method.
        for (final Iterator<TrackedSections> allTrackedSections = sectionList.iterator(); allTrackedSections.hasNext();) {
            TrackedSections ts = allTrackedSections.next();
            final Request r = new Request(ts.getSubject(), ts.getSemester(), ts.getLocations(), ts.getLevels(), ts.getIndexNumber());
            getCourse(allTrackedSections, r);
        }
        return START_NOT_STICKY;
    }

    private void getCourse(final Iterator<TrackedSections> allTrackedSections, final Request r) {
        //Get Url based on the request
        String url = UrlUtils.getCourseUrl(UrlUtils.buildParamUrl(r));

        Ion.with(this)
                .load(url)
                .as(new TypeToken<List<Course>>() {
                })
                .setCallback(new FutureCallback<List<Course>>() {
                    @Override
                    public void onCompleted(Exception e, List<Course> courses) {
                        //If e is null, notghing catastophic occur while completeing the request.
                        // If course size is 0, we can't do anthing.
                        if (e == null && courses.size() > 0) {
                            //For courses in the list.
                            for (final Course c : courses) {
                                //For sections in the course
                                for (final Course.Sections s : c.getSections()) {
                                    //If the index number of the current section is the same as the
                                    //one we are looking for and the section is open
                                    if (s.getIndex().equals(r.getIndex()) && s.isOpenStatus()) {
                                        //Create a notification.
                                        makeNotification(c, s, r);
                                        //If the interator does not have anymore sections,
                                        //stop the service.
                                        if (!allTrackedSections.hasNext()) {
                                            stopSelf();
                                        }
                                    }
                                }
                            }
                        } else if (!(e instanceof CancellationException)) {
                            //If an error occured while completing the request. Send it to crash reporting.
                            Timber.e(e, "Crash while attempting to complete request in %s to %s"
                                    , RequestService.this.toString(), r.toString());
                        }
                    }
                });
    }

    //Creates a notfication of the Android system.
    private void makeNotification(Course c, Course.Sections s, Request r) {
        String courseTitle = c.getTrueTitle();
        String sectionNumber = s.getNumber();

        //Builds a notification
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
        mBuilder.addAction(0, "OPEN WEBREG", pOpenInBrowser);

        //Intent open the app.
        Intent openTracked = new Intent(RequestService.this, MainActivity.class);
        PendingIntent pOpenTracked = PendingIntent.getActivity(RequestService.this, 0, openTracked, FLAG_ONE_SHOT);
        mBuilder.addAction(0, "STOP TRACKING", pOpenTracked);

        //When you click on the notification itself.
        mBuilder.setContentIntent(pOpenInBrowser);

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //Builds the intent and sends it to notification manager with a unique ID.
        // The id is the index number of the section since those are also unique.
        // It also allows me to easily update the notication in the future.
        mNotifyMgr.notify(Integer.valueOf(r.getIndex()), mBuilder.build());
    }


    //Androd boilerplate code
    @Override public IBinder onBind(Intent intent) {return null;}

    @Override
    public void onDestroy() {
        Timber.i("Request Service ended at %s", MyApplication.getTimeNow());

        //Tells wakeful reciever to release wakelock and allow the the CPU to goto sleep.
        AlarmWakefulReceiver.completeWakefulIntent(mIntent);
        super.onDestroy();
    }

}
