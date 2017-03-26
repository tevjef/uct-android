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
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Course;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Section;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Subject;
import com.tevinjeffrey.rutgersct.data.uctapi.model.UCTNotification;
import com.tevinjeffrey.rutgersct.data.uctapi.model.University;
import com.tevinjeffrey.rutgersct.utils.PreferenceUtils;
import java.io.IOException;
import java.math.BigInteger;
import javax.inject.Inject;
import timber.log.Timber;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

  private static final String SECTION_NOTIFICATION_GROUP = "SECTION_NOTIFICATION_GROUP";

  @Inject
  Gson gson;

  @Inject
  PreferenceUtils mPreferenceUtils;

  private static final String TAG = "FirebaseMessagingSer";

  public void onMessageReceived(RemoteMessage message) {
    RutgersCTApp.getObjectGraph(this).inject(this);

    String data = message.getData().get("message");
    Timber.d(new StringBuilder().append("From: ").append(message.getFrom()).toString());
    Timber.d(new StringBuilder().append("Message: ").append(data).toString());

    if (data == null) {
      handleGenericNotification(message);
      return;
    }

    sendNotification(data);
  }

  private void sendNotification(String message) {
    UCTNotification uctNotification = null;
    try {
      uctNotification = gson.getAdapter(UCTNotification.class).fromJson(message);
    } catch (IOException e) {
      e.printStackTrace();
    }

    University university = uctNotification.university;
    Subject subject = university.subjects.get(0);
    Course course = subject.courses.get(0);
    Section section = course.sections.get(0);

    NotificationManager notificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    NotificationCompat.Builder mBuilder;

    String title;
    String body;
    int color;
    if (uctNotification.status.equals("Open")) {
      title = "A section has opened!";
      body = "Section " + section.number + " of " + course.name + " has opened!";
      color = R.color.green;
    } else {
      title = "A section has closed!";
      body = "Section " + section.number + " of " + course.name + " has closed!";
      color = R.color.red;
    }

    mBuilder =
        new NotificationCompat.Builder(this)
            .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(body)
                .setBigContentTitle(course.name))
            .setSmallIcon(R.drawable.ic_notification)
            .setWhen(System.currentTimeMillis())
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setGroup(SECTION_NOTIFICATION_GROUP)
            .setColor(ContextCompat.getColor(this, color))
            .setAutoCancel(true)
            .setSound(getSound())
            .setContentTitle(title)
            .setContentText(body);

    //Intent to start web browser
    Intent openInBrowser = new Intent(Intent.ACTION_VIEW);
    openInBrowser.setData(Uri.parse("https://sims.rutgers.edu/webreg/"));
    PendingIntent pOpenInBrowser = PendingIntent.getActivity(this, 0, openInBrowser, 0);
    mBuilder.addAction(R.drawable.ic_open_in_browser_white_24dp, "Webreg", pOpenInBrowser);
    mBuilder.setContentIntent(pOpenInBrowser);

    notificationManager.notify(
        new BigInteger(String.valueOf(System.currentTimeMillis())).intValue(),
        mBuilder.build()
    );

    Answers.getInstance()
        .logCustom(new CustomEvent("receive_notification")
            .putCustomAttribute("status", section.status)
            .putCustomAttribute("topic", section.topic_name));

    Log.d("FCM message", message);
  }

  private void handleGenericNotification(RemoteMessage message) {
  }

  private Uri getSound() {
    if (mPreferenceUtils.getCanPlaySound()) {
      return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    } else {
      return null;
    }
  }
}
