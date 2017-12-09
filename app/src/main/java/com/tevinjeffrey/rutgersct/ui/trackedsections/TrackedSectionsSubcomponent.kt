package com.tevinjeffrey.rutgersct.ui.trackedsections

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface TrackedSectionsSubcomponent : AndroidInjector<TrackedSectionsFragment> {

  fun inject(trackedSectionsPresenter: TrackedSectionsViewModel)

  @Subcomponent.Builder
  abstract class Builder : AndroidInjector.Builder<TrackedSectionsFragment>() {
    abstract override fun build(): TrackedSectionsSubcomponent
  }
}
