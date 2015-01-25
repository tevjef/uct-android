package com.tevinjeffrey.rutgerssoc.utils;


import android.content.Context;

import com.tevinjeffrey.rutgerssoc.model.Request;

import java.util.ArrayList;

public class UrlUtils {


    public Context context;

    public UrlUtils() {

    }

    final static String FIRST_LEVEL = "Undergraduate";
    final static String SECOND_LEVEL = "Graduate";
    final static String FIRST_LEVEL_ID = "U";
    final static String SECOND_LEVEL_ID = "G";
    final static String FIRST_SEMESTER = "Winter 2015";
    final static String SECOND_SEMESTER = "Spring 2015";
    final static String THIRD_SEMESTER = "Summer 2015";
    final static String FIRST_SEMESTER_ID = "02015";
    final static String SECOND_SEMESTER_ID = "12015";
    final static String THIRD_SEMESTER_ID = "72015";
    final static String FIRST_LOCATION = "New Brunswick";
    final static String SECOND_LOCATION = "Newark";
    final static String THIRD_LOCATION = "Camden";
    final static String FIRST_LOCATION_ID = "NB";
    final static String SECOND_LOCATION_ID = "NK";
    final static String THIRD_LOCATION_ID = "CM";

    private static String parseSemester(CharSequence rb) {
        //Abstracted list of semesters with a limit of 3 selections
        //TODO: Abstract this class to handle an unlimited number of selections

        String semesterId;



        if (FIRST_SEMESTER.equals(rb)) {
            semesterId = FIRST_SEMESTER_ID;
        } else if (SECOND_SEMESTER.equals(rb)) {
            semesterId = SECOND_SEMESTER_ID;
        } else if(THIRD_SEMESTER.equals(rb)) {
            semesterId = THIRD_SEMESTER_ID;
        } else {
            semesterId = FIRST_SEMESTER_ID;
        }

        return semesterId;
    }
    static String getSemester(Request request) {
        return parseSemester(request.getSemester());

    }

    public static String parseLocations(ArrayList<String> loc) {
        //TODO: Abstract this class to support more location from a server

        StringBuilder location = new StringBuilder();

        for(String s : loc) {
            if (FIRST_LOCATION.equals(s)) {
                append(location, FIRST_LOCATION_ID);
            } else if (SECOND_LOCATION.equals(s)) {
                append(location, SECOND_LOCATION_ID);
            } else if (THIRD_LOCATION.equals(s)) {
                append(location, THIRD_LOCATION_ID);
            } else {
                append(location, FIRST_LOCATION_ID);
                append(location, SECOND_LOCATION_ID);
                append(location, THIRD_LOCATION_ID);
            }
        }

        return location.toString();
    }

    public static String getLocations(Request request) {
       return parseLocations(request.getLocations());
    }

    public static String parseLevels(ArrayList<String> lvls) {
        StringBuilder level= new StringBuilder();

        //TODO: Abstract this class to support more location from a server

        for(String s: lvls) {
            if (FIRST_LEVEL.equals(s)) {
                append(level, FIRST_LEVEL_ID);
            } else if (SECOND_LEVEL.equals(s)) {
                append(level, SECOND_LEVEL_ID);
            } else {
                append(level, FIRST_LEVEL_ID);
                append(level, SECOND_LEVEL_ID);
            }
        }
        return level.toString();
    }
    public static String getLevels(Request request) {
        return parseLevels(request.getLevels());
    }

    public static void appendComma(StringBuilder sb) {
        sb.append("%2C");
    }

    public static void append(StringBuilder sb, String loc) {
        if(sb.length() != 0)
            appendComma(sb);
        sb.append(loc);
    }
    public static String trim(StringBuilder sb){
        return sb.substring(0, sb.length() -3);
    }

    public static String buildParamUrl(Request request) {
        StringBuilder sb = new StringBuilder();
        if(request.isCourseRequest()) {
            sb.append("subject=");
            sb.append(request.getSubject());
            sb.append("&");
        }
        sb.append("semester=");
        sb.append(getSemester(request));
        sb.append("&");
        sb.append("campus=");
        sb.append(getLocations(request));
        sb.append("&");
        sb.append("level=");
        sb.append(getLevels(request));
        return sb.toString();
    }

    public static String getSubjectUrl(String params) {
        String baseUrl = "http://sis.rutgers.edu/soc/";
        String subjectJson = "subjects.json";
        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl);
        sb.append(subjectJson);
        sb.append("?");
        sb.append(params);

        return sb.toString();
    }

    public static String getCourseUrl(String params) {
        String baseUrl = "http://sis.rutgers.edu/soc/";
        String subjectJson = "courses.json";
        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl);
        sb.append(subjectJson);
        sb.append("?");
        sb.append(params);

        return sb.toString();
    }

}
