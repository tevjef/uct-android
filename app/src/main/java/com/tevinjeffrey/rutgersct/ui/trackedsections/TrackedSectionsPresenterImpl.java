package com.tevinjeffrey.rutgersct.ui.trackedsections;

import android.os.Bundle;
import com.squareup.otto.Bus;
import com.tevinjeffrey.rutgersct.data.RetroUCT;
import com.tevinjeffrey.rutgersct.data.search.UCTSubscription;
import com.tevinjeffrey.rutgersct.ui.base.BasePresenter;
import com.tevinjeffrey.rutgersct.utils.AndroidMainThread;
import com.tevinjeffrey.rutgersct.utils.BackgroundThread;
import com.tevinjeffrey.rutgersct.utils.RxUtils;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import java.util.List;
import javax.inject.Inject;

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

  private Disposable disposable;
  private SingleObserver<List<UCTSubscription>> trackedSectinsSubscriber;

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

    trackedSectinsSubscriber = new SingleObserver<List<UCTSubscription>>() {
      @Override
      public void onError(Throwable e) {
        //Removes the animated loading drawable
        if (getView() != null) {
          getView().showLoading(false);
          getView().showError(e);
        }
      }

      @Override public void onSubscribe(final Disposable d) {
        disposable = d;
      }

      @Override public void onSuccess(final List<UCTSubscription> uctSubscriptions) {
        if (getView() != null) {
          getView().setData(uctSubscriptions);
          getView().showLoading(false);
        }
      }
    };

    Observable.defer(() -> mRetroUCT.refreshSubscriptions())
        .doOnSubscribe(disposable -> isLoading = true)
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

  private void cancePreviousSubscription() {
    RxUtils.disposeIfNotNull(disposable);
  }
}
