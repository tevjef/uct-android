package com.tevinjeffrey.rutgersct.data.notifications

import android.annotation.TargetApi
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
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.squareup.moshi.Moshi
import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.data.UCTApi
import com.tevinjeffrey.rutgersct.data.model.UCTNotification
import com.tevinjeffrey.rutgersct.ui.MainActivity
import com.tevinjeffrey.rutgersct.utils.PreferenceUtils
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.io.IOException
import java.math.BigInteger
import javax.inject.Inject


class UCTFirebaseMessagingService : FirebaseMessagingService() {
  @Inject lateinit var moshi: Moshi
  @Inject lateinit var uctApi: UCTApi
  @Inject lateinit var mPreferenceUtils: PreferenceUtils

  private lateinit var notificationManager: NotificationManager

  private val sound: Uri?
    get() = if (mPreferenceUtils.canPlaySound) {
      RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    } else {
      null
    }

  override fun onCreate() {
    AndroidInjection.inject(this)
    super.onCreate()
    notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val group = createSectionGroup()
      createOpenChannel(group)
      createClosedChannel(group)
      createGenericChannel()
    }
  }

  @TargetApi(Build.VERSION_CODES.O)
  private fun createSectionGroup(): String {
    // The id of the group.
    val group = getString(R.string.section_group);
    // The user-visible name of the group.
    val groupName = getString(R.string.sections)
    notificationManager.createNotificationChannelGroup(NotificationChannelGroup(group, groupName))
    return group
  }

  @TargetApi(Build.VERSION_CODES.O)
  private fun createGenericChannel() {
    val id = getString(R.string.channel_generic)
    val name = getString(R.string.other)
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(id, name, importance)
    notificationManager.createNotificationChannel(channel)
  }

  @TargetApi(Build.VERSION_CODES.O)
  private fun createClosedChannel(group: String) {
    val id = getString(R.string.channel_section_closed)
    val name = getString(R.string.channel_closed_name)
    val description = getString(R.string.channel_closed_description)
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(id, name, importance)
    channel.description = description
    channel.enableLights(true)
    channel.enableVibration(true)
    channel.group = group
    notificationManager.createNotificationChannel(channel)
  }

  @TargetApi(Build.VERSION_CODES.O)
  private fun createOpenChannel(group: String) {
    val id = getString(R.string.channel_section_open)
    val name = getString(R.string.channel_open_name)
    val description = getString(R.string.channel_open_description)
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(id, name, importance)
    channel.description = description
    channel.enableLights(true)
    channel.enableVibration(true)
    channel.group = group
    notificationManager.createNotificationChannel(channel)
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
    val notificationBuilder = NotificationCompat.Builder(this, getString(R.string.channel_generic))
        .setSmallIcon(R.drawable.ic_notification)
        .setWhen(System.currentTimeMillis())
        .setContentTitle(message.notification?.title)
        .setContentText(message.notification?.body)

    val intent = openAppIntent()
    notificationBuilder.setContentIntent(intent)

    val notification = notificationBuilder.build()
    notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL

    message.notification
    notificationManager.notify(0, notification)
  }

  private fun openAppIntent(): PendingIntent {
    val notificationIntent = Intent(this.applicationContext, MainActivity::class.java)
    notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
    return PendingIntent.getActivity(applicationContext, 0, notificationIntent, 0)
  }

  private fun sendNotification(message: String) {
    var uctNotification: UCTNotification? = null
    try {
      uctNotification = moshi.adapter(UCTNotification::class.java).fromJson(message)
    } catch (e: IOException) {
      Timber.e(e)
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
      title = getString(R.string.title_section_open)
      body = getString(R.string.body_section_open, section.number, course.name)
      color = R.color.green
      channel = getString(R.string.channel_section_open)
    } else {
      title = getString(R.string.title_section_closed)
      body = getString(R.string.body_section_closed, section.number, course.name)
      color = R.color.red
      channel = getString(R.string.channel_section_closed)
    }

    val notificationBuilder = NotificationCompat.Builder(this, channel)
        .setStyle(NotificationCompat.BigTextStyle()
            .bigText(body)
            .setBigContentTitle(course.name))
        .setSmallIcon(R.drawable.ic_notification)
        .setWhen(System.currentTimeMillis())
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setGroup(getString(R.string.section_notification_group))
        .setColor(ContextCompat.getColor(this, color))
        .setAutoCancel(true)
        .setSound(sound)
        .setContentTitle(title)
        .setContentText(body)

    //Intent to start web browser
    val actionIntent = PendingIntent.getActivity(this, 0, Intent(Intent.ACTION_VIEW).apply {
      data = Uri.parse(university.registration_page)
    }, 0)

    notificationBuilder
        .addAction(R.drawable.ic_open_in_browser, getString(R.string.register), actionIntent)
        .setContentIntent(openAppIntent())

    notificationManager.notify(
        BigInteger(System.currentTimeMillis().toString()).toInt(),
        notificationBuilder.build()
    )

    uctApi.acknowledgeNotification(
        section.topic_id.orEmpty(),
        section.topic_name.orEmpty(),
        uctNotification.notification_id.toString()
    ).observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { Timber.i("Acknowledged notification: ${uctNotification.notification_id} for topic:" +
                  " ${uctNotification.topic_name}")
            },
            { Timber.e(it) }
        )

    Answers.getInstance()
        .logCustom(CustomEvent("receive_notification")
            .putCustomAttribute("status", section.status)
            .putCustomAttribute("topic", section.topic_name))
  }
}
