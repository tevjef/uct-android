package com.tevinjeffrey.rutgerssoc.model;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Arrays;

public class TrackedSections extends SugarRecord<TrackedSections> {
    private String subject;
    private String semester;
    private String locations;
    private String levels;
    private String indexNumber;

    public TrackedSections() {
    }

    public TrackedSections(String subject, String semester, String locations, String levels, String indexNumber) {
        this.subject = subject;
        this.semester = semester;
        this.locations = locations;
        this.levels = levels;
        this.indexNumber = indexNumber;
    }

    public String getSubject() {
        return subject;
    }

    public String getSemester() {
        return semester;
    }

    public ArrayList<String> getLocations() {
        return new ArrayList<>(Arrays.asList(locations.split(", ")));
    }

    public ArrayList<String> getLevels() {
        return new ArrayList<>(Arrays.asList(levels.split(", ")));
    }

    public String getIndexNumber() {
        return indexNumber;
    }
}
