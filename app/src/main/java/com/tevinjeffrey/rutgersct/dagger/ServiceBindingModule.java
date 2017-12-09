package com.tevinjeffrey.rutgersct.dagger;

import android.app.Service;

import com.tevinjeffrey.rutgersct.data.notifications.RegistrationIntentService;
import com.tevinjeffrey.rutgersct.data.notifications.UCTFirebaseMessagingService;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.ServiceKey;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {
    UCTFirebaseMessagingService.Subcomponent.class,
    RegistrationIntentService.Subcomponent.class
})
public abstract class ServiceBindingModule {
  @Binds
  @IntoMap
  @ServiceKey(UCTFirebaseMessagingService.class)
  abstract AndroidInjector.Factory<? extends Service>
  bindUCTFirebaseMessagingServiceInjectorFactory(UCTFirebaseMessagingService.Subcomponent.Builder builder);

  @Binds
  @IntoMap
  @ServiceKey(RegistrationIntentService.class)
  abstract AndroidInjector.Factory<? extends Service>
  bindRegistrationIntentServiceInjectorFactory(RegistrationIntentService.Subcomponent.Builder builder);
}
