package com.tevinjeffrey.rutgersct.database;

import com.tevinjeffrey.rutgersct.model.Request;
import com.tevinjeffrey.rutgersct.model.TrackedSections;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    public static AbstractList<DatabaseListener> listeners = new ArrayList<>();

    public static void setDatabaseListener(DatabaseListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public static int removeSectionFromDb(Request request) {
        List<TrackedSections> trackedSections = TrackedSections.find(TrackedSections.class,
                "INDEX_NUMBER = ?", request.getIndex());
        for (TrackedSections ts : trackedSections) {
            ts.delete();
            notifyOnRemoveListeners(request);
        }
        return trackedSections.size();
    }

    private static void notifyOnRemoveListeners(Request request) {
        for (DatabaseListener dl : listeners) {
            if (dl != null) {
                dl.onRemove(request);
            }
        }
    }

    private static void notifyOnAddListeners(Request request) {
        for (DatabaseListener dl : listeners) {
            if (dl != null) {
                dl.onAdd(request);
            }
        }
    }

    public static boolean isSectionTracked(Request request) {
        List<TrackedSections> trackedSections = TrackedSections.find(TrackedSections.class,
                "INDEX_NUMBER = ?", request.getIndex());

        return trackedSections != null && trackedSections.size() != 0;
    }

    public static void addSectionToDb(Request request) {
        TrackedSections trackedSections = new TrackedSections(request.getSubject(),
                request.getSemester().toString(), Request.toStringList(request.getLocations()),
                Request.toStringList(request.getLevels()), request.getIndex());
        trackedSections.save();
        notifyOnAddListeners(request);
    }

    public interface DatabaseListener {
        void onAdd(Request addedSection);

        void onRemove(Request removedSection);
    }
}
