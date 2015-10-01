package com.tevinjeffrey.rutgersct.ui.sectioninfo;

import com.tevinjeffrey.rmp.common.Parameter;
import com.tevinjeffrey.rmp.common.Professor;
import com.tevinjeffrey.rmp.common.RMP;
import com.tevinjeffrey.rutgersct.TestModule;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.rutgersapi.RetroRutgers;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Subject;
import com.tevinjeffrey.rutgersct.ui.base.View;

import com.tevinjeffrey.rutgersct.utils.TestConts;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class SectionInfoPresenterImplTest {
    SectionInfoPresenterImpl sectioninfoPresenterImpl;
    @Inject
    SectionInfoView sectioninfoView;
    @Inject
    RetroRutgers retroRutgers;
    @Inject
    DatabaseHandler databaseHandler;
    @Inject
    Course.Section section;

    @Before
    public void setUp() throws Exception {
        ObjectGraph og = ObjectGraph.create(new TestModule(), new MockModule());
        og.inject(this);
        sectioninfoPresenterImpl = new SectionInfoPresenterImpl(section);
        og.inject(sectioninfoPresenterImpl);
    }

    @Test
    public void GetView_NotNull_AfterAttach() throws Exception {
        sectioninfoPresenterImpl.attachView(sectioninfoView);
        assertNotNull(sectioninfoPresenterImpl.getView());
    }

    @Test
    public void GetView_Null_AfterDetach() throws Exception {
        sectioninfoPresenterImpl.attachView(sectioninfoView);
        sectioninfoPresenterImpl.detachView();
        assertNull(sectioninfoPresenterImpl.getView());
    }

    @Test
    public void GetView_NotNull() throws Exception {
        sectioninfoPresenterImpl.attachView(sectioninfoView);
        View expected = sectioninfoView;
        assertEquals(expected, sectioninfoPresenterImpl.getView());
    }

    @Test
    public void LoadRMP_ShowRatingsLayoutCalled() throws Exception {
        sectioninfoPresenterImpl.attachView(sectioninfoView);
        sectioninfoPresenterImpl.loadRMP();
        verify(sectioninfoView).showRatingsLayout();
    }

    @Test
    public void LoadRMP_HideRatingLayoutCalled() throws Exception {
        sectioninfoPresenterImpl.attachView(sectioninfoView);
        sectioninfoPresenterImpl.loadRMP();
        verify(sectioninfoView).showRatingsLayout();
    }

    @Module(injects = {
            SectionInfoPresenterImplTest.class
    }
            , overrides = true
            , library = true
            , complete = false)
    public class MockModule {
        @Mock
        SectionInfoView mockSectionInfoView;

        public MockModule() {
            MockitoAnnotations.initMocks(this);
        }

        @Provides
        @Singleton
        public RetroRutgers providesRetroRutgers() {
            //Inline mock
            RetroRutgers retroRutgers = Mockito.mock(RetroRutgers.class);
            Subject subject = mock(Subject.class);
            when(subject.getDescription()).thenReturn("Computer Science");
            when(retroRutgers.getSubjectFromJson(anyString())).thenReturn(subject);
            return retroRutgers;
        }
        @Provides
        @Singleton
        public SectionInfoView providesSectionInfoView() {
            //Injected mock
            return mockSectionInfoView;
        }
        @Provides
        @Singleton
        public Course.Section providesSection() {
            Course.Section section = mock(Course.Section.class);
            Course course = mock(Course.class);
            when(course.getCourseNumber()).thenReturn("198");
            when(course.getSubject()).thenReturn("Computer Science");
            when(section.getInstructors()).thenReturn(Arrays.asList(new Course.Section.Instructors("Carolla")));
            when(section.getRequest()).thenReturn(TestConts.getPrimarySemesterRequest());
            when(section.getCourse()).thenReturn(course);
            return section;
        }

        @Provides
        @Singleton
        public RMP providesRMP() {
            RMP rmp = mock(RMP.class);
            Professor professor = mock(Professor.class);
            when(professor.getFirstName()).thenReturn("Joyce");
            when(professor.getLastName()).thenReturn("Carolla");
            when(rmp.getProfessor(any(Parameter.class))).thenReturn(Observable.just(professor));
            return rmp;
        }
    }
}