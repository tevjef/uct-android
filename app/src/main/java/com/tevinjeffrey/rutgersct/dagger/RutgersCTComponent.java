package com.tevinjeffrey.rutgersct.dagger;

import com.tevinjeffrey.rutgersct.RutgersCTApp;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

@PerApp
@Component(modules = {
    RutgersCTModule.class,
    AndroidInjectionModule.class,
    AndroidSupportInjectionModule.class,
    ActivityBindingModule.class,
    ServiceBindingModule.class
})
public interface RutgersCTComponent {
  void inject(RutgersCTApp app);

  @Component.Builder interface Builder {

    @BindsInstance Builder application(RutgersCTApp application);

    RutgersCTComponent build();
  }
}