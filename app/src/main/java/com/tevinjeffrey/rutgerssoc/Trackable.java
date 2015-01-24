package com.tevinjeffrey.rutgerssoc;

import java.util.ArrayList;

/**
 * Created by Tevin on 1/22/2015.
 */
public class Trackable extends Request {
    public Trackable(String subject, String semester, ArrayList<String> locations, ArrayList<String> levels) {
        super(subject, semester, locations, levels);
    }
    public Trackable(Request request) {
        this(request.getSubject(), request.getSemester(), request.getLocations(), request.getLevels());
    }

    String index;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
