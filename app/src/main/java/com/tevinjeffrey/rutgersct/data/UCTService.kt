package com.tevinjeffrey.rutgersct.data

import com.tevinjeffrey.rutgersct.data.model.Response

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface UCTService {
  @Headers(ACCEPT_PROTOBUF)
  @GET("/v2/universities")
  fun universities(): Observable<Response>

  @Headers(ACCEPT_PROTOBUF)
  @GET("/v2/course/{topic}")
  fun getCourse(@Path("topic") courseTopic: String): Observable<Response>

  @Headers(ACCEPT_PROTOBUF)
  @GET("/v2/courses/{topic}")
  fun getCourses(@Path("topic") subjectTopic: String): Observable<Response>

  @Headers(ACCEPT_PROTOBUF)
  @GET("/v2/section/{topic}")
  fun getSection(@Path("topic") sectionTopic: String): Observable<Response>

  @Headers(ACCEPT_PROTOBUF)
  @GET("/v2/subject/{topic}")
  fun getSubject(@Path("topic") subjectTopic: String): Observable<Response>

  @Headers(ACCEPT_PROTOBUF)
  @GET("/v2/subjects/{topic}/{season}/{year}")
  fun getSubjects(
      @Path("topic") universityTopic: String,
      @Path("season") season: String,
      @Path("year") year: String): Observable<Response>

  @Headers(ACCEPT_PROTOBUF)
  @GET("/v2/university/{topic}")
  fun getUniversity(@Path("topic") topic: String): Observable<Response>

  companion object {
    const val ACCEPT_PROTOBUF = "Accept: application/x-protobuf"
  }
}
