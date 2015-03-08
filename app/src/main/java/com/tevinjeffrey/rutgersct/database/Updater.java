package com.tevinjeffrey.rutgersct.database;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.splunk.mint.Mint;
import com.tevinjeffrey.rutgersct.MyApplication;
import com.tevinjeffrey.rutgersct.model.Course;
import com.tevinjeffrey.rutgersct.model.Request;
import com.tevinjeffrey.rutgersct.model.TrackedSections;
import com.tevinjeffrey.rutgersct.utils.SemesterUtils;
import com.tevinjeffrey.rutgersct.utils.UrlUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import timber.log.Timber;

public class Updater {
    private final Builder mBuilder;
    private final Collection<Request> listOfRequests = new ArrayList<>();

    protected Updater(Builder builder) {
        mBuilder = builder;
        getTrackedSections();
    }

    public static Builder with(Context context) {
        return new Builder(context);
    }

    private void getTrackedSections() {
        //Get all tracked sections from the database
        final List<TrackedSections> allTrackedSections = TrackedSections.listAll(TrackedSections.class);

        //Log data about request to get valable data in event of a crash.
        Mint.addExtraData(MyApplication.ITEMS_IN_DATABASE, String.valueOf(allTrackedSections.size()));
        Crashlytics.setInt(MyApplication.ITEMS_IN_DATABASE, allTrackedSections.size());
        Timber.d("Getting %s items from dataase", allTrackedSections.size());

        //Temp list of the sections retrieved from the server
        final List<Course> updatedTrackedSections = new ArrayList<>();

        //Atomic integer to increment after every asyncronous network call is completed, failed on not.
        final AtomicInteger numOfRequestedCourses = new AtomicInteger();

        //For all tracked sections
        for (TrackedSections ts : allTrackedSections) {

            //Create a request object form the tracked section list.
            final Request r = new Request(ts.getSubject(), new SemesterUtils.Semester(ts.getSemester()), ts.getLocations(), ts.getLevels(), ts.getIndexNumber());

            //Add the request to a list the list for later use.
            listOfRequests.add(r);

            //Get course
            getCourse(allTrackedSections, updatedTrackedSections, numOfRequestedCourses, r);
        }
    }

    private void getCourse(final List<TrackedSections> allTrackedSections, final List<Course> updatedTrackedSections, final AtomicInteger numOfRequestedCourses, final Request r) {
        String url = UrlUtils.getCourseUrl(UrlUtils.buildParamUrl(r));
        Ion.with(mBuilder.context)
                .load(url)
                .as(new TypeToken<List<Course>>() {
                })
                .setCallback(new ListFutureCallback(numOfRequestedCourses, r, updatedTrackedSections, allTrackedSections));
    }

    private void completeOperation(List<Course> updatedTrackedSections, Collection<Request> listOfRequests) {
        Map<Course, Request> mappedValues = new TreeMap<>();
        //Match the request back with course. The Request object hold valuable information about the course/section.
        for (Course c : updatedTrackedSections) {
            for (Request request : listOfRequests) {
                //Match index of the section witht e index of the request then insert them into the rootview.
                if (request.getIndex().equals(c.getSections().get(0).getIndex())) {
                    mappedValues.put(c, request);
                }
            }
        }

        for (Map.Entry<Course, Request> entry : mappedValues.entrySet()) {
            Request value = entry.getValue();
            Course key = entry.getKey();

            mBuilder.onCompleteListener.onSuccess(value, key);
        }

        mBuilder.onCompleteListener.onDone(mappedValues);
    }

    public interface OnCompleteListener {
        void onSuccess(Request request, Course course);

        void onError(Throwable t);

        void onDone(Map<Course, Request> mappedValues);
    }

    public static class Builder {
        protected Context context;
        protected OnCompleteListener onCompleteListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setOnCompleteListener(OnCompleteListener onCompleteListener) {
            if (onCompleteListener != null) {
                this.onCompleteListener = onCompleteListener;
            }
            return this;
        }

        public Updater start() {
            return new Updater(this);
        }
    }

    //Quite difficult to get this right.
    private class ListFutureCallback implements FutureCallback<List<Course>> {
        private final AtomicInteger numOfRequestedCourses;
        private final Request r;
        private final List<Course> updatedTrackedSections;
        private final List<TrackedSections> allTrackedSections;

        public ListFutureCallback(AtomicInteger numOfRequestedCourses, Request r, List<Course> updatedTrackedSections, List<TrackedSections> allTrackedSections) {
            this.numOfRequestedCourses = numOfRequestedCourses;
            this.r = r;
            this.updatedTrackedSections = updatedTrackedSections;
            this.allTrackedSections = allTrackedSections;
        }

        @Override
        public void onCompleted(Exception e, List<Course> courses) {
            //Increment no matter if the request fails or not.
            numOfRequestedCourses.incrementAndGet();
            //If no error and the list of courses is > 0
            if (e == null && courses.size() > 0) {
                //For courses in the list
                for (final Course c : courses) {
                    //For section in the the course
                    for (final Course.Sections s : c.getSections()) {
                        //If the index of the section equals the index of the request.
                        if (s.getIndex().equals(r.getIndex())) {
                            //Replace the sections in c with the section we are looking for.
                            List<Course.Sections> currentSection = new ArrayList<>();
                            currentSection.add(s);
                            c.setSections(currentSection);

                            //The course to the list of the updated sections.
                            updatedTrackedSections.add(c);

                            //Logic to determine when the series of the asyncrous takes have been completed.
                            if (allTrackedSections.size() == numOfRequestedCourses.get()) {
                                completeOperation(updatedTrackedSections, listOfRequests);
                            }
                        }
                    }
                }
            } else {
                mBuilder.onCompleteListener.onError(e);
            }
        }
    }
}
