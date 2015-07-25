package com.tevinjeffrey.rutgersct.database;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Subject;
import com.tevinjeffrey.rutgersct.rutgersapi.RutgersApiImpl;
import com.tevinjeffrey.rutgersct.rutgersapi.RutgersApiImpl;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RutgersApiImplTest {

    OkHttpClient client;

    List<TrackedSection> trackedSections;

    RutgersApiTestConts conts = new RutgersApiTestConts();

    private void initOkHttp() {
        client = new OkHttpClient();
        File httpCacheDir = new File("/", "test-cache");
        long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDir, httpCacheSize);
        client.networkInterceptors().add(RutgersApiTestConts.REWRITE_CACHE_CONTROL_INTERCEPTOR);
        client.setCache(cache);
    }

    @Before
    public void setUp() throws Exception {
        initOkHttp();
        RutgersApiImpl.init();
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
    public void testRutgersApiCreate() {
        RutgersApiImpl api = new RutgersApiImpl(client);
        assertNotNull(api);
    }


    @Test
    public void testGetTrackedSections() {
        final RutgersApiImpl api = new RutgersApiImpl(client);

        Observable<Request> requestObservable = api.createRequestObservableFromTrackedSections(trackedSections);
        requestObservable.toBlocking().forEach(new Action1<Request>() {
            @Override
            public void call(Request request) {
                testCourse(api, request);
            }
        });
    }

    @Test
    public void testNewark() {
        RutgersApiImpl api = new RutgersApiImpl(client);

        getSubjectAndCourses(api, conts.requestNewark);

    }

    @Test
    public void testNewBruswick() {
        RutgersApiImpl api = new RutgersApiImpl(client);

        getSubjectAndCourses(api, conts.requestBrunswick);

    }

    @Test
    public void testCamden() {
        RutgersApiImpl api = new RutgersApiImpl(client);

        getSubjectAndCourses(api, conts.requestCamden);

    }

    @Test
    public void testFall() {
        RutgersApiImpl api = new RutgersApiImpl(client);

        getSubjectAndCourses(api, conts.requestAllFall);

    }

    @Test
    public void testWinter() {
        RutgersApiImpl api = new RutgersApiImpl(client);

        getSubjectAndCourses(api, conts.requestAllWinter);
    }

    @Test
    public void testSpring() {
        RutgersApiImpl api = new RutgersApiImpl(client);

        getSubjectAndCourses(api, conts.requestAllSpring);
    }

    @Test
    public void testSummer() {
        RutgersApiImpl api = new RutgersApiImpl(client);

        getSubjectAndCourses(api, conts.requestAllSummer);
    }

    @Test
    public void testPrimarySemester() {
        RutgersApiImpl api = new RutgersApiImpl(client);

        getSubjectAndCourses(api, conts.getPrimarySemesterRequest());
    }

    @Test
    public void testSecondarySemester() {
        RutgersApiImpl api = new RutgersApiImpl(client);

        getSubjectAndCourses(api, conts.getSecondarySemesterRequest());
    }

    private void getSubjectAndCourses(RutgersApiImpl api, Request request) {
        testSubject(api, request);
        testCourse(api, request);
    }

    private void testSubject(RutgersApiImpl api, Request request) {

        System.out.printf("Subject url: %s%n", api.createSubjectUrlFromRequest(request));

        Observable<String> subjectResponseFromServer = api.getSubjectResponseFromServer(request);

        testServerResponseString("Subject", subjectResponseFromServer);

        Observable<List<Subject>> subjectListObservable = api.createSubjects(subjectResponseFromServer.toBlocking().first());

        testListObservable("Subject", subjectListObservable);

        api.getSubjects(request)
                .toBlocking().forEach(new Action1<Subject>() {
            @Override
            public void call(Subject subject) {
                assertTrue(subjectFailCases(subject));
            }
        });
    }

    private void testCourse(RutgersApiImpl api, Request request) {

        System.out.printf("Course url: %s%n", api.createCourseUrlFromRequest(request));

        Observable<String> courseResponseFromServer = api.getCourseResponseFromServer(request);

        testServerResponseString("Course", courseResponseFromServer);

        Observable<List<Course>> courseListObservable = api.createCourses(courseResponseFromServer.toBlocking().first());

        testListObservable("Course", courseListObservable);

        api.getCourses(request)
                .toBlocking().forEach(new Action1<Course>() {
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


    private <E> void testListObservable(String tag, Observable<List<E>> listObservable) {
        List<E> list = listObservable.toBlocking().first();
        System.out.printf("%s List: %s%n", tag, StringUtils.join(list, ", "));

        assertTrue(list.size() > 0);
        assertNotNull(list.get(0));
    }

    private void testServerResponseString(String tag, Observable<String> stringObservable) {
        String response = stringObservable.toBlocking().first();
        System.out.printf("%s Response: %s%n", tag, response);

        assertNotNull(response);
        assertTrue(response.length() > 5);
    }
}