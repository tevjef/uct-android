package com.tevinjeffrey.rutgersct;

import com.squareup.otto.Bus;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.database.DatabaseHandlerImpl;
import com.tevinjeffrey.rutgersct.ui.TrackedSectionsFragmentTest;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                TrackedSectionsFragmentTest.class,
}, overrides = true, library = true)

public class RutgersCTTestModule {
    @Provides
    @Singleton
    public DatabaseHandler providesDatabaseHandler(Bus bus) {
        return new DatabaseHandlerImpl(bus);
    }

    @Provides
    @Singleton
    public Bus providesEventBus() {
        return new Bus();
    }
}
