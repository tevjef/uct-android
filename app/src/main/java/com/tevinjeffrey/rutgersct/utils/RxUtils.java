package com.tevinjeffrey.rutgersct.utils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RxUtils {

  public static void disposeIfNotNull(Disposable disposable) {
    if (disposable != null) {
      disposable.dispose();
    }
  }

  public static CompositeDisposable getNewCompositeSubIfUnsubscribed(CompositeDisposable disposable) {
    if (disposable == null || disposable.isDisposed()) {
      return new CompositeDisposable();
    }

    return disposable;
  }
}
