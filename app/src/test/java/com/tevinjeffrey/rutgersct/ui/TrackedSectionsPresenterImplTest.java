package com.tevinjeffrey.rutgersct.ui;

import com.squareup.okhttp.OkHttpClient;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.database.RutgersApiTestConts;
import com.tevinjeffrey.rutgersct.rutgersapi.RutgersApi;
import com.tevinjeffrey.rutgersct.ui.base.View;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created by Tevin on 7/22/2015.
 */
public class TrackedSectionsPresenterImplTest {
    RutgersApiTestConts testConts = new RutgersApiTestConts();

    TrackedSectionsPresenterImpl trackedSectionsPresenterImpl;

    RutgersApi testRutgersApi;
    DatabaseHandler testDatabaseHandler;
    TrackedSectionsView testTrackedSectionsView;

    @Mock
    OkHttpClient client;

    @Before
    public void setUp() throws Exception {
        testRutgersApi = mock(RutgersApi.class);
        testDatabaseHandler = mock(DatabaseHandler.class);
        testTrackedSectionsView = mock(TrackedSectionsView.class);


    }

    //Android main thread stub need to be injected, revisit when you learn dependacy injection
    public void testLoadTrackedSections() throws Exception {
        /*Course.Section section = mock(Course.Section.class);
        List<Course.Section> sectionList = new ArrayList<>();
        sectionList.add(section);

        when(testDatabaseHandler.getAllSections()).thenReturn(Observable.just(testConts.createTrackedSections()));
        when(testRutgersApi.getTrackedSections(testConts.createTrackedSections())).thenReturn(Observable.just(section));
        when(testRutgersApi.getClient()).thenReturn(mock(OkHttpClient.class));*/

        trackedSectionsPresenterImpl = new TrackedSectionsPresenterImpl(testRutgersApi, testDatabaseHandler);
        trackedSectionsPresenterImpl.attachView(testTrackedSectionsView);

        assertTrue(trackedSectionsPresenterImpl.getView() != null);
        /*verify(testTrackedSectionsView)
        trackedSectionsPresenterImpl.loadTrackedSections(true);

        verify(testRutgersApi).getTrackedSections(testConts.createTrackedSections());
        verify(testDatabaseHandler).getAllSections();*/

    }

    @Test
    public void testAttachView() throws Exception {
        trackedSectionsPresenterImpl = new TrackedSectionsPresenterImpl(testRutgersApi, testDatabaseHandler);
        trackedSectionsPresenterImpl.attachView(testTrackedSectionsView);
        assertTrue(trackedSectionsPresenterImpl.getView() != null);
    }


    @Test
    public void testDetachView() throws Exception {
        trackedSectionsPresenterImpl = new TrackedSectionsPresenterImpl(testRutgersApi, testDatabaseHandler);
        trackedSectionsPresenterImpl.attachView(testTrackedSectionsView);
        trackedSectionsPresenterImpl.detachView(false);
        assertTrue(trackedSectionsPresenterImpl.getView() == null);
    }

    @Test
    public void testGetView() throws Exception {
        trackedSectionsPresenterImpl = new TrackedSectionsPresenterImpl(testRutgersApi, testDatabaseHandler);
        trackedSectionsPresenterImpl.attachView(testTrackedSectionsView);

        View expected = testTrackedSectionsView;
        assertEquals(expected, trackedSectionsPresenterImpl.getView());
    }
}