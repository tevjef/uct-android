package com.tevinjeffrey.rutgersct.ui.trackedsections;

import com.tevinjeffrey.rutgersct.TestModule;
import com.tevinjeffrey.rutgersct.data.rutgersapi.RetroRutgers;
import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.ui.base.View;
import com.tevinjeffrey.rutgersct.utils.TestConts;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import rx.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class TrackedSectionsPresenterImplTest {
    TrackedSectionsPresenterImpl trackedSectionsPresenterImpl;
    @Inject
    TrackedSectionsView trackedSectionsView;
    @Inject
    RetroRutgers retroRutgers;
    @Inject
    DatabaseHandler databaseHandler;

    @Before
    public void setUp() throws Exception {
        ObjectGraph og = ObjectGraph.create(new TestModule(), new MockModule());
        og.inject(this);
        trackedSectionsPresenterImpl = new TrackedSectionsPresenterImpl();
        og.inject(trackedSectionsPresenterImpl);
    }

    @Test
    public void GetView_NotNull_AfterAttach() throws Exception {
        trackedSectionsPresenterImpl.attachView(trackedSectionsView);
        assertNotNull(trackedSectionsPresenterImpl.getView());
    }

    @Test
    public void GetView_Null_AfterDetach() throws Exception {
        trackedSectionsPresenterImpl.attachView(trackedSectionsView);
        trackedSectionsPresenterImpl.detachView();
        assertNull(trackedSectionsPresenterImpl.getView());
    }

    @Test
    public void GetView_NotNull() throws Exception {
        trackedSectionsPresenterImpl.attachView(trackedSectionsView);
        View expected = trackedSectionsView;
        assertEquals(expected, trackedSectionsPresenterImpl.getView());
    }

    @Test
    public void ShowLoading_True_AfterSubscribe() throws Exception {
        trackedSectionsPresenterImpl.attachView(trackedSectionsView);
        trackedSectionsPresenterImpl.loadTrackedSections(true);
        verify(trackedSectionsView).showLoading(true);
    }

    @Test
    public void LoadTrackedSections_CompletesWithEmptyList() throws Exception {
        trackedSectionsPresenterImpl.attachView(trackedSectionsView);
        databaseHandler.addSectionToDb(TestConts.getPrimarySemesterRequest());
        when(retroRutgers.getTrackedSections(anyListOf(Request.class))).thenReturn(Observable.<Course.Section>empty());
        trackedSectionsPresenterImpl.loadTrackedSections(true);

        verify(trackedSectionsView).showLoading(true);
        verify(trackedSectionsView).showLoading(false);

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(trackedSectionsView).setData(captor.capture());
        assertEquals(0, captor.getValue().size());
        verify(trackedSectionsView, never()).showError(any(Exception.class));

    }


    @Test
    public void LoadTrackedSections_CompletesWithList() throws Exception {
        trackedSectionsPresenterImpl.attachView(trackedSectionsView);
        databaseHandler.addSectionToDb(TestConts.getPrimarySemesterRequest());
        when(retroRutgers.getTrackedSections(anyListOf(Request.class))).thenReturn(Observable.just(new Course.Section()));
        trackedSectionsPresenterImpl.loadTrackedSections(true);

        verify(trackedSectionsView).showLoading(true);
        verify(trackedSectionsView).showLoading(false);

        verify(trackedSectionsView).setData(anyListOf(Course.Section.class));
    }

    @Test
    public void LoadTrackedSections_CompletesWithErrors() throws Exception {
        trackedSectionsPresenterImpl.attachView(trackedSectionsView);
        databaseHandler.addSectionToDb(TestConts.getPrimarySemesterRequest());
        when(retroRutgers.getTrackedSections(anyListOf(Request.class))).thenReturn(Observable.<Course.Section>error(new Exception()));
        trackedSectionsPresenterImpl.loadTrackedSections(true);

        verify(trackedSectionsView).showLoading(true);
        verify(trackedSectionsView).showLoading(false);

        verify(trackedSectionsView).showError(any(Exception.class));
        verify(trackedSectionsView, never()).setData(anyListOf(Course.Section.class));
    }

    @Test
    public void IsLoading_True_WhenObservableHasNotTerminated() throws Exception {
        trackedSectionsPresenterImpl.attachView(trackedSectionsView);
        databaseHandler.addSectionToDb(TestConts.getPrimarySemesterRequest());
        when(retroRutgers.getTrackedSections(anyListOf(Request.class))).thenReturn(Observable.<Course.Section>never());
        trackedSectionsPresenterImpl.loadTrackedSections(true);

        assertTrue(trackedSectionsPresenterImpl.isLoading());
    }

    @Test
    public void IsLoading_False_WhenObservableHasTerminated() throws Exception {
        trackedSectionsPresenterImpl.attachView(trackedSectionsView);
        databaseHandler.addSectionToDb(TestConts.getPrimarySemesterRequest());
        when(retroRutgers.getTrackedSections(anyListOf(Request.class))).thenReturn(Observable.<Course.Section>empty());
        trackedSectionsPresenterImpl.loadTrackedSections(true);

        assertFalse(trackedSectionsPresenterImpl.isLoading());
    }


    @Module(injects = {
            TrackedSectionsPresenterImplTest.class
    }

            , overrides = true
            , library = true
            , complete = false)
    public class MockModule {
        @Mock
        TrackedSectionsView mockTrackedSectionsView;

        public MockModule() {
            MockitoAnnotations.initMocks(this);
        }

        @Provides
        @Singleton
        public RetroRutgers providesRetroRutgers() {
            //Inline mock
            return Mockito.mock(RetroRutgers.class);
        }
        @Provides
        @Singleton
        public TrackedSectionsView providesTrackedSectionsView() {
            //Injected mock
            return mockTrackedSectionsView;
        }
    }
}