package com.tevinjeffrey.rutgersct.ui.utils;


import android.support.v7.widget.RecyclerView;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

public class RecyclerSimpleScrollListener extends RecyclerView.OnScrollListener {
    private static final int SCROLL_TOLERANCE = 0;
    private final SerializedSubject<Direction, Direction> scrollBus;

    public RecyclerSimpleScrollListener() {
        scrollBus = new SerializedSubject<>(PublishSubject.<Direction>create());
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (2 < dy) {
            scrollBus.onNext(Direction.DOWN);
        } else if (-2 > dy) {
            scrollBus.onNext(Direction.UP);
        } else if (0 == dy) {
            scrollBus.onNext(Direction.NEUTRAL);
        }
    }

    public Observable<Direction> getDirectionObservable() {
        return scrollBus.onBackpressureDrop().distinctUntilChanged().asObservable();
    }

    public enum Direction {
        UP, DOWN, NEUTRAL
    }

    @Override
    public String toString() {
        return "RecyclerSimpleScrollListener";
    }
}