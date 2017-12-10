/*
 * Copyright 2016 Tevin Jeffrey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tevinjeffrey.rutgersct.data.notifications

import android.app.IntentService
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import dagger.android.AndroidInjection
import javax.inject.Inject

class RegistrationIntentService : IntentService(TAG) {
  @Inject lateinit var subscriptionManager: SubscriptionManager

  override fun onCreate() {
    AndroidInjection.inject(this)
    super.onCreate()
  }

  override fun onHandleIntent(intent: Intent?) {
    val registrationComplete = Intent(SubscriptionManager.REGISTRATION_COMPLETE)
    LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete)
  }

  companion object {
    private val TAG = "RegIntentService"
  }
}
