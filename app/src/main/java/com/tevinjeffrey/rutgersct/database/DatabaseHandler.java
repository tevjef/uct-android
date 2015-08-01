package com.tevinjeffrey.rutgersct.database;

import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;

import java.util.List;

import rx.Observable;

public interface DatabaseHandler {

    void removeSectionFromDb(Request request);

    Observable<List<TrackedSections>> getAllSections();

    Observable<List<Request>> getObservableSections();

    void addSectionToDb(Request request);

    boolean isSectionTracked(Request request);

    TrackedSections saveToDb(Request request);
}
