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

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import timber.log.Timber

class UCTFirebaseInstanceIDService : FirebaseInstanceIdService() {
  override fun onTokenRefresh() {
    // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
    val refreshedToken = FirebaseInstanceId.getInstance().token
    Timber.d("Refreshed token: $refreshedToken")
  }
}
