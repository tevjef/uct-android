package com.tevinjeffrey.rutgersct.database;

import com.tevinjeffrey.rutgersct.model.Request;
import com.tevinjeffrey.rutgersct.model.TrackedSections;

import java.util.List;

public class DatabaseHandler {

    public static void removeSectionFromDb(Request request) {
        List<TrackedSections> trackedSections = TrackedSections.find(TrackedSections.class,
                "INDEX_NUMBER = ?", request.getIndex());
        for (TrackedSections ts : trackedSections) {
            ts.delete();
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
    }
}
