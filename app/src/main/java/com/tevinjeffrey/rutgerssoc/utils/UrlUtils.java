package com.tevinjeffrey.rutgerssoc.utils;


import android.content.Context;

import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.Request;

import java.util.ArrayList;

public class UrlUtils {


    public Context context;

    public UrlUtils(Context context) {
        this.context = context;
    }

    private String parseSemester(CharSequence rb) {
        //Abstracted list of semesters with a limit of 3 selections
        //TODO: Abstract this class to handle an unlimited number of selections

        String semesterId;

        final String FIRST_SEMESTER = context.getResources().getString(R.string.firstSemester);
        final String SECOND_SEMESTER = context.getResources().getString(R.string.secondSemester);
        final String THIRD_SEMESTER = context.getResources().getString(R.string.thirdSemester);
        final String FIRST_SEMESTER_ID = context.getResources().getString(R.string.firstSemester_id);
        final String SECOND_SEMESTER_ID = context.getResources().getString(R.string.secondSemester_id);
        final String THIRD_SEMESTER_ID = context.getResources().getString(R.string.thirdSemester_id);

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
    String getSemester(Request request) {
        return parseSemester(request.getSemester());

    }

    public String parseLocations(ArrayList<String> loc) {
        //TODO: Abstract this class to support more location from a server

        StringBuilder location = new StringBuilder();

        final String FIRST_LOCATION = context.getResources().getString(R.string.firstLocation);
        final String SECOND_LOCATION = context.getResources().getString(R.string.secondLocation);
        final String THIRD_LOCATION = context.getResources().getString(R.string.thirdLocation);

        final String FIRST_LOCATION_ID = context.getResources().getString(R.string.firstLocation_id);
        final String SECOND_LOCATION_ID = context.getResources().getString(R.string.secondLocation_id);
        final String THIRD_LOCATION_ID = context.getResources().getString(R.string.thirdLocation_id);

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

    public String getLocations(Request request) {
       return parseLocations(request.getLocations());
    }

    public String parseLevels(ArrayList<String> lvls) {
        StringBuilder level= new StringBuilder();

        //TODO: Abstract this class to support more location from a server
        final String FIRST_LEVEL = context.getResources().getString(R.string.firstLevel);
        final String SECOND_LEVEL = context.getResources().getString(R.string.secondLevel);
        final String FIRST_LEVEL_ID = context.getResources().getString(R.string.firstLevel_id);
        final String SECOND_LEVEL_ID = context.getResources().getString(R.string.secondLevel_id);

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
    public String getLevels(Request request) {
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

    public String buildParamUrl(Request request) {
        StringBuilder sb = new StringBuilder();
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
