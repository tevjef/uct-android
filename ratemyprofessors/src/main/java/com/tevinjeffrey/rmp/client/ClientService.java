package com.tevinjeffrey.rmp.client;

import com.tevinjeffrey.rmp.common.Professor;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ClientService {
  @GET("/search")
  Observable<Professor> findProfessor(@QueryMap Map<String, String> options);

  @GET("/report")
  Observable<Professor> reportProfessor(@QueryMap Map<String, String> options);
}
