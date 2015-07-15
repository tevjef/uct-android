package com.tevinjeffrey.rutgersct.database;

import android.support.annotation.MainThread;
import android.support.annotation.Nullable;

import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class DatabaseHandler {

    private static DatabaseHandler sDatabaseHandler = null;

    @Nullable
    public DatabaseListener mListener;

    public static DatabaseHandler getInstance() {
        if (sDatabaseHandler == null) {
            sDatabaseHandler = new DatabaseHandler();
        }
        return sDatabaseHandler;
    }

    public DatabaseHandler() {
    }

    public void setDatabaseListener(DatabaseListener listener) {
        mListener = listener;
    }

    public void removeListener() {
        mListener = null;
    }

    @MainThread
    public void removeSectionFromDb(final Request request) {
        Observable.just(removeFromDb(request))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        notifyOnRemoveListeners(request);
                    }
                })
        .subscribe();
    }

    public static int removeFromDb(Request request) {
        List<TrackedSection> trackedSections = TrackedSection.find(TrackedSection.class,
                "INDEX_NUMBER = ?", request.getIndex());
        for (TrackedSection ts : trackedSections) {
            ts.delete();
        }
        return trackedSections.size();
    }

    private void notifyOnRemoveListeners(Request request) {
        if (mListener != null)
            mListener.onRemove(request);
    }

    private void notifyOnAddListeners(Request request) {
        if (mListener != null)
            mListener.onAdd(request);
    }

    public static boolean isSectionTracked(Request request) {
        List<TrackedSection> trackedSections = TrackedSection.find(TrackedSection.class,
                "INDEX_NUMBER = ?", request.getIndex());

        return trackedSections != null && trackedSections.size() != 0;
    }

    @MainThread
    public void addSectionToDb(final Request request) {
        Observable.just(saveToDb(request))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        notifyOnAddListeners(request);
                    }
                })
        .subscribe();
    }

    public TrackedSection saveToDb(Request request) {
        TrackedSection trackedSections = new TrackedSection(request.getSubject(),
                request.getSemester().toString(), Request.toStringList(request.getLocations()),
                Request.toStringList(request.getLevels()), request.getIndex());
        trackedSections.save();

        return trackedSections;
    }

    public interface DatabaseListener {
        void onAdd(Request addedSection);
        void onRemove(Request removedSection);
    }
}
