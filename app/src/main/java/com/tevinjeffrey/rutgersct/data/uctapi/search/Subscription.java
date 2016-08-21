package com.tevinjeffrey.rutgersct.data.uctapi.search;

import com.tevinjeffrey.rutgersct.data.uctapi.model.University;

public class Subscription {
    String sectionTopicName;
    University university;

    public Subscription(String sectionTopicName, University university) {
        this.sectionTopicName = sectionTopicName;
        this.university = university;
    }

    public String getSectionTopicName() {
        return sectionTopicName;
    }

    public University getUniversity() {
        return university;
    }
}
