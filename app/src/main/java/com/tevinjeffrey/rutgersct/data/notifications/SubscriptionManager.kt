package com.tevinjeffrey.rutgersct.data.notifications

import android.content.Context
import android.util.Log

import com.google.firebase.messaging.FirebaseMessaging

import java.io.IOException

import javax.inject.Inject

class SubscriptionManager @Inject
constructor(val context: Context) {
  @Throws(IOException::class)
  fun subscribe(topicName: String) {
    val pubSub = FirebaseMessaging.getInstance()
    Log.d("SubscriptionManager", "Subscribing: " + topicName)
    pubSub.subscribeToTopic(topicName)
  }

  @Throws(IOException::class)
  fun unsubscribe(topicName: String) {
    val pubSub = FirebaseMessaging.getInstance()
    Log.d("SubscriptionManager", "Unsubscribing: " + topicName)
    pubSub.unsubscribeFromTopic(topicName)
  }

  companion object {
    val SENT_TOKEN_TO_SERVER = "sentTokenToServer"
    val REGISTRATION_COMPLETE = "registrationComplete"
  }
}
