package com.tevinjeffrey.rutgersct.data;

import com.tevinjeffrey.rutgersct.data.model.Response;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface UCTService {
  String ACCEPT_PROTOBUF = "Accept: application/x-protobuf";

  @Headers({ ACCEPT_PROTOBUF })
  @GET("/v2/course/{topic}")
  Observable<Response> getCourse(@Path("topic") String courseTopic);

  @Headers({ ACCEPT_PROTOBUF })
  @GET("/v2/courses/{topic}")
  Observable<Response> getCourses(@Path("topic") String subjectTopic);

  @Headers({ ACCEPT_PROTOBUF })
  @GET("/v2/section/{topic}")
  Observable<Response> getSection(@Path("topic") String sectionTopic);

  @Headers({ ACCEPT_PROTOBUF })
  @GET("/v2/subject/{topic}")
  Observable<Response> getSubject(@Path("topic") String subjectTopic);

  @Headers({ ACCEPT_PROTOBUF })
  @GET("/v2/subjects/{topic}/{season}/{year}")
  Observable<Response> getSubjects(
      @Path("topic") String universityTopic,
      @Path("season") String season,
      @Path("year") String year);

  @Headers({ ACCEPT_PROTOBUF })
  @GET("/v2/universities") Observable<Response> getUniversities();

  @Headers({ ACCEPT_PROTOBUF })
  @GET("/v2/university/{topic}") Observable<Response> getUniversity(@Path("topic") String topic);
}
