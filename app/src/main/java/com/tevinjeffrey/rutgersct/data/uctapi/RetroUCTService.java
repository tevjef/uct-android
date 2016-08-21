package com.tevinjeffrey.rutgersct.data.uctapi;

import com.tevinjeffrey.rutgersct.data.uctapi.model.Response;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface RetroUCTService {

    @GET("/v2/universities") Observable<Response> getUniversities();

    @GET("/v2/university/{topic}") Observable<Response> getUniversity(@Path("topic") String topic);

    @GET("/v2/subjects/{topic}/{season}/{year}")
    Observable<Response> getSubjects(@Path("topic") String universityTopic, @Path("season") String season, @Path("year") int year);

    @GET("/v2/subject/{topic}")
    Observable<Response> getSubject(@Path("topic") String subjectTopic);

    @GET("/v2/courses/{topic}")
    Observable<Response> getCourses(@Path("topic") String subjectTopic);

    @GET("/v2/course/{topic}")
    Observable<Response> getCourse(@Path("topic") String courseTopic);

    @GET("/v2/section/{topic}")
    Observable<Response> getSection(@Path("topic") String sectionTopic);
}
