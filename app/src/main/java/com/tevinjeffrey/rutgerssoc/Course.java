package com.tevinjeffrey.rutgerssoc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tevin on 1/14/2015.
 */
public class Course {
    private String title;

    public String getExpandedTitle() {
        return expandedTitle;
    }

    public void setExpandedTitle(String expendedTitle) {
        this.expandedTitle = expendedTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String expendedTitle) {
        this.title = expendedTitle;
    }


    public boolean isOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(boolean openStatus) {
        this.openStatus = openStatus;
    }

    public List<Sections> getSections() {
        return sections;
    }

    public void setSections(List<Sections> sections) {
        this.sections = sections;
    }

    List<Sections> sections = new ArrayList<Sections>();
    String expandedTitle;
    boolean openStatus;

    public class Sections {
        public List<Instructors> getInstructors() {
            return instructors;
        }

        public void setInstructors(List<Instructors> instructors) {
            this.instructors = instructors;
        }

        List<Instructors> instructors = new ArrayList<Instructors>();

        public class Instructors {
            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            String name;
        }
    }
}
