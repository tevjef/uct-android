package com.tevinjeffrey.rutgersct.database;

import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.common.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.splunk.mint.Mint;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.model.Course;
import com.tevinjeffrey.rutgersct.model.Request;
import com.tevinjeffrey.rutgersct.model.TrackedSection;
import com.tevinjeffrey.rutgersct.utils.SemesterUtils;
import com.tevinjeffrey.rutgersct.utils.UrlUtils;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import timber.log.Timber;

public class Updater {

    static OkHttpClient client = RutgersCTApp.getClient();

    static Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    public static Observable<Course> getTrackedSections() {
        //Get all tracked sections from the database
        final List<TrackedSection> allTrackedSections = TrackedSection.listAll(TrackedSection.class);


        //Log data about request to get valable data in event of a crash.
        Mint.addExtraData(RutgersCTApp.ITEMS_IN_DATABASE, String.valueOf(allTrackedSections.size()));
        Crashlytics.setInt(RutgersCTApp.ITEMS_IN_DATABASE, allTrackedSections.size());
        Timber.d("Getting %s items from database", allTrackedSections.size());
        Timber.d("Items: %s\n", StringUtils.join(allTrackedSections, "\n"));


        return Observable.from(allTrackedSections)
                .flatMap(new Func1<TrackedSection, Observable<Request>>() {
                    @Override
                    public Observable<Request> call(TrackedSection trackedSection) {
                        return createRequest(trackedSection);
                    }
                })
                .flatMap(new Func1<Request, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(final Request request) {
                        return getCourses(request);
                    }
                });

    }

    private static Observable<Request> createRequest(TrackedSection trackedSection) {
        return Observable.just(UrlUtils.getRequestFromTrackedSections(trackedSection));
    }

    @NonNull
    private static Observable<Course> getCourses(final Request request) {
        return getCoursesFromServer(request).retry(5)
                .flatMap(new Func1<String, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(final String response) {
                        return createCourses(response)
                                .flatMap(new Func1<List<Course>, Observable<Course>>() {
                                    @Override
                                    public Observable<Course> call(List<Course> courses) {
                                        return Observable.from(courses);
                                    }
                                });
                    }
                })
                .map(new Func1<Course, Course>() {
                    @Override
                    public Course call(Course course) {
                        boolean foundMatchFlag = false;
                        for (Course.Sections section : course.getSections()) {
                            if (section.getIndex().equals(request.getIndex())) {
                                foundMatchFlag = true;
                                //Replace the sections in the course with the section we are looking for.
                                List<Course.Sections> currentSection = new ArrayList<>();
                                currentSection.add(section);
                                course.setSections(currentSection);
                            }
                        }
                        if (foundMatchFlag) {
                            return course;
                        } else {
                            return null;
                        }
                    }
                }).filter(new Func1<Course, Boolean>() {
                    @Override
                    public Boolean call(Course course) {
                        return course != null;
                    }
                });
    }

    @NonNull
    private static Observable<List<Course>> createCourses( final String response){
        return Observable.create(new Observable.OnSubscribe<List<Course>>() {
            @Override
            public void call(Subscriber<? super List<Course>> sub) {
                List<Course> courses = new ArrayList<>();
                Type listType = new TypeToken<List<Course>>() {
                }.getType();
                try {
                    courses = gson.fromJson(response, listType);
                } catch (JsonSyntaxException e) {
                    sub.onError(e);
                }
                //It should have responded with a 400 or 500 error, but nope :/
                if (response == null || response.equals("") || courses == null || courses.size() < 1) {
                    sub.onError(new IllegalStateException("No content response from server"));
                }
                sub.onNext(courses);
                sub.onCompleted();
            }
        });
    }

    private static Observable<String> getCoursesFromServer(final Request r) {
        final String url = UrlUtils.getCourseUrl(UrlUtils.buildParamUrl(r));

        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> sub) {
                com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                        .url(url)
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    sub.onError(new IOException("Error while attempting to execute request."));
                }
                if (response != null && response.isSuccessful()) {
                    try {
                        sub.onNext(response.body().string());
                    } catch (IOException e) {
                        sub.onError(e);                    }
                    sub.onCompleted();
                } else {
                    sub.onError(new IOException("Request failed."));
                }
            }
        });
    }
}
