package com.tevinjeffrey.rutgersct.database;

import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;

import java.util.List;

import rx.Observable;

public interface DatabaseHandler {

    void setDatabaseListener(DatabaseListener listener);

    void removeListener();

    void removeSectionFromDb(Request request);

    Observable<List<TrackedSection>> getAllSections();

    void addSectionToDb(Request request);

    boolean isSectionTracked(Request request);

    TrackedSection saveToDb(Request request);

    interface DatabaseListener {
        void onAdd(Request addedSection);

        void onRemove(Request removedSection);
    }
}
