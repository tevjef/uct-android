package com.tevinjeffrey.rmp.client;

import com.tevinjeffrey.rmp.common.Professor;

import java.util.Map;

import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.QueryMap;
import rx.Observable;

public interface ClientService {
    @GET("/search")
    Observable<Professor> findProfessor(@QueryMap Map<String, String> options);
    @GET("/report")
    Observable<Professor> reportProfessor(@QueryMap Map<String, String> options);
}
