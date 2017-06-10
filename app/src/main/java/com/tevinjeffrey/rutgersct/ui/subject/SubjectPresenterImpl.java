package com.tevinjeffrey.rutgersct.ui.subject;

import com.tevinjeffrey.rutgersct.data.UCTApi;
import com.tevinjeffrey.rutgersct.data.search.SearchFlow;
import com.tevinjeffrey.rutgersct.ui.base.BasePresenter;
import com.tevinjeffrey.rutgersct.ui.base.View;
import com.tevinjeffrey.rutgersct.utils.AndroidMainThread;
import com.tevinjeffrey.rutgersct.utils.BackgroundThread;
import com.tevinjeffrey.rutgersct.utils.RxUtils;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;

public class SubjectPresenterImpl extends BasePresenter implements SubjectPresenter {

  private static final String TAG = SubjectPresenter.class.getSimpleName();
  private final SearchFlow searchFlow;

  @Inject UCTApi mUCTApi;
  @Inject @AndroidMainThread Scheduler mMainThread;
  @Inject @BackgroundThread Scheduler mBackgroundThread;

  private Disposable disposable;
  private boolean isLoading;

  SubjectPresenterImpl(SearchFlow searchFlow) {
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
    disposable = mUCTApi.getSubjects(searchFlow)
        .doOnSubscribe(disposable -> isLoading = true)
        .doOnTerminate(() -> isLoading = false)
        .subscribeOn(mBackgroundThread)
        .observeOn(mMainThread)
        .subscribe(subjectList -> {
          if (getView() != null) {

            getView().setData(subjectList);

            if (subjectList.size() > 0) {
              getView().showLayout(View.LayoutType.LIST);
            }
          }
        }, throwable -> {
          //Lets the view decide what to display depending on what type of exception it is.
          if (getView() != null) {
            getView().showError(throwable);
          }
          //Removes the animated loading drawable
          if (getView() != null) {
            getView().showLoading(false);
          }
        }, () -> {
          if (getView() != null) {
            getView().showLoading(false);
          }
        });
  }

  private void cancePreviousSubscription() {
    RxUtils.disposeIfNotNull(disposable);
  }
}
