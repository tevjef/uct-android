package com.tevinjeffrey.rutgersct.ui;

import com.squareup.otto.Bus;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.rutgersapi.RetroRutgers;
import com.tevinjeffrey.rutgersct.ui.base.View;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsView;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class TrackedSectionsPresenterImplTest {
    TrackedSectionsPresenterImpl trackedSectionsPresenterImpl;

    @Inject
    TrackedSectionsView mockTrackedSectionsView;

    @Before
    public void setUp() throws Exception {
        ObjectGraph og = ObjectGraph.create(new TestModule());
        og.inject(this);
        trackedSectionsPresenterImpl = new TrackedSectionsPresenterImpl();
        og.inject(trackedSectionsPresenterImpl);
    }

    @Test
    public void testAttachView() throws Exception {
        trackedSectionsPresenterImpl.attachView(mockTrackedSectionsView);
        assertNotNull(trackedSectionsPresenterImpl.getView());
    }


    @Test
    public void testDetachView() throws Exception {
        trackedSectionsPresenterImpl.attachView(mockTrackedSectionsView);
        assertNotNull(trackedSectionsPresenterImpl.getView());
        trackedSectionsPresenterImpl.detachView();
        assertNull(trackedSectionsPresenterImpl.getView());
    }

    @Test
    public void testGetView() throws Exception {
        trackedSectionsPresenterImpl.attachView(mockTrackedSectionsView);
        View expected = mockTrackedSectionsView;
        assertEquals(expected, trackedSectionsPresenterImpl.getView());
    }

    @Module(
            injects = {TrackedSectionsPresenterImplTest.class, TrackedSectionsPresenterImpl.class}
            , overrides = true
            ,library = true
            ,complete = false)
    static class TestModule {
        @Provides
        @Singleton
        public Bus provideBus() {
            return mock(Bus.class);
        }

        @Provides
        @Singleton
        public RetroRutgers provideRutgers() {
            return mock(RetroRutgers.class);
        }

        @Provides
        @Singleton
        public DatabaseHandler provideDatabaseHandler() {
            return mock(DatabaseHandler.class);
        }

        @Provides
        @Singleton
        public TrackedSectionsView provideTrackedSectionsView() {
            return mock(TrackedSectionsView.class);
        }
    }

}