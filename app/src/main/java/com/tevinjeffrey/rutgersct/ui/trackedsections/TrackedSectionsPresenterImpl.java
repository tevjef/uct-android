package com.tevinjeffrey.rutgersct.ui.trackedsections;

import android.os.Bundle;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.tevinjeffrey.rutgersct.data.uctapi.RetroUCT;
import com.tevinjeffrey.rutgersct.data.uctapi.search.UCTSubscription;
import com.tevinjeffrey.rutgersct.database.DatabaseUpdateEvent;
import com.tevinjeffrey.rutgersct.ui.base.BasePresenter;
import com.tevinjeffrey.rutgersct.utils.AndroidMainThread;
import com.tevinjeffrey.rutgersct.utils.BackgroundThread;
import com.tevinjeffrey.rutgersct.utils.RxUtils;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;

public class TrackedSectionsPresenterImpl extends BasePresenter
    implements TrackedSectionsPresenter {

  private static final String TAG = TrackedSectionsPresenterImpl.class.getSimpleName();

  @Inject
  RetroUCT mRetroUCT;

  @Inject
  Bus mBus;

  @Inject
  @AndroidMainThread
  Scheduler mMainThread;

  @Inject
  @BackgroundThread
  Scheduler mBackgroundThread;

  private boolean isLoading = false;

  private Subscription mSubscription;
  private Subscriber<List<UCTSubscription>> trackedSectinsSubscriber;

  public TrackedSectionsPresenterImpl() {
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onResume() {
    mBus.register(this);
  }

  @Override
  public void onPause() {
    super.onPause();
    mBus.unregister(this);
  }

  public TrackedSectionsView getView() {
    return (TrackedSectionsView) super.getView();
  }

  @Override
  public boolean isLoading() {
    return isLoading;
  }

  public void loadTrackedSections(final boolean pullToRefresh) {
    if (getView() != null) {
      getView().showLoading(pullToRefresh);
    }

    if (isLoading) {
      return;
    }

    trackedSectinsSubscriber = new Subscriber<List<UCTSubscription>>() {
      @Override
      public void onCompleted() {
        if (getView() != null) {
          getView().showLoading(false);
        }
      }

      @Override
      public void onError(Throwable e) {
        //Removes the animated loading drawable
        if (getView() != null) {
          getView().showLoading(false);
        }
        //Lets the view decide what to display depending on what type of exception it is.
        if (getView() != null) {
          getView().showError(e);
        }
      }

      @Override
      public void onNext(List<UCTSubscription> subscriptions) {
        if (getView() != null) {
          getView().setData(subscriptions);
        }
      }
    };

    mSubscription = Observable.defer(() -> mRetroUCT.refreshSubscriptions())
        .doOnSubscribe(() -> isLoading = true)
        .doOnTerminate(() -> isLoading = false)
        .toSortedList()
        .subscribeOn(mBackgroundThread)
        .observeOn(mMainThread)
        .subscribe(trackedSectinsSubscriber);
  }

  @Override
  public String toString() {
    return TAG;
  }

  @Subscribe
  public void onDbUpdateEvent(DatabaseUpdateEvent event) {
    //When a database update event comes through it loads the data, without a loading animation.
    loadTrackedSections(false);
  }

  private void cancePreviousSubscription() {
    RxUtils.unsubscribeIfNotNull(mSubscription);
  }
}
