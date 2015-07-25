package com.tevinjeffrey.rutgersct.rutgersapi.utils;


import com.tevinjeffrey.rutgersct.database.TrackedSection;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;

import java.util.ArrayList;

public class UrlUtils {

    private final static String FIRST_LEVEL = "Undergraduate";
    private final static String SECOND_LEVEL = "Graduate";
    private final static String FIRST_LEVEL_ID = "U";
    private final static String SECOND_LEVEL_ID = "G";
    private final static String FIRST_LOCATION = "New Brunswick";
    private final static String SECOND_LOCATION = "Newark";
    private final static String THIRD_LOCATION = "Camden";
    private final static String FIRST_LOCATION_ID = "NB";
    private final static String SECOND_LOCATION_ID = "NK";
    private final static String THIRD_LOCATION_ID = "CM";

    public UrlUtils() {
    }

    private static String parseSemester(SemesterUtils.Semester semester) {
        return semester.getSeason().getCode() + semester.getYear();
    }

    private static String getSemester(Request request) {
        return parseSemester(request.getSemester());

    }

    private static String parseLocations(Iterable<String> loc) {
        StringBuilder location = new StringBuilder();

        for (String s : loc) {
            switch (s) {
                case FIRST_LOCATION:
                    append(location, FIRST_LOCATION_ID);
                    break;
                case SECOND_LOCATION:
                    append(location, SECOND_LOCATION_ID);
                    break;
                case THIRD_LOCATION:
                    append(location, THIRD_LOCATION_ID);
                    break;
                default:
                    append(location, FIRST_LOCATION_ID);
                    append(location, SECOND_LOCATION_ID);
                    append(location, THIRD_LOCATION_ID);
                    break;
            }
        }

        return location.toString();
    }

    private static String getLocations(Request request) {
        return parseLocations(request.getLocations());
    }

    private static String parseLevels(Iterable<String> lvls) {
        StringBuilder level = new StringBuilder();

        for (String s : lvls) {
            switch (s) {
                case FIRST_LEVEL:
                    append(level, FIRST_LEVEL_ID);
                    break;
                case SECOND_LEVEL:
                    append(level, SECOND_LEVEL_ID);
                    break;
                default:
                    append(level, FIRST_LEVEL_ID);
                    append(level, SECOND_LEVEL_ID);
                    break;
            }
        }
        return level.toString();
    }

    private static String getLevels(Request request) {
        return parseLevels(request.getLevels());
    }

    private static void appendComma(StringBuilder sb) {
        sb.append("%2C");
    }

    private static void append(StringBuilder sb, String loc) {
        if (sb.length() != 0)
            appendComma(sb);
        sb.append(loc);
    }

    public static String buildParamUrl(Request request) {
        StringBuilder sb = new StringBuilder(50);
        sb.append("subject=");
        sb.append(request.getSubject());
        sb.append("&");
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

    public static Request getRequestFromTrackedSections(TrackedSection ts) {
        return new Request(ts.getSubject(), new SemesterUtils.Semester(ts.getSemester()), ts.getLocations(), ts.getLevels(), ts.getIndexNumber());
    }

    public static String getSubjectUrl(String params) {
        String baseUrl = "http://sis.rutgers.edu/soc/";
        String subjectJson = "subjects.json";
        return baseUrl + subjectJson + "?" + params;
    }

    public static String getCourseUrl(String params) {
        String baseUrl = "http://sis.rutgers.edu/soc/";
        String courseJson = "courses.json";
        return baseUrl + courseJson + "?" + params;
    }

    public static String getAbbreviatedLocationName(String s) {
        switch (s) {
            case FIRST_LOCATION:
                return FIRST_LOCATION_ID;
            case SECOND_LOCATION:
                return SECOND_LOCATION_ID;
            case THIRD_LOCATION:
                return THIRD_LOCATION_ID;
            default:
                return null;
        }
    }

    public static String getRmpUrl(Course.Section s) {
        String query = "+rutgers+site:ratemyprofessors.com";
        return createSearchUrl(query, s);

    }

    private static String createSearchUrl(String query, Course.Section s) {
        return s.getToStringInstructors("+OR+") + query;
    }

    public static String getGoogleUrl(Course.Section s) {
        String query = "+rutgers";
        return createSearchUrl(query, s);
    }

}
