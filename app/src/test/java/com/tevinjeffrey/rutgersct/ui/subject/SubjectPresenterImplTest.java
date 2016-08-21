package com.tevinjeffrey.rutgersct.ui.subject;

import com.tevinjeffrey.rutgersct.TestModule;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.data.rutgersapi.RetroRutgers;
import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Subject;
import com.tevinjeffrey.rutgersct.ui.base.View;
import com.tevinjeffrey.rutgersct.utils.TestConts;

import org.junit.Before;
import org.junit.Test;
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


public class SubjectPresenterImplTest {
    SubjectPresenterImpl subjectPresenterImpl;
    @Inject
    SubjectView subjectView;
    @Inject
    RetroRutgers retroRutgers;
    @Inject
    DatabaseHandler databaseHandler;

    @Before
    public void setUp() throws Exception {
        ObjectGraph og = ObjectGraph.create(new TestModule(), new MockModule());
        og.inject(this);
        subjectPresenterImpl = new SubjectPresenterImpl(TestConts.getPrimarySemesterRequest());
        og.inject(subjectPresenterImpl);
    }

    @Test
    public void GetView_NotNull_AfterAttach() throws Exception {
        subjectPresenterImpl.attachView(subjectView);
        assertNotNull(subjectPresenterImpl.getView());
    }

    @Test
    public void GetView_Null_AfterDetach() throws Exception {
        subjectPresenterImpl.attachView(subjectView);
        subjectPresenterImpl.detachView();
        assertNull(subjectPresenterImpl.getView());
    }

    @Test
    public void GetView_NotNull() throws Exception {
        subjectPresenterImpl.attachView(subjectView);
        View expected = subjectView;
        assertEquals(expected, subjectPresenterImpl.getView());
    }

    @Test
    public void ShowLoading_True_AfterSubscribe() throws Exception {
        when(retroRutgers.getSubjects(any(Request.class))).thenReturn(Observable.<List<Subject>>empty());

        subjectPresenterImpl.attachView(subjectView);
        subjectPresenterImpl.loadSubjects(true);
        verify(subjectView).showLoading(true);
    }

    @Test
    public void LoadSubjects_CompletesWithList() throws Exception {
        subjectPresenterImpl.attachView(subjectView);
        databaseHandler.addSectionToDb(TestConts.getPrimarySemesterRequest());
        when(retroRutgers.getSubjects(any(Request.class))).thenReturn(Observable.just(new Subject()).toList());
        subjectPresenterImpl.loadSubjects(true);

        verify(subjectView).showLoading(true);
        verify(subjectView).showLoading(false);

        verify(subjectView).setData(anyListOf(Subject.class));
    }

    @Test
    public void LoadSubjects_CompletesWithErrors() throws Exception {
        subjectPresenterImpl.attachView(subjectView);
        databaseHandler.addSectionToDb(TestConts.getPrimarySemesterRequest());
        when(retroRutgers.getSubjects(any(Request.class))).thenReturn(Observable.<List<Subject>>error(new Exception()));
        subjectPresenterImpl.loadSubjects(true);

        verify(subjectView).showLoading(true);
        verify(subjectView).showLoading(false);

        verify(subjectView).showError(any(Exception.class));
        verify(subjectView, never()).setData(anyListOf(Subject.class));
    }

    @Test
    public void IsLoading_True_WhenObservableHasNotTerminated() throws Exception {
        subjectPresenterImpl.attachView(subjectView);
        databaseHandler.addSectionToDb(TestConts.getPrimarySemesterRequest());
        when(retroRutgers.getSubjects(any(Request.class))).thenReturn(Observable.<List<Subject>>never());
        subjectPresenterImpl.loadSubjects(true);

        assertTrue(subjectPresenterImpl.isLoading());
    }

    @Test
    public void IsLoading_False_WhenObservableHasTerminated() throws Exception {
        subjectPresenterImpl.attachView(subjectView);
        databaseHandler.addSectionToDb(TestConts.getPrimarySemesterRequest());
        when(retroRutgers.getSubjects(any(Request.class))).thenReturn(Observable.<List<Subject>>empty());
        subjectPresenterImpl.loadSubjects(true);

        assertFalse(subjectPresenterImpl.isLoading());
    }


    @Module(injects = {
            SubjectPresenterImplTest.class
    }

            , overrides = true
            , library = true
            , complete = false)
    public class MockModule {
        @Mock
        SubjectView mockSubjectView;

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
        public SubjectView providesSubjectView() {
            //Injected mock
            return mockSubjectView;
        }
    }
}