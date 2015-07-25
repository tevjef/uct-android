package com.tevinjeffrey.rutgersct.database;

import android.support.annotation.MainThread;
import android.support.annotation.Nullable;

import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.utils.DatabaseUpdateEvent;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class DatabaseHandlerImpl implements DatabaseHandler {

    private final Bus bus;

    public DatabaseHandlerImpl(Bus eventBus) {
        this.bus = eventBus;
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

    private int removeFromDb(Request request) {
        List<TrackedSection> trackedSections = TrackedSection.find(TrackedSection.class,
                "INDEX_NUMBER = ?", request.getIndex());
        for (TrackedSection ts : trackedSections) {
            ts.delete();
        }
        return trackedSections.size();
    }

    private void notifyOnRemoveListeners(Request request) {
        bus.post(new DatabaseUpdateEvent());
    }

    private void notifyOnAddListeners(Request request) {
        bus.post(new DatabaseUpdateEvent());
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

    @Produce
    public DatabaseUpdateEvent produceUpdate() {
        return new DatabaseUpdateEvent();
    }

}
