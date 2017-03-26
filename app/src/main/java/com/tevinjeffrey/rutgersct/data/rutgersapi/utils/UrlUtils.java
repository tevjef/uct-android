package com.tevinjeffrey.rutgersct.data.rutgersapi.utils;

import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Section;
import com.tevinjeffrey.rutgersct.data.uctapi.model.extensions.Utils;
import java.util.LinkedHashMap;
import java.util.Map;

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
    sb.append(",");
  }

  private static void append(StringBuilder sb, String loc) {
    if (sb.length() != 0) {
      appendComma(sb);
    }
    sb.append(loc);
  }

  public static String buildParamUrl(Request request) {
    return "subject=" + request.getSubject()
        + "&" + "semester="
        + getSemester(request)
        + "&" + "campus="
        + getLocations(request)
        + "&" + "level="
        + getLevels(request);
  }

  public static Map<String, String> buildSubjectQuery(Request request) {
    //The order of the parameters matter when send the request >:(
    Map<String, String> query = new LinkedHashMap<>(3);
    query.put("semester", getSemester(request));
    query.put("campus", getLocations(request));
    query.put("level", getLevels(request));
    return query;
  }

  public static Map<String, String> buildCourseQuery(Request request) {
    //The order of the parameters matter when send the request >:(
    Map<String, String> query = new LinkedHashMap<>(4);
    query.put("subject", request.getSubject());
    query.put("semester", getSemester(request));
    query.put("campus", getLocations(request));
    query.put("level", getLevels(request));
    return query;
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

  private static String createSearchUrl(String query, Section s) {
    return Utils.InstructorUtils.getToStringInstructors(s.instructors, "+OR+") + query;
  }

  public static String getGoogleUrl(Section s) {
    String query = "+rutgers";
    return createSearchUrl(query, s);
  }
}
