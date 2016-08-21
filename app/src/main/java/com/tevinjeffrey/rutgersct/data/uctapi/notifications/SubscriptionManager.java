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

package com.tevinjeffrey.rutgersct.data.uctapi.notifications;

import android.content.Context;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.tevinjeffrey.uct.dagger.annotations.PerApplication;

import java.io.IOException;

import javax.inject.Inject;

@PerApplication
public class SubscriptionManager {

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    public final Context context;

    @Inject
    public SubscriptionManager(Context context) {
        this.context = context;
    }

    public void subscribe(String topicName) throws IOException {
        FirebaseMessaging pubSub = FirebaseMessaging.getInstance();
        Log.d("SubscriptionManager", "Subscribing: " +topicName);
        pubSub.subscribeToTopic(topicName);
    }

    public void unsubscribe(String topicName) throws IOException {
        FirebaseMessaging pubSub = FirebaseMessaging.getInstance();
        Log.d("SubscriptionManager", "Unsubscribing: " +topicName);
        pubSub.unsubscribeFromTopic(topicName);
    }
}
