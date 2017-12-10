package com.tevinjeffrey.rutgersct.data.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.data.model.UCTNotification
import com.tevinjeffrey.rutgersct.utils.PreferenceUtils
import dagger.android.AndroidInjection
import timber.log.Timber
import java.io.IOException
import java.math.BigInteger
import javax.inject.Inject

class UCTFirebaseMessagingService : FirebaseMessagingService() {
  @Inject lateinit var gson: Gson
  @Inject lateinit var mPreferenceUtils: PreferenceUtils

  private val sound: Uri?
    get() = if (mPreferenceUtils.canPlaySound) {
      RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    } else {
      null
    }

  override fun onCreate() {
    AndroidInjection.inject(this)
    super.onCreate()
  }

  override fun onMessageReceived(message: RemoteMessage) {
    val data = message.data["message"]
    Timber.d(StringBuilder().append("From: ").append(message.from).toString())
    Timber.d(StringBuilder().append("Message: ").append(data).toString())

    if (data == null) {
      handleGenericNotification(message)
      return
    }

    sendNotification(data)
  }

  private fun handleGenericNotification(message: RemoteMessage) {}

  private fun sendNotification(message: String) {
    var uctNotification: UCTNotification? = null
    try {
      uctNotification = gson.getAdapter(UCTNotification::class.java).fromJson(message)
    } catch (e: IOException) {
      e.printStackTrace()
    }

    val university = uctNotification!!.university
    val subject = university!!.subjects[0]
    val course = subject.courses[0]
    val section = course.sections[0]

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val mBuilder: NotificationCompat.Builder

    val title: String
    val body: String
    val color: Int
    if (uctNotification.status == "Open") {
      title = "A section has opened!"
      body = "Section " + section.number + " of " + course.name + " has opened!"
      color = R.color.green
    } else {
      title = "A section has closed!"
      body = "Section " + section.number + " of " + course.name + " has closed!"
      color = R.color.red
    }

    mBuilder = NotificationCompat.Builder(this)
        .setStyle(NotificationCompat.BigTextStyle()
            .bigText(body)
            .setBigContentTitle(course.name))
        .setSmallIcon(R.drawable.ic_notification)
        .setWhen(System.currentTimeMillis())
        .setChannelId(SECTION_CHANNEL)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setGroup(SECTION_NOTIFICATION_GROUP)
        .setColor(ContextCompat.getColor(this, color))
        .setAutoCancel(true)
        .setSound(sound)
        .setContentTitle(title)
        .setContentText(body)

    //Intent to start web browser
    val openInBrowser = Intent(Intent.ACTION_VIEW)
    openInBrowser.data = Uri.parse("https://sims.rutgers.edu/webreg/")
    val pOpenInBrowser = PendingIntent.getActivity(this, 0, openInBrowser, 0)
    mBuilder.addAction(R.drawable.ic_open_in_browser, "Webreg", pOpenInBrowser)
    mBuilder.setContentIntent(pOpenInBrowser)

    notificationManager.notify(
        BigInteger(System.currentTimeMillis().toString()).toInt(),
        mBuilder.build()
    )

    Answers.getInstance()
        .logCustom(CustomEvent("receive_notification")
            .putCustomAttribute("status", section.status)
            .putCustomAttribute("topic", section.topic_name))

    Log.d("FCM message", message)
  }

  companion object {
    private val SECTION_NOTIFICATION_GROUP = "SECTION_NOTIFICATION_GROUP"
    private val SECTION_CHANNEL = "SECTION_CHANNEL"
  }
}
