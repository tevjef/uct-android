package com.tevinjeffrey.rutgersct.utils;

import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.data.rutgersapi.utils.SemesterUtils;
import com.tevinjeffrey.rutgersct.database.TrackedSections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class TestConts {

  public static final String YEAR = SEMESTER.getYear();
  public static final String SEASON = SEMESTER.getSeason().getName();
  public static final TrackedSections t1 =
      new TrackedSections("011", SEASON + " " + YEAR, "Newark", "Undergraduate", "19961");
  public static final TrackedSections t2 =
      new TrackedSections("014", SEASON + " " + YEAR, "Newark", "Undergraduate", "07495");
  public static final TrackedSections t3 =
      new TrackedSections("049", SEASON + " " + YEAR, "Newark", "Undergraduate", "13927");
  public static final TrackedSections t4 =
      new TrackedSections("510", SEASON + " " + YEAR, "Newark", "Undergraduate", "19173");
  public static final TrackedSections t5 =
      new TrackedSections("510", SEASON + " " + YEAR, "Newark", "Undergraduate", "19172");
  public static final TrackedSections t6 =
      new TrackedSections("011", SEASON + " " + YEAR, "Newark", "Undergraduate", "01842");
  public static final Request requestNewark =
      new Request(
          "010",
          SEMESTER,
          new ArrayList<>(Arrays.asList(new String[] { "Newark" })),
          new ArrayList<>(Arrays.asList(new String[] { "Undergraduate", "Graduate" }))
      );
  public static final Request requestBrunswick =
      new Request(
          "010",
          SEMESTER,
          new ArrayList<>(Arrays.asList(new String[] { "New Bruswick" })),
          new ArrayList<>(Arrays.asList(new String[] { "Undergraduate", "Graduate" }))
      );
  public static final Request requestCamden =
      new Request(
          "010",
          SEMESTER,
          new ArrayList<>(Arrays.asList(new String[] { "Camden" })),
          new ArrayList<>(Arrays.asList(new String[] { "Undergraduate", "Graduate" }))
      );
  public static final Request requestAllFall =
      new Request(
          "010",
          //Current year - 1 because not all semesters of the current year will be available.
          new SemesterUtils.Semester(
              SemesterUtils.Season.FALL,
              String.valueOf(Integer.valueOf(YEAR) - 1)
          ),
          new ArrayList<>(Arrays.asList(new String[] { "Newark", "New Brunswick", "Camden" })),
          new ArrayList<>(Arrays.asList(new String[] { "Undergraduate", "Graduate" }))
      );
  public static final Request requestAllWinter =
      new Request(
          "010",
          new SemesterUtils.Semester(
              SemesterUtils.Season.WINTER,
              String.valueOf(Integer.valueOf(YEAR) - 1)
          ),
          new ArrayList<>(Arrays.asList(new String[] { "Newark", "New Brunswick", "Camden" })),
          new ArrayList<>(Arrays.asList(new String[] { "Undergraduate", "Graduate" }))
      );
  public static final Request requestAllSpring =
      new Request(
          "010",
          new SemesterUtils.Semester(
              SemesterUtils.Season.SPRING,
              String.valueOf(Integer.valueOf(YEAR) - 1)
          ),
          new ArrayList<>(Arrays.asList(new String[] { "Newark", "New Brunswick", "Camden" })),
          new ArrayList<>(Arrays.asList(new String[] { "Undergraduate", "Graduate" }))
      );
  public static final Request requestAllSummer =
      new Request(
          "010",
          new SemesterUtils.Semester(
              SemesterUtils.Season.SUMMER,
              String.valueOf(Integer.valueOf(YEAR) - 1)
          ),
          new ArrayList<>(Arrays.asList(new String[] { "Newark", "New Brunswick", "Camden" })),
          new ArrayList<>(Arrays.asList(new String[] { "Undergraduate", "Graduate" }))
      );
  public static SemesterUtils semesterUtils = new SemesterUtils(Calendar.getInstance());
  public static final SemesterUtils.Semester SEMESTER = semesterUtils.resolveCurrentSemester();

  public TestConts() {
    throw new AssertionError("Can't instantiate");
  }

  public static List<TrackedSections> createTrackedSections() {
    ArrayList<TrackedSections> trackedSections = new ArrayList<>();
    trackedSections.add(t1);
    trackedSections.add(t2);
    trackedSections.add(t3);
    trackedSections.add(t4);
    trackedSections.add(t5);
    trackedSections.add(t6);
    return trackedSections;
  }

  public static Request getPrimarySemesterRequest() {
    return new Request(
        "198",
        semesterUtils.resolvePrimarySemester(),
        new ArrayList<>(Arrays.asList(new String[] { "Newark", "New Brunswick", "Camden" })),
        new ArrayList<>(Arrays.asList(new String[] { "Undergraduate", "Graduate" }))
    );
  }

  public static Request getSecondarySemesterRequest() {
    return new Request(
        "198",
        semesterUtils.resolveSecondarySemester(),
        new ArrayList<>(Arrays.asList(new String[] { "Newark", "New Brunswick", "Camden" })),
        new ArrayList<>(Arrays.asList(new String[] { "Undergraduate", "Graduate" }))
    );
  }

  @Override
  public String toString() {
    return "RutgersApiTestConts{" +
        "SEMESTER=" + SEMESTER +
        '}';
  }
}
