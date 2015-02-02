package com.tevinjeffrey.rutgerssoc;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.tevinjeffrey.rutgerssoc.model.Course;
import com.tevinjeffrey.rutgerssoc.model.Request;
import com.tevinjeffrey.rutgerssoc.model.TrackedSections;
import com.tevinjeffrey.rutgerssoc.ui.MainActivity;
import com.tevinjeffrey.rutgerssoc.utils.CourseUtils;
import com.tevinjeffrey.rutgerssoc.utils.UrlUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
        Log.d("RequestService-Response", "RequestService started");

        //Construct a list of requests by semester.
        List<Request> requests = new ArrayList<>();

        for(Iterator<TrackedSections> allTrackedSections =
                    TrackedSections.findAll(TrackedSections.class); allTrackedSections.hasNext();) {
            TrackedSections ts = allTrackedSections.next();
            requests.add(new Request(ts.getSubject(), ts.getSemester(), ts.getLocations(), ts.getLevels(), ts.getIndexNumber()));
        }

        for(final Request r: requests) {
            String url = UrlUtils.getCourseUrl(UrlUtils.buildParamUrl(r));
            Ion.with(this)
                    .load(url)
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {

                            if(e == null && result.size() > 0) {
                                Log.d("RequestService-Response", result.toString());
                                ArrayList<Course> courses = getListFromJson(result);

                                for(Course c: courses) {
                                    for(Course.Sections s: c.getSections()) {
                                        if(s.getIndex().equals(r.getIndex()) && s.isOpenStatus()) {
                                            makeNotification(c, s, r);
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(RequestService.this, "No Internet connection", Toast.LENGTH_LONG).show();
                            }

                        }

                        private ArrayList<Course> getListFromJson(JsonArray result) {
                            Type listType = new TypeToken<List<Course>>() {
                            }.getType();
                            return  new Gson().fromJson(result.toString(), listType);
                        }
                    });
        }

        AlarmWakefulReceiver.completeWakefulIntent(intent);

        return START_NOT_STICKY;
    }

    private void makeNotification(Course c, Course.Sections s, Request r) {
        String courseTitle = CourseUtils.getTitle(c);
        String sectionNumber = s.getNumber();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
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
        PendingIntent pOpenInBrowser = PendingIntent.getActivity(RequestService.this,0 , openInBrowser, 0);
        mBuilder.addAction(0, "Open WebReg", pOpenInBrowser);


        //Intent open tracked sections.
        Intent openTracked = new Intent(RequestService.this, MainActivity.class);
        PendingIntent pOpenTracked = PendingIntent.getActivity(RequestService.this,0 , openTracked, 0);
        mBuilder.addAction(0, "Delete", pOpenTracked);


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
