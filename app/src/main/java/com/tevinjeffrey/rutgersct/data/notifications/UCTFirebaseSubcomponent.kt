package com.tevinjeffrey.rutgersct.data.notifications

import dagger.android.AndroidInjector

@dagger.Subcomponent
interface UCTFirebaseSubcomponent : AndroidInjector<UCTFirebaseMessagingService> {

  @dagger.Subcomponent.Builder
  abstract class Builder : AndroidInjector.Builder<UCTFirebaseMessagingService>()
}
