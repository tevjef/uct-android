package com.tevinjeffrey.rutgersct.rutgersapi;

import android.support.annotation.NonNull;

import com.tevinjeffrey.rutgersct.database.TrackedSection;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Subject;

import java.lang.reflect.Type;
import java.util.List;

import rx.Observable;

public interface RutgersApi {

    String ACTIVITY_TAG = "ACTIVITY_TAG";

    Observable<Course> getTrackedSections(Iterable<TrackedSection> allTrackedSections);

    Observable<Request> createRequestObservableFromTrackedSections(Iterable<TrackedSection> allTrackedSections);

    Observable<Request> createRequest(TrackedSection trackedSection);

    @NonNull
    Observable<Course> getCourses(Request request);

    @NonNull
    Observable<Subject> getSubjects(Request request);

    @NonNull
    Observable<List<Course>> createCourses(String response);

    @NonNull
    Observable<List<Subject>> createSubjects(String response);

    String createCourseUrlFromRequest(Request r);

    String createSubjectUrlFromRequest(Request r);

    @NonNull
    <E> Observable<List<E>> parseJsonResponse(String response, Type type);

    Observable<String> getCourseResponseFromServer(Request r);

    Observable<String> getSubjectResponseFromServer(Request r);

    @NonNull
    Observable<String> getServerResponse(String url);
}
