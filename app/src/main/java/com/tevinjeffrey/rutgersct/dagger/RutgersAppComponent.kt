package com.tevinjeffrey.rutgersct.dagger

import com.tevinjeffrey.rutgersct.RutgersCTApp

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule

@PerApp
@Component(modules = [
  RutgersAppModule::class,
  AndroidInjectionModule::class,
  AndroidSupportInjectionModule::class,
  ActivityBindingModule::class,
  ServiceBindingModule::class]
)
interface RutgersAppComponent {
  fun inject(app: RutgersCTApp)

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: RutgersCTApp): Builder
    fun build(): RutgersAppComponent
  }
}