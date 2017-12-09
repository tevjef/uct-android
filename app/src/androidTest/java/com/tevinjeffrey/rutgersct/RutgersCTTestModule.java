package com.tevinjeffrey.rutgersct;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RutgersCTTestModule {
  @Provides
  @Singleton
  public Bus providesEventBus() {
    return new Bus();
  }
}
