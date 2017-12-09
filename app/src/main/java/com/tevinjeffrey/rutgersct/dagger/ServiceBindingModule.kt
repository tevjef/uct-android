package com.tevinjeffrey.rutgersct.dagger

import android.app.Service
import com.tevinjeffrey.rutgersct.data.notifications.RegistrationIntentService
import com.tevinjeffrey.rutgersct.data.notifications.UCTFirebaseMessagingService
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ServiceKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [
  UCTFirebaseMessagingService.Subcomponent::class,
  RegistrationIntentService.Subcomponent::class]
)
abstract class ServiceBindingModule {
  @Binds
  @IntoMap
  @ServiceKey(UCTFirebaseMessagingService::class)
  abstract fun bindUCTFirebaseMessagingServiceInjectorFactory(
      builder: UCTFirebaseMessagingService.Subcomponent.Builder): AndroidInjector.Factory<out Service>

  @Binds
  @IntoMap
  @ServiceKey(RegistrationIntentService::class)
  abstract fun bindRegistrationIntentServiceInjectorFactory(
      builder: RegistrationIntentService.Subcomponent.Builder): AndroidInjector.Factory<out Service>
}
