package com.tevinjeffrey.rutgersct.testUtils;


import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.data.rutgersapi.utils.SemesterUtils;
import com.tevinjeffrey.rutgersct.database.TrackedSections;
import com.tevinjeffrey.rutgersct.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Response;
import timber.log.Timber;

public class RutgersApiConts {

    public static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            okhttp3.Request originalRequest = chain.request();
            Timber.d("Host: %s", originalRequest.url().host());

            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .header("Cache-Control", "max-age=10")
                    .build();
        }
    };

    List<TrackedSections> trackedSections;

    static SemesterUtils semesterUtils = new SemesterUtils(Calendar.getInstance());

    static final SemesterUtils.Semester SEMESTER = semesterUtils.resolveCurrentSemester();

    static final String YEAR = "2016";
    static final String SEASON = "Winter";

    static final TrackedSections t1 = new TrackedSections("010", SEASON + " " + YEAR, "Newark", "Undergraduate", "00939");
    static final TrackedSections t2 = new TrackedSections("014", SEASON + " " + YEAR, "Newark", "Undergraduate", "00810");
    static final TrackedSections t3 = new TrackedSections("050", SEASON + " " + YEAR, "Newark", "Undergraduate", "00767");
    static final TrackedSections t4 = new TrackedSections("522", SEASON + " " + YEAR, "Newark", "Undergraduate", "00044");


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
        return Utils.getRequestFromTrackedSections(t1);
    }

    public static Request getSecondarySemesterRequest() {
        return Utils.getRequestFromTrackedSections(t2);
    }

    public static List<TrackedSections> createTrackedSections() {
        ArrayList<TrackedSections> trackedSections = new ArrayList<>();
        trackedSections.add(t1);
        trackedSections.add(t2);
        trackedSections.add(t3);
        trackedSections.add(t4);
        return trackedSections;
    }

    @Override
    public String toString() {
        return "RutgersApiTestConts{" +
                "SEMESTER=" + SEMESTER +
                '}';
    }

    public static Request getRequestFromTrackedSections(TrackedSections ts) {
        return new Request(ts.getSubject(), new SemesterUtils.Semester(ts.getSemester()), ts.getLocations(), ts.getLevels(), ts.getIndexNumber());
    }
}
