package com.tevinjeffrey.rutgersct.data.rutgersapi;

import com.tevinjeffrey.rutgersct.TestModule;
import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Subject;
import com.tevinjeffrey.rutgersct.utils.BackgroundThread;
import com.tevinjeffrey.rutgersct.utils.TestConts;
import dagger.ObjectGraph;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;

import static org.junit.Assert.assertTrue;

public class RetroRutgersTest {

  RetroRutgers retroRutgers;

  @Inject
  @BackgroundThread
  Scheduler backgroundThread;

  @Inject
  RetroRutgersService rutgersService;

  @Before
  public void setUp() throws Exception {
    ObjectGraph og = ObjectGraph.create(new TestModule());
    og.inject(this);
    retroRutgers = new RetroRutgers(rutgersService, backgroundThread);
  }

  @Test
  public void testNewark() {
    getSubjectAndCourses(TestConts.requestNewark);
  }

  @Test
  public void testNewBruswick() {
    getSubjectAndCourses(TestConts.requestBrunswick);
  }

  @Test
  public void testCamden() {
    getSubjectAndCourses(TestConts.requestCamden);
  }

  @Test
  public void testFall() {
    getSubjectAndCourses(TestConts.requestAllFall);
  }

  @Test
  public void testWinter() {
    getSubjectAndCourses(TestConts.requestAllWinter);
  }

  @Test
  public void testSpring() {
    getSubjectAndCourses(TestConts.requestAllSpring);
  }

  @Test
  public void testSummer() {
    getSubjectAndCourses(TestConts.requestAllSummer);
  }

  @Test
  public void testPrimarySemester() {
    getSubjectAndCourses(TestConts.getPrimarySemesterRequest());
  }

  private void getSubjectAndCourses(Request request) {
    testSubject(request);
    testCourse(request);
  }

  private void testSubject(Request request) {
    Observable.from(retroRutgers.getSubjects(request)
        .toBlocking().first()).forEach(new Action1<Subject>() {
      @Override
      public void call(Subject subject) {
        assertTrue(subjectFailCases(subject));
      }
    });
  }

  private void testCourse(Request request) {
    Observable.from(retroRutgers.getCourses(request)
        .toBlocking().first()).forEach(new Action1<Course>() {
      @Override
      public void call(Course course) {
        assertTrue(courseFailCases(course));
      }
    });
  }

  public boolean courseFailCases(Course course) {
    Course.Section section = course.getSections().get(0);
    return course.getSubject() != null &&
        course.getCourseNumber() != null &&
        section.getIndex() != null &&
        section.getNumber() != null &&
        section.getExamCode() != null;
  }

  public boolean subjectFailCases(Subject subject) {
    return subject.getDescription() != null &&
        subject.getCode() != null;
  }
}