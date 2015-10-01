package com.tevinjeffrey.rutgersct.database;

import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

public class MockDatabaseHandler implements DatabaseHandler {
    Map<String, Request> db = new HashMap<>();

    @Override
    public void removeSectionFromDb(Request request) {
        db.remove(request.toString());
    }

    @Override
    public Observable<List<Request>> getObservableSections() {
        List<Request> requests = new ArrayList<>(db.values());
        return Observable.just(requests);
    }

    @Override
    public void addSectionToDb(Request request) {
        db.put(request.toString(), request);
    }

    @Override
    public boolean isSectionTracked(Request request) {
        return db.get(request.toString()) != null;
    }

    @Override
    public String toString() {
        return "MockDatabaseHandler{" +
                "db=" + db +
                '}';
    }
}
