package com.tevinjeffrey.rutgersct.dagger

import android.app.Service
import com.tevinjeffrey.rutgersct.data.notifications.RegistrationIntentService
import com.tevinjeffrey.rutgersct.data.notifications.RegistrationSubcomponent
import com.tevinjeffrey.rutgersct.data.notifications.UCTFirebaseMessagingService
import com.tevinjeffrey.rutgersct.data.notifications.UCTFirebaseSubcomponent
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ServiceKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [
  UCTFirebaseSubcomponent::class,
  RegistrationSubcomponent::class]
)
abstract class ServiceBindingModule {
  @Binds
  @IntoMap
  @ServiceKey(UCTFirebaseMessagingService::class)
  abstract fun bindUCTFirebaseMessagingServiceInjectorFactory(
      builder: UCTFirebaseSubcomponent.Builder): AndroidInjector.Factory<out Service>

  @Binds
  @IntoMap
  @ServiceKey(RegistrationIntentService::class)
  abstract fun bindRegistrationIntentServiceInjectorFactory(
      builder: RegistrationSubcomponent.Builder): AndroidInjector.Factory<out Service>
}
