package com.tevinjeffrey.rmp.client;

import com.tevinjeffrey.rmp.common.Parameter;
import com.tevinjeffrey.rmp.common.Professor;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class RMPClient {

  private final ClientService mClientService;

  public RMPClient(ClientService clientService) {
    this.mClientService = clientService;
  }

  public static Map<String, String> makeParameters(final Parameter params) {
    Map<String, String> map = new HashMap<>();
    map.put("first", params.firstName);
    map.put("last", params.lastName);
    map.put("subject", params.department);
    map.put("course", params.courseNumber);
    map.put("city", params.location);
    return map;
  }

  public Observable<Professor> findProfessor(Parameter parameter) {
    return mClientService.findProfessor(makeParameters(parameter))
        .filter(professor -> professor != null);
  }
}
