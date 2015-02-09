package com.tevinjeffrey.rutgerssoc.utils;

import com.tevinjeffrey.rutgerssoc.model.Course;

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

    public static String getTitle(Course course) {
        return course.getExpandedTitle() == null ? course.getTitle() :
                course.getExpandedTitle();
    }

    enum COURSE_SEMESTER {
        WINTER(0),
        SPRING(1),
        SUMMER(7),
        FALL(8);

        private final int id;

        COURSE_SEMESTER(int id) {
            this.id = id;
        }

        int getId(COURSE_SEMESTER s) {
            return s.id;
        }
    }
}
