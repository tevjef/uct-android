package com.tevinjeffrey.rutgersct.database;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.tevinjeffrey.rutgersct.rutgersapi.RetroRutgers;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Subject;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

import static org.junit.Assert.assertTrue;

public class RutgersApiImplTest {

    OkHttpClient client = new OkHttpClient();

    List<TrackedSection> trackedSections;

    RetroRutgers retroRutgers = new RetroRutgers(client);

    RutgersApiTestConts conts = new RutgersApiTestConts();

    private void initOkHttp() {
        File httpCacheDir = new File("/", "test-cache");
        long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDir, httpCacheSize);
        client.networkInterceptors().add(RutgersApiTestConts.REWRITE_CACHE_CONTROL_INTERCEPTOR);
        client.setCache(cache);
    }

    @Before
    public void setUp() throws Exception {
        initOkHttp();
        createTrackedSections();

    }

    private void createTrackedSections() {
        trackedSections = new ArrayList<>();
        trackedSections.add(conts.t1);
        trackedSections.add(conts.t2);
        trackedSections.add(conts.t3);
        trackedSections.add(conts.t4);
        trackedSections.add(conts.t5);
        trackedSections.add(conts.t6);
    }

    @Test
    public void testNewark() {
        getSubjectAndCourses(conts.requestNewark);

    }

    @Test
    public void testNewBruswick() {
        getSubjectAndCourses(conts.requestBrunswick);

    }

    @Test
    public void testCamden() {
        getSubjectAndCourses(conts.requestCamden);

    }

    @Test
    public void testFall() {
        getSubjectAndCourses(conts.requestAllFall);

    }

    @Test
    public void testWinter() {
        getSubjectAndCourses(conts.requestAllWinter);
    }

    @Test
    public void testSpring() {
        getSubjectAndCourses(conts.requestAllSpring);
    }

    @Test
    public void testSummer() {
        getSubjectAndCourses(conts.requestAllSummer);
    }

    @Test
    public void testPrimarySemester() {
        getSubjectAndCourses(conts.getPrimarySemesterRequest());
    }

    @Test
    public void testSecondarySemester() {
        getSubjectAndCourses(conts.getSecondarySemesterRequest());
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