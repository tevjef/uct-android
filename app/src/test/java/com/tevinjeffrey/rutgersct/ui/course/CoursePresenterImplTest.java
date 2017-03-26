package com.tevinjeffrey.rutgersct.ui.course;

import com.tevinjeffrey.rutgersct.TestModule;
import com.tevinjeffrey.rutgersct.data.rutgersapi.RetroRutgers;
import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.ui.base.View;
import com.tevinjeffrey.rutgersct.utils.TestConts;
import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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

public class CoursePresenterImplTest {
  CoursePresenterImpl coursePresenterImpl;
  @Inject
  CourseView courseView;
  @Inject
  RetroRutgers retroRutgers;
  @Inject
  DatabaseHandler databaseHandler;

  @Before
  public void setUp() throws Exception {
    ObjectGraph og = ObjectGraph.create(new TestModule(), new MockModule());
    og.inject(this);
    coursePresenterImpl = new CoursePresenterImpl(TestConts.getPrimarySemesterRequest());
    og.inject(coursePresenterImpl);
  }

  @Test
  public void GetView_NotNull_AfterAttach() throws Exception {
    coursePresenterImpl.attachView(courseView);
    assertNotNull(coursePresenterImpl.getView());
  }

  @Test
  public void GetView_Null_AfterDetach() throws Exception {
    coursePresenterImpl.attachView(courseView);
    coursePresenterImpl.detachView();
    assertNull(coursePresenterImpl.getView());
  }

  @Test
  public void GetView_NotNull() throws Exception {
    coursePresenterImpl.attachView(courseView);
    View expected = courseView;
    assertEquals(expected, coursePresenterImpl.getView());
  }

  @Test
  public void ShowLoading_True_AfterSubscribe() throws Exception {
    when(retroRutgers.getCourses(any(Request.class))).thenReturn(Observable.<List<Course>>empty());

    coursePresenterImpl.attachView(courseView);
    coursePresenterImpl.loadCourses(true);
    verify(courseView).showLoading(true);
  }

  @Test
  public void LoadCourses_CompletesWithList() throws Exception {
    coursePresenterImpl.attachView(courseView);
    databaseHandler.addSectionToDb(TestConts.getPrimarySemesterRequest());
    when(retroRutgers.getCourses(any(Request.class))).thenReturn(Observable
        .just(new Course())
        .toList());
    coursePresenterImpl.loadCourses(true);

    verify(courseView).showLoading(true);
    verify(courseView).showLoading(false);

    verify(courseView).setData(anyListOf(Course.class));
  }

  @Test
  public void LoadCourses_CompletesWithErrors() throws Exception {
    coursePresenterImpl.attachView(courseView);
    databaseHandler.addSectionToDb(TestConts.getPrimarySemesterRequest());
    when(retroRutgers.getCourses(any(Request.class))).thenReturn(Observable.<List<Course>>error(new Exception()));
    coursePresenterImpl.loadCourses(true);

    verify(courseView).showLoading(true);
    verify(courseView).showLoading(false);

    verify(courseView).showError(any(Exception.class));
    verify(courseView, never()).setData(anyListOf(Course.class));
  }

  @Test
  public void IsLoading_True_WhenObservableHasNotTerminated() throws Exception {
    coursePresenterImpl.attachView(courseView);
    databaseHandler.addSectionToDb(TestConts.getPrimarySemesterRequest());
    when(retroRutgers.getCourses(any(Request.class))).thenReturn(Observable.<List<Course>>never());
    coursePresenterImpl.loadCourses(true);

    assertTrue(coursePresenterImpl.isLoading());
  }

  @Test
  public void IsLoading_False_WhenObservableHasTerminated() throws Exception {
    coursePresenterImpl.attachView(courseView);
    databaseHandler.addSectionToDb(TestConts.getPrimarySemesterRequest());
    when(retroRutgers.getCourses(any(Request.class))).thenReturn(Observable.<List<Course>>empty());
    coursePresenterImpl.loadCourses(true);

    assertFalse(coursePresenterImpl.isLoading());
  }

  @Module(injects = {
      CoursePresenterImplTest.class
  }

      , overrides = true
      , library = true
      , complete = false)
  public class MockModule {
    @Mock
    CourseView mockCourseView;

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
    public CourseView providesCourseView() {
      //Injected mock
      return mockCourseView;
    }
  }
}