package com.tevinjeffrey.rutgersct.data.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
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
import com.tevinjeffrey.rutgersct.ui.MainActivity
import com.tevinjeffrey.rutgersct.utils.PreferenceUtils
import dagger.android.AndroidInjection
import timber.log.Timber
import java.io.IOException
import java.math.BigInteger
import javax.inject.Inject


class UCTFirebaseMessagingService : FirebaseMessagingService() {
  @Inject lateinit var gson: Gson
  @Inject lateinit var mPreferenceUtils: PreferenceUtils

  private lateinit var notificationManager: NotificationManager

  private val sound: Uri?
    get() = if (mPreferenceUtils.canPlaySound) {
      RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    } else {
      null
    }

  @RequiresApi(Build.VERSION_CODES.N)
  override fun onCreate() {
    AndroidInjection.inject(this)
    super.onCreate()
    notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      // The id of the group.
      val group = "section_group";
      // The user-visible name of the group.
      val groupName = "Sections"
      notificationManager.createNotificationChannelGroup(NotificationChannelGroup(group, groupName))

      val open: Unit = {
        val id = "channel_section_open"
        val name = "Open Sections"
        val description = "Notify when sections open"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(id, name, importance)
        channel.description = description
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.group = group
        notificationManager.createNotificationChannel(channel)
      }()

      val closed: Unit = {
        val id = "channel_section_closed"
        val name = "Closed Sections"
        val description = "Notify when sections closed"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(id, name, importance)
        channel.description = description
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.group = group
        notificationManager.createNotificationChannel(channel)
      }()

      val generic: Unit = {
        val id = "channel_generic"
        val name = "Other"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance)
        notificationManager.createNotificationChannel(channel)
      }()
    }
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

  private fun handleGenericNotification(message: RemoteMessage) {
    val notificationBuilder = NotificationCompat.Builder(this, "channel_generic")
        .setSmallIcon(R.drawable.ic_notification)
        .setWhen(System.currentTimeMillis())
        .setContentTitle(message.notification.title)
        .setContentText(message.notification.body)

    val notificationIntent = Intent(this.applicationContext, MainActivity::class.java)

    notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
    val intent = PendingIntent.getActivity(applicationContext, 0, notificationIntent, 0)
    notificationBuilder.setContentIntent(intent)

    val notification = notificationBuilder.build()
    notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL

    message.notification
    notificationManager.notify(0, notification)
  }

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

    val title: String
    val body: String
    val color: Int
    val channel: String

    if (uctNotification.status == "Open") {
      title = "A section has opened!"
      body = "Section " + section.number + " of " + course.name + " has opened!"
      color = R.color.green
      channel = "channel_section_open"
    } else {
      title = "A section has closed!"
      body = "Section " + section.number + " of " + course.name + " has closed!"
      color = R.color.red
      channel = "channel_section_closed"
    }

    val notificationBuilder = NotificationCompat.Builder(this, channel)
        .setStyle(NotificationCompat.BigTextStyle()
            .bigText(body)
            .setBigContentTitle(course.name))
        .setSmallIcon(R.drawable.ic_notification)
        .setWhen(System.currentTimeMillis())
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
    notificationBuilder.addAction(R.drawable.ic_open_in_browser, "Webreg", pOpenInBrowser)
    notificationBuilder.setContentIntent(pOpenInBrowser)

    notificationManager.notify(
        BigInteger(System.currentTimeMillis().toString()).toInt(),
        notificationBuilder.build()
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


    private val sectionChannelName = "Section"
    private val sectionChannelDescription = "Notifications for section status changes."
  }
}
