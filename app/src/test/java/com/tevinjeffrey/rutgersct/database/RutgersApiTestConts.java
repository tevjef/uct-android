package com.tevinjeffrey.rutgersct.database;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.rutgersapi.utils.SemesterUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import timber.log.Timber;

public class RutgersApiTestConts {

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

    SemesterUtils semesterUtils = new SemesterUtils(Calendar.getInstance());

    final SemesterUtils.Semester SEMESTER = semesterUtils.resolveCurrentSemester();

    final String YEAR = SEMESTER.getYear();
    final String SEASON = SEMESTER.getSeason().getName();

    final TrackedSections t1 = new TrackedSections("011", SEASON + " " + YEAR, "Newark", "Undergraduate", "19961");
    final TrackedSections t2 = new TrackedSections("014", SEASON + " " + YEAR, "Newark", "Undergraduate", "07495");
    final TrackedSections t3 = new TrackedSections("049", SEASON + " " + YEAR, "Newark", "Undergraduate", "13927");
    final TrackedSections t4 = new TrackedSections("510", SEASON + " " + YEAR, "Newark", "Undergraduate", "19173");
    final TrackedSections t5 = new TrackedSections("510", SEASON + " " + YEAR, "Newark", "Undergraduate", "19172");
    final TrackedSections t6 = new TrackedSections("011", SEASON + " " + YEAR, "Newark", "Undergraduate", "01842");

    final Request requestNewark =
            new Request("010",
                    SEMESTER,
                    new ArrayList<>(Arrays.asList(new String[]{"Newark"})),
                    new ArrayList<>(Arrays.asList(new String[]{"Undergraduate", "Graduate"})));
    final Request requestBrunswick =
            new Request("010",
                    SEMESTER,
                    new ArrayList<>(Arrays.asList(new String[]{"New Bruswick"})),
                    new ArrayList<>(Arrays.asList(new String[]{"Undergraduate", "Graduate"})));
    final Request requestCamden =
            new Request("010",
                    SEMESTER,
                    new ArrayList<>(Arrays.asList(new String[]{"Camden"})),
                    new ArrayList<>(Arrays.asList(new String[]{"Undergraduate", "Graduate"})));
    final Request requestAllFall =
            new Request("010",
                    //Current year - 1 because not all semesters of the current year will be available.
                    new SemesterUtils.Semester(SemesterUtils.Season.FALL, String.valueOf(Integer.valueOf(YEAR) - 1)),
                    new ArrayList<>(Arrays.asList(new String[]{"Newark", "New Brunswick", "Camden"})),
                    new ArrayList<>(Arrays.asList(new String[]{"Undergraduate", "Graduate"})));
    final Request requestAllWinter =
            new Request("010",
                    new SemesterUtils.Semester(SemesterUtils.Season.WINTER, String.valueOf(Integer.valueOf(YEAR) - 1)),
                    new ArrayList<>(Arrays.asList(new String[]{"Newark", "New Brunswick", "Camden"})),
                    new ArrayList<>(Arrays.asList(new String[]{"Undergraduate", "Graduate"})));
    final Request requestAllSpring =
            new Request("010",
                    new SemesterUtils.Semester(SemesterUtils.Season.SPRING, String.valueOf(Integer.valueOf(YEAR) - 1)),
                    new ArrayList<>(Arrays.asList(new String[]{"Newark", "New Brunswick", "Camden"})),
                    new ArrayList<>(Arrays.asList(new String[]{"Undergraduate", "Graduate"})));
    final Request requestAllSummer =
            new Request("010",
                    new SemesterUtils.Semester(SemesterUtils.Season.SUMMER, String.valueOf(Integer.valueOf(YEAR) - 1)),
                    new ArrayList<>(Arrays.asList(new String[]{"Newark", "New Brunswick", "Camden"})),
                    new ArrayList<>(Arrays.asList(new String[]{"Undergraduate", "Graduate"})));

    public RutgersApiTestConts() {
        this.semesterUtils = new SemesterUtils(Calendar.getInstance());
    }

    Request getPrimarySemesterRequest() {
        return new Request("198",
                semesterUtils.resolvePrimarySemester(),
                new ArrayList<>(Arrays.asList(new String[]{"Newark", "New Brunswick", "Camden"})),
                new ArrayList<>(Arrays.asList(new String[]{"Undergraduate", "Graduate"})));
    }

    Request getSecondarySemesterRequest() {
        return new Request("198",
                semesterUtils.resolveSecondarySemester(),
                new ArrayList<>(Arrays.asList(new String[]{"Newark", "New Brunswick", "Camden"})),
                new ArrayList<>(Arrays.asList(new String[]{"Undergraduate", "Graduate"})));
    }

    public List<TrackedSections> createTrackedSections() {
        trackedSections = new ArrayList<>();
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