/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tevinjeffrey.rutgersct.data.uctapi.notifications;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Course;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Section;
import com.tevinjeffrey.rutgersct.data.uctapi.model.University;

import java.io.IOException;

import javax.inject.Inject;

import timber.log.Timber;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Inject
    Gson gson;

    private static final String TAG = "FirebaseMessagingSer";


    public void onMessageReceived(RemoteMessage message) {
        RutgersCTApp.getObjectGraph(this).inject(this);

        String data = message.getData().get("message");
        Timber.d(new StringBuilder().append("From: ").append(message.getFrom()).toString());
        Timber.d(new StringBuilder().append("Message: ").append(data).toString());



        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(data);
    }


    private void sendNotification(String message) {
        University university = null;
        try {
            university = gson.getAdapter(University.class).fromJson(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Timber.d("university: %s", university.toString());

        Course course = university.subjects.get(0).courses.get(0);
        Section section = course.sections.get(0);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder;
        if (section.status.equals("Open")) {
            mBuilder =
                    new NotificationCompat.Builder(this)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("Section " + section.number + " of " + course.name
                                            + " has opened")
                                    .setBigContentTitle(course.number + " - " + course.name))
                            .setSmallIcon(R.drawable.ic_track_changes_black_24dp)                        .setWhen(System.currentTimeMillis())
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentTitle("A section has opened!")
                            .setContentText("Section " + section.number + " of " + course.name
                                    + " has opened");


            notificationManager.notify(Integer.parseInt(section.call_number) /* ID of notification */, mBuilder.build());
        } else {
            notificationManager.cancel(Integer.parseInt(section.call_number));
        }

        /*
        * //Builds a notification
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("Section " + sectionNumber + " of " + courseTitle
                                            + " has opened")
                                    .setBigContentTitle(r.getSemester().toString() + " - " + courseTitle))
                            .setSmallIcon(R.drawable.ic_notification)
                            .setWhen(System.currentTimeMillis())
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setColor(ContextCompat.getColor(mContext, R.color.green))
                            .setAutoCancel(true)
                            .setGroup(SECTION_NOTIFICATION_GROUP)
                            .setSound(getSound())
                            .setContentTitle("A section has opened!")
                            .setContentText("Section " + sectionNumber + " of " + courseTitle
                                    + " has opened");

            //Intent to start web browser
            Intent openInBrowser = new Intent(Intent.ACTION_VIEW);
            openInBrowser.setData(Uri.parse("https://sims.rutgers.edu/webreg/"));
            PendingIntent pOpenInBrowser = PendingIntent.getActivity(RequestService.this, 0, openInBrowser, 0);
            mBuilder.addAction(R.drawable.ic_open_in_browser_white_24dp, "Webreg", pOpenInBrowser);

            //Intent open the app.
            Intent openTracked = new Intent(RequestService.this, DatabaseReceiver.class);
            openTracked.putExtra(TrackedSectionsView.REQUEST, r);
            openTracked.putExtra(CourseInfoView.SELECTED_SECTION, section);
            //The intent that will be when the user clicks stop tracking in the notification bar.
            PendingIntent pOpenTracked = PendingIntent.getBroadcast(RequestService.this,
                    Integer.valueOf(r.getIndex()), openTracked, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.addAction(R.drawable.ic_close_white_24dp, "Stop Tracking", pOpenTracked);

            //When you click on the notification itself.
            mBuilder.setContentIntent(pOpenInBrowser);

            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            //Builds the intent and sends it to notification manager with a unique ID.
            // The id is the index number of the section since those are also unique.
            // It also allows me to easily update the notication in the future.
            Notification n = mBuilder.build();
            mNotifyMgr.notify(Integer.valueOf(r.getIndex()), n);*/

        Log.d("FCM message", message);
    }
}
