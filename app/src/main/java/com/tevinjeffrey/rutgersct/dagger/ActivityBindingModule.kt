package com.tevinjeffrey.rutgersct.dagger

import android.app.Activity

import com.tevinjeffrey.rutgersct.ui.MainActivity

import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Module(subcomponents = [MainActivitySubcomponent::class])
abstract class ActivityBindingModule {
  @Binds
  @IntoMap
  @ActivityKey(MainActivity::class)
  abstract fun bindMainActivityInjectorFactory(
      builder: MainActivitySubcomponent.Builder): AndroidInjector.Factory<out Activity>
}