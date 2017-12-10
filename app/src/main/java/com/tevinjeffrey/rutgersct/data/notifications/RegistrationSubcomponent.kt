package com.tevinjeffrey.rutgersct.data.notifications

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface RegistrationSubcomponent : AndroidInjector<RegistrationIntentService> {
  @Subcomponent.Builder
  abstract class Builder : AndroidInjector.Builder<RegistrationIntentService>()
}