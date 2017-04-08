package com.tevinjeffrey.rutgersct;

import com.squareup.otto.Bus;
import com.tevinjeffrey.rutgersct.ui.TrackedSectionsFragmentTest;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(
    injects = {
        TrackedSectionsFragmentTest.class,
    }, overrides = true, library = true)

public class RutgersCTTestModule {
  @Provides
  @Singleton
  public Bus providesEventBus() {
    return new Bus();
  }
}
