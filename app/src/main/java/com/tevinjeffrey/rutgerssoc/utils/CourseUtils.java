package com.tevinjeffrey.rutgerssoc.utils;

import com.tevinjeffrey.rutgerssoc.model.Course;

public class CourseUtils {

    public CourseUtils() {

    }
    public static String formatNumber(int subjectCode) {
        if (subjectCode <10) {
            return "00" + subjectCode;
        } else if (subjectCode < 100) {
            return "0" + subjectCode;
        } else {
            return String.valueOf(subjectCode);
        }
    }

    public static String getTitle(Course course) {
        return course.getExpandedTitle() == null? course.getTitle():
                course.getExpandedTitle();
    }
}
