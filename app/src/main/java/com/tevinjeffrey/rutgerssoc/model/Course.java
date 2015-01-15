package com.tevinjeffrey.rutgerssoc.model;

import org.apache.commons.lang3.StringUtils;

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
        this.expandedTitle = StringUtils.capitalize(expendedTitle.toLowerCase());;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = StringUtils.capitalize(title.toLowerCase());
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
