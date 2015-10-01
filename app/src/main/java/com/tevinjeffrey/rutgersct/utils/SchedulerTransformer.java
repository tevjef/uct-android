package com.tevinjeffrey.rutgersct.utils;

import rx.Observable;

public interface SchedulerTransformer<T> extends Observable.Transformer<T, T> {
}