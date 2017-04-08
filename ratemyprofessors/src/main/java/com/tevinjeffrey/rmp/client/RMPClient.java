package com.tevinjeffrey.rmp.client;


import com.tevinjeffrey.rmp.common.Parameter;
import com.tevinjeffrey.rmp.common.Professor;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

public class RMPClient {

    private final ClientService mClientService;

    public RMPClient(ClientService clientService) {
        this.mClientService = clientService;
    }

    public Observable<Professor> findProfessor(Parameter parameter) {
        return mClientService.findProfessor(makeParameters(parameter))
                .filter(new Func1<Professor, Boolean>() {
            @Override
            public Boolean call(Professor professor) {
                return professor != null;
            }
        });
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

}
