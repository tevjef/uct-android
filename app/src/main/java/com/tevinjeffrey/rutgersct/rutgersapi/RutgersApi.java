package com.tevinjeffrey.rutgersct.rutgersapi;

import com.squareup.okhttp.OkHttpClient;
import com.tevinjeffrey.rutgersct.database.TrackedSection;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Subject;
import com.tevinjeffrey.rutgersct.rutgersapi.model.SystemMessage;

import java.util.List;

import rx.Observable;

public interface RutgersApi {

    OkHttpClient getClient();

    void setTag(String TAG);

    Observable<SystemMessage> getSystemMessage();

    Observable<Course.Section> getTrackedSections(List<TrackedSection> allTrackedSections);

    Observable<Request> createRequestObservableFromTrackedSections(Iterable<TrackedSection> allTrackedSections);

    List<Subject> getSubjectsFromJson();

    Observable<Course> getCourses(Request mRequest);

    Observable<Subject> getSubjects(Request mRequest);
}
