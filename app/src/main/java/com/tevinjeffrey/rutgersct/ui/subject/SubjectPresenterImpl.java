package com.tevinjeffrey.rutgersct.ui.subject;

import com.tevinjeffrey.rutgersct.data.uctapi.RetroUCT;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Subject;
import com.tevinjeffrey.rutgersct.data.uctapi.search.SearchFlow;
import com.tevinjeffrey.rutgersct.ui.base.BasePresenter;
import com.tevinjeffrey.rutgersct.ui.base.View;
import com.tevinjeffrey.rutgersct.utils.AndroidMainThread;
import com.tevinjeffrey.rutgersct.utils.BackgroundThread;
import com.tevinjeffrey.rutgersct.utils.RxUtils;
import java.util.List;
import javax.inject.Inject;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;

public class SubjectPresenterImpl extends BasePresenter implements SubjectPresenter {

  private static final String TAG = SubjectPresenter.class.getSimpleName();
  private final SearchFlow searchFlow;

  @Inject
  RetroUCT mRetroUCT;
  @Inject
  @AndroidMainThread
  Scheduler mMainThread;
  @Inject
  @BackgroundThread
  Scheduler mBackgroundThread;

  private Subscription mSubscription;
  private boolean isLoading;

  public SubjectPresenterImpl(SearchFlow searchFlow) {
    this.searchFlow = searchFlow;
  }

  public SubjectView getView() {
    return (SubjectView) super.getView();
  }

  @Override
  public boolean isLoading() {
    return isLoading;
  }

  @Override
  public void loadSubjects(boolean pullToRefresh) {
    if (getView() != null) {
      getView().showLoading(pullToRefresh);
    }

    cancePreviousSubscription();

    Subscriber<List<Subject>> mSubscriber = new Subscriber<List<Subject>>() {
      @Override
      public void onCompleted() {
        if (getView() != null) {
          getView().showLoading(false);
        }
      }

      @Override
      public void onError(Throwable e) {
        //Lets the view decide what to display depending on what type of exception it is.
        if (getView() != null) {
          getView().showError(e);
        }
        //Removes the animated loading drawable
        if (getView() != null) {
          getView().showLoading(false);
        }
      }

      @Override
      public void onNext(List<Subject> subjectList) {
        if (getView() != null) {

          getView().setData(subjectList);

          if (subjectList.size() > 0) {
            getView().showLayout(View.LayoutType.LIST);
          }
        }
      }
    };

    mSubscription = mRetroUCT.getSubjects(searchFlow)
        .doOnSubscribe(() -> isLoading = true)
        .doOnTerminate(() -> isLoading = false)
        .subscribeOn(mBackgroundThread)
        .observeOn(mMainThread)
        .subscribe(mSubscriber);
  }

  private void cancePreviousSubscription() {
    RxUtils.unsubscribeIfNotNull(mSubscription);
  }
}
