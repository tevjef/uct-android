package com.tevinjeffrey.rutgersct.testUtils;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;
import com.tevinjeffrey.rutgersct.database.TrackedSections;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.rutgersapi.utils.SemesterUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import timber.log.Timber;

public class RutgersApiConts {

    public static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            com.squareup.okhttp.Request originalRequest = chain.request();
            Timber.d("Host: %s", originalRequest.httpUrl().host());

            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .header("Cache-Control", "max-age=10")
                    .build();
        }
    };

    List<TrackedSections> trackedSections;

    static SemesterUtils semesterUtils = new SemesterUtils(Calendar.getInstance());

    static final SemesterUtils.Semester SEMESTER = semesterUtils.resolveCurrentSemester();

    static final String YEAR = SEMESTER.getYear();
    static final String SEASON = SEMESTER.getSeason().getName();

    static final TrackedSections t1 = new TrackedSections("011", SEASON + " " + YEAR, "Newark", "Undergraduate", "19961");
    static final TrackedSections t2 = new TrackedSections("014", SEASON + " " + YEAR, "Newark", "Undergraduate", "07495");
    static final TrackedSections t3 = new TrackedSections("049", SEASON + " " + YEAR, "Newark", "Undergraduate", "13927");
    static final TrackedSections t4 = new TrackedSections("510", SEASON + " " + YEAR, "Newark", "Undergraduate", "19173");
    static final TrackedSections t5 = new TrackedSections("510", SEASON + " " + YEAR, "Newark", "Undergraduate", "19172");
    static final TrackedSections t6 = new TrackedSections("011", SEASON + " " + YEAR, "Newark", "Undergraduate", "01842");

    static final Request requestNewark =
            new Request("010",
                    SEMESTER,
                    new ArrayList<>(Arrays.asList(new String[]{"Newark"})),
                    new ArrayList<>(Arrays.asList(new String[]{"Undergraduate", "Graduate"})));
    static final Request requestBrunswick =
            new Request("010",
                    SEMESTER,
                    new ArrayList<>(Arrays.asList(new String[]{"New Bruswick"})),
                    new ArrayList<>(Arrays.asList(new String[]{"Undergraduate", "Graduate"})));
    static final Request requestCamden =
            new Request("010",
                    SEMESTER,
                    new ArrayList<>(Arrays.asList(new String[]{"Camden"})),
                    new ArrayList<>(Arrays.asList(new String[]{"Undergraduate", "Graduate"})));
    static final Request requestAllFall =
            new Request("010",
                    //Current year - 1 because not all semesters of the current year will be available.
                    new SemesterUtils.Semester(SemesterUtils.Season.FALL, String.valueOf(Integer.valueOf(YEAR) - 1)),
                    new ArrayList<>(Arrays.asList(new String[]{"Newark", "New Brunswick", "Camden"})),
                    new ArrayList<>(Arrays.asList(new String[]{"Undergraduate", "Graduate"})));
    static final Request requestAllWinter =
            new Request("010",
                    new SemesterUtils.Semester(SemesterUtils.Season.WINTER, String.valueOf(Integer.valueOf(YEAR) - 1)),
                    new ArrayList<>(Arrays.asList(new String[]{"Newark", "New Brunswick", "Camden"})),
                    new ArrayList<>(Arrays.asList(new String[]{"Undergraduate", "Graduate"})));
    static final Request requestAllSpring =
            new Request("010",
                    new SemesterUtils.Semester(SemesterUtils.Season.SPRING, String.valueOf(Integer.valueOf(YEAR) - 1)),
                    new ArrayList<>(Arrays.asList(new String[]{"Newark", "New Brunswick", "Camden"})),
                    new ArrayList<>(Arrays.asList(new String[]{"Undergraduate", "Graduate"})));
    static final Request requestAllSummer =
            new Request("010",
                    new SemesterUtils.Semester(SemesterUtils.Season.SUMMER, String.valueOf(Integer.valueOf(YEAR) - 1)),
                    new ArrayList<>(Arrays.asList(new String[]{"Newark", "New Brunswick", "Camden"})),
                    new ArrayList<>(Arrays.asList(new String[]{"Undergraduate", "Graduate"})));

    public RutgersApiConts() {
        throw new AssertionError("Can't instantiate");
    }

    public static Request getPrimarySemesterRequest() {
        return new Request("198",
                semesterUtils.resolvePrimarySemester(),
                new ArrayList<>(Arrays.asList(new String[]{"Newark", "New Brunswick", "Camden"})),
                new ArrayList<>(Arrays.asList(new String[]{"Undergraduate", "Graduate"})));
    }

    public static Request getSecondarySemesterRequest() {
        return new Request("198",
                semesterUtils.resolveSecondarySemester(),
                new ArrayList<>(Arrays.asList(new String[]{"Newark", "New Brunswick", "Camden"})),
                new ArrayList<>(Arrays.asList(new String[]{"Undergraduate", "Graduate"})));
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

    @Override
    public String toString() {
        return "RutgersApiTestConts{" +
                "SEMESTER=" + SEMESTER +
                '}';
    }


}
