package com.tevinjeffrey.rutgersct.utils;

import com.tevinjeffrey.rutgersct.model.Course;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CourseUtils {

    public CourseUtils() {

    }

    public static String formatNumber(int subjectCode) {
        if (subjectCode < 10) {
            return "00" + subjectCode;
        } else if (subjectCode < 100) {
            return "0" + subjectCode;
        } else {
            return String.valueOf(subjectCode);
        }
    }

    public static void scrubCourseList(Collection<Course> courseList) {
        Collection<Course> toRemove = new ArrayList<>();
        for (Course course : courseList) {
            if (course.getSectionsTotal() == 0) {
                toRemove.add(course);
            }
        }
        courseList.removeAll(toRemove);
    }
}
