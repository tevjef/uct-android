package com.tevinjeffrey.rutgersct.ui;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;
import com.tevinjeffrey.rutgersct.RutgersCTModule;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.database.RutgersApiTestConts;
import com.tevinjeffrey.rutgersct.ui.base.View;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsView;
import com.tevinjeffrey.rutgersct.utils.PreferenceUtils;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class TrackedSectionsPresenterImplTest {
    RutgersApiTestConts testConts = new RutgersApiTestConts();
    TrackedSectionsPresenterImpl trackedSectionsPresenterImpl;

    @Inject
    TrackedSectionsView mockTrackedSectionsView;

    @Before
    public void setUp() throws Exception {
        ObjectGraph og = ObjectGraph.create(new TestRutgersCTModule());
        og.inject(this);
        trackedSectionsPresenterImpl = new TrackedSectionsPresenterImpl();
        og.inject(trackedSectionsPresenterImpl);

    }

    //Android main thread stub need to be injected, revisit when you learn dependacy injection
    public void testLoadTrackedSections() throws Exception {
        /*Course.Section section = mock(Course.Section.class);
        List<Course.Section> sectionList = new ArrayList<>();
        sectionList.add(section);

        when(testDatabaseHandler.getAllSections()).thenReturn(Observable.just(testConts.createTrackedSections()));
        when(testRutgersApi.getTrackedSections(testConts.createTrackedSections())).thenReturn(Observable.just(section));
        when(testRutgersApi.getDefaultClient()).thenReturn(mock(OkHttpClient.class));*/

        trackedSectionsPresenterImpl.attachView(mockTrackedSectionsView);

        assertTrue(trackedSectionsPresenterImpl.getView() != null);
        /*verify(testTrackedSectionsView)
        trackedSectionsPresenterImpl.loadTrackedSections(true);

        verify(testRutgersApi).getTrackedSections(testConts.createTrackedSections());
        verify(testDatabaseHandler).getAllSections();*/

    }

    @Test
    public void testAttachView() throws Exception {
        trackedSectionsPresenterImpl.attachView(mockTrackedSectionsView);
        assertTrue(trackedSectionsPresenterImpl.getView() != null);
    }


    @Test
    public void testDetachView() throws Exception {
        trackedSectionsPresenterImpl.attachView(mockTrackedSectionsView);
        trackedSectionsPresenterImpl.detachView(false);
        assertTrue(trackedSectionsPresenterImpl.getView() == null);
    }

    @Test
    public void testGetView() throws Exception {
        trackedSectionsPresenterImpl.attachView(mockTrackedSectionsView);

        View expected = mockTrackedSectionsView;
        assertEquals(expected, trackedSectionsPresenterImpl.getView());
    }

    @Module(
            injects = TrackedSectionsPresenterImplTest.class
            , overrides = true
            ,library = true
            ,complete = false)
    static class TestRutgersCTModule {

        @Provides
        @Singleton
        public Context provideContext() {
            return mock(Context.class);
        }

        @Provides
        @Singleton
        public DatabaseHandler providesDatabaseHandler(Bus bus) {
            return mock(DatabaseHandler.class);
        }

        @Provides
        @Singleton
        public Bus providesEventBus() {
            return mock(Bus.class);
        }

        @Provides
        @Singleton
        public PreferenceUtils providesPreferenceUtils(Context context) {
            return new PreferenceUtils(context);
        }

        @Provides
        @Singleton
        public OkHttpClient providesOkHttpClient(Context context) {
            return mock(OkHttpClient.class);
        }

        @Provides
        @Singleton
        public TrackedSectionsView providesTrackedSectionView() {
            return mock(TrackedSectionsView.class);
        }
    }

}