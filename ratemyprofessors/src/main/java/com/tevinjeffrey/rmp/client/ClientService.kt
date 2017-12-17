package com.tevinjeffrey.rmp.client

import com.tevinjeffrey.rmp.common.Professor

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ClientService {
  @GET("/search")
  fun findProfessor(@QueryMap options: Map<String, String>): Observable<Professor>

  @GET("/report")
  fun reportProfessor(@QueryMap options: Map<String, String>): Observable<Professor>
}
