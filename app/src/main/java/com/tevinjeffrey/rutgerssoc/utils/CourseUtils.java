package com.tevinjeffrey.rutgerssoc.utils;

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
}
