package com.tevinjeffrey.rutgersct.database;

import android.support.annotation.MainThread;
import android.support.annotation.Nullable;

import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class DatabaseHandlerImpl implements DatabaseHandler {

    private static DatabaseHandler sDatabaseHandler = null;

    @Nullable
    private DatabaseListener mListener;

    public static DatabaseHandler getInstance() {
        if (sDatabaseHandler == null) {
            sDatabaseHandler = new DatabaseHandlerImpl();
        }
        return sDatabaseHandler;
    }

    private DatabaseHandlerImpl() {
    }

    @Override
    public void setDatabaseListener(DatabaseListener listener) {
        mListener = listener;
    }

    @Override
    public void removeListener() {
        mListener = null;
    }

    @Override
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

    public Observable<List<TrackedSection>> getAllSections() {
        return Observable.just(TrackedSection.listAll(TrackedSection.class))
                .subscribeOn(Schedulers.io());
    }

    private static int removeFromDb(Request request) {
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

    public boolean isSectionTracked(Request request) {
        List<TrackedSection> trackedSections = TrackedSection.find(TrackedSection.class,
                "INDEX_NUMBER = ?", request.getIndex());

        return trackedSections != null && trackedSections.size() != 0;
    }

    @Override
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

    @Override
    public TrackedSection saveToDb(Request request) {
        TrackedSection trackedSections = new TrackedSection(request.getSubject(),
                request.getSemester().toString(), Request.toStringList(request.getLocations()),
                Request.toStringList(request.getLevels()), request.getIndex());
        trackedSections.save();

        return trackedSections;
    }

}
