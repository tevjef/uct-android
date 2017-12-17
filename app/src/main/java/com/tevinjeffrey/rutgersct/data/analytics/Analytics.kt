package com.tevinjeffrey.rutgersct.data.analytics

import android.app.Activity
import android.os.Bundle
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.ContentViewEvent
import com.crashlytics.android.answers.CustomEvent
import com.google.firebase.analytics.FirebaseAnalytics
import com.tevinjeffrey.rutgersct.dagger.PerApp
import com.tevinjeffrey.rutgersct.data.model.Semester
import com.tevinjeffrey.rutgersct.data.model.string
import javax.inject.Inject

@PerApp
class Analytics @Inject constructor(private val firebaseAnalytics: FirebaseAnalytics) {

  fun setDefaultUniversity(topicName: String) {
    firebaseAnalytics.setUserProperty(Event.defaultUniversity, topicName)
  }

  fun setDefaultSemester(semester: Semester) {
    firebaseAnalytics.setUserProperty(Event.defaultSemester, semester.string())
  }

  fun logScreenView(activity: Activity, screenName: String) {
    firebaseAnalytics.setCurrentScreen(activity, screenName, screenName)
    Answers.getInstance().logContentView(ContentViewEvent().putContentName(screenName))
  }

  fun logTrackedSections(sectionCount: Int) {
    val params = Bundle().apply {
      putInt(Param.sectionCount, sectionCount)
    }
    firebaseAnalytics.logEvent(Event.subscribe, params)
    Answers.getInstance().logEvent(Event.subscribe, params)
  }

  fun logTrackedSectionsMigration(sectionCount: Int) {
    val params = Bundle().apply {
      putInt(Param.sectionCount, sectionCount)
    }
    firebaseAnalytics.logEvent(Event.migration, params)
    Answers.getInstance().logEvent(Event.migration, params)
  }

  fun logSubscribe(sectionTopicId: String, sectionTopicName: String) {
    val params = Bundle().apply {
      putString(Param.topicId, sectionTopicId)
      putString(Param.topicName, sectionTopicName)
    }
    firebaseAnalytics.logEvent(Event.subscribe, params)
    Answers.getInstance().logEvent(Event.subscribe, params)
  }

  fun logUnsubscribe(sectionTopicId: String, sectionTopicName: String) {
    val params = Bundle().apply {
      putString(Param.topicId, sectionTopicId)
      putString(Param.topicName, sectionTopicName)
    }

    firebaseAnalytics.logEvent(Event.unsubscribe, params)
    Answers.getInstance().logEvent(Event.unsubscribe, params)
  }

  fun logReceiveNotification(sectionTopicId: String, sectionTopicName: String, notification: String) {
    val params = Bundle().apply {
      putString(Param.topicId, sectionTopicId)
      putString(Param.topicName, sectionTopicName)
      putString(Param.notificationIdentifier, notification)
    }

    firebaseAnalytics.logEvent(Event.receiveNotification, params)
    Answers.getInstance().logEvent(Event.receiveNotification, params)
  }

  private fun Answers.logEvent(eventName: String, params: Bundle) {
    val event = CustomEvent(eventName)
    params.keySet()
        .filter { params.get(it).toString().length < 100 }
        .fold(event, { acc, s ->
      acc.putCustomAttribute(s, params.get(s).toString())
    })
    this.logCustom(event)
  }

   object Param {
     var screen_name = "screen_name"
     var name = "name"
     var number = "number"
     var topicId = "topic_id"
     var topicName = "topic_name"
     var sectionCount = "section_count"
     var semester = "semester"
     var notificationIdentifier = "notificationIdentifier"
   }

  object Event {
    var defaultUniversity = "default_university"
    var defaultSemester = "default_semester"
    var subscribe = "subscribe"
    var migration = "migration"
    var unsubscribe = "unsubscribe"
    var receiveNotification = "receive_notification"
    var popHome = "pop_home"
  }
}