package com.tevinjeffrey.rutgersct.data

import com.tevinjeffrey.rutgersct.data.model.Response
import io.reactivex.Completable

import io.reactivex.Observable
import io.reactivex.Single
import org.threeten.bp.ZonedDateTime
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
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

  @FormUrlEncoded
  @Headers(ACCEPT_PROTOBUF)
  @POST("/v2/notification")
  fun acknowledgeNotification(
      @Field("receiveAt") receiveAt: String,
      @Field("topicName") topicName: String,
      @Field("fcmToken") fcmToken: String,
      @Field("notificationId") notificationId: String): Completable

  @FormUrlEncoded
  @Headers(ACCEPT_PROTOBUF)
  @POST("/v2/subscription")
  fun subscription(
      @Field("isSubscribed") isSubscribed: Boolean,
      @Field("topicName") topicName: String,
      @Field("fcmToken") fcmToken: String): Completable

  companion object {
    const val ACCEPT_PROTOBUF = "Accept: application/x-protobuf"
  }
}
