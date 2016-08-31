package com.tevinjeffrey.rutgersct.data.uctapi;

import com.tevinjeffrey.rutgersct.data.uctapi.model.Response;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface RetroUCTService {

    @GET("/api/v4/universities") Observable<Response> getUniversities();

    @GET("/api/v4/university/{topic}") Observable<Response> getUniversity(@Path("topic") String topic);

    @GET("/api/v4/subjects/{topic}/{season}/{year}")
    Observable<Response> getSubjects(@Path("topic") String universityTopic, @Path("season") String season, @Path("year") String year);

    @GET("/api/v4/subject/{topic}")
    Observable<Response> getSubject(@Path("topic") String subjectTopic);

    @GET("/api/v4/courses/{topic}")
    Observable<Response> getCourses(@Path("topic") String subjectTopic);

    @GET("/api/v4/course/{topic}")
    Observable<Response> getCourse(@Path("topic") String courseTopic);

    @GET("/api/v4/section/{topic}")
    Observable<Response> getSection(@Path("topic") String sectionTopic);
}
