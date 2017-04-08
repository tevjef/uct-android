package com.tevinjeffrey.rutgersct.utils;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class RxUtils {

  public static CompositeSubscription getNewCompositeSubIfUnsubscribed(CompositeSubscription subscription) {
    if (subscription == null || subscription.isUnsubscribed()) {
      return new CompositeSubscription();
    }

    return subscription;
  }

  public static void unsubscribeIfNotNull(Subscription subscription) {
    if (subscription != null) {
      subscription.unsubscribe();
    }
  }

  //https://github.com/kaushikgopal/RxJava-Android-Samples/blob/master/app/src/main/java/com/morihacky/android/rxjava/ExponentialBackoffFragment.java
  @SuppressWarnings("ValueOfIncrementOrDecrementUsed")
  public static class RetryWithDelay
      implements Func1<Observable<? extends Throwable>, Observable<?>> {
    private final int _maxRetries;
    private final int _retryDelayMillis;
    private int _retryCount;

    public RetryWithDelay(final int maxRetries, final int retryDelayMillis) {
      _maxRetries = maxRetries;
      _retryDelayMillis = retryDelayMillis;
      _retryCount = 0;
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> attempts) {
      return attempts.flatMap(new Func1<Throwable, Observable<?>>() {
        @Override
        public Observable<?> call(Throwable throwable) {
          if (++_retryCount < _maxRetries) {
            // When this Observable calls onNext, the original
            // Observable will be retried (i.e. re-subscribed).
            Timber.d("Retry %d", _retryCount);
            Timber.d("Retrying in %d ms", _retryCount * _retryDelayMillis);
            return Observable.timer(
                _retryCount * _retryDelayMillis,
                TimeUnit.MILLISECONDS
            );
          }
          // Max retries hit. Just pass the error along.
          return Observable.error(throwable);
        }
      });
    }

    @Override
    public String toString() {
      return "RetryWithDelay{" +
          "_maxRetries=" + _maxRetries +
          ", _retryDelayMillis=" + _retryDelayMillis +
          ", _retryCount=" + _retryCount +
          '}';
    }
  }
}
