package com.tevinjeffrey.rutgersct.utils;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public abstract class RxBus<T> {
  private Subject<T, T> bus;

  public RxBus() {
    final PublishSubject<T> publishSubject = PublishSubject.create();
    bus = new SerializedSubject<>(publishSubject);
  }

  public void send(T event) {
    bus.onNext(event);
  }

  public Observable<T> asObservable() {
    return bus.asObservable();
  }
}