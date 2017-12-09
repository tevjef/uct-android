package com.tevinjeffrey.rutgersct.dagger

import com.tevinjeffrey.rutgersct.ui.MainActivity
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsViewModel
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = [
  MainActivityModule::class,
  FragmentBindingModule::class]
)
interface MainActivitySubcomponent : AndroidInjector<MainActivity> {

  fun inject(trackedSectionsPresenter: TrackedSectionsViewModel)

  @Subcomponent.Builder
  abstract class Builder : AndroidInjector.Builder<MainActivity>() {
    abstract override fun build(): MainActivitySubcomponent
  }
}
