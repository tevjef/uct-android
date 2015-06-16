package com.tevinjeffrey.rutgersct.model;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Arrays;

public class TrackedSection extends SugarRecord<TrackedSection> {
    private String subject;
    private String semester;
    private String locations;
    private String levels;
    private String indexNumber;

    public TrackedSection() {
    }

    public TrackedSection(String subject, String semester, String locations, String levels, String indexNumber) {
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

    @Override
    public String toString() {
        return "TrackedSection{" +
                "subject='" + subject + '\'' +
                ", semester='" + semester + '\'' +
                ", locations='" + locations + '\'' +
                ", levels='" + levels + '\'' +
                ", indexNumber='" + indexNumber + '\'' +
                '}';
    }
}
