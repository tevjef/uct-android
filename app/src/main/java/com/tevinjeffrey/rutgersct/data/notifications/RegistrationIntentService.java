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

package com.tevinjeffrey.rutgersct.data.notifications;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import javax.inject.Inject;

public class RegistrationIntentService extends IntentService {

  private static final String TAG = "RegIntentService";

  @Inject SubscriptionManager subscriptionManager;

  public RegistrationIntentService() {
    super(TAG);
  }

  @Override public void onCreate() {
    AndroidInjection.inject(this);
    super.onCreate();
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Intent registrationComplete = new Intent(SubscriptionManager.REGISTRATION_COMPLETE);
    LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
  }

  @dagger.Subcomponent
  public interface Subcomponent extends AndroidInjector<RegistrationIntentService> {
    @dagger.Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<RegistrationIntentService> {
      @Override public abstract Subcomponent build();
    }
  }
}
