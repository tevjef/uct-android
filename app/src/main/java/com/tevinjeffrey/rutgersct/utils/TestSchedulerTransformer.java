package com.tevinjeffrey.rutgersct.utils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TestSchedulerTransformer<T> implements SchedulerTransformer<T> {
    @Override public Observable<T> call(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.immediate()).observeOn(Schedulers.immediate());
    }
}