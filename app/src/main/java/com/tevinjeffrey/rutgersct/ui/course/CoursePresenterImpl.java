package com.tevinjeffrey.rutgersct.ui.course;

import com.tevinjeffrey.rutgersct.data.uctapi.RetroUCT;
import com.tevinjeffrey.rutgersct.data.uctapi.search.SearchFlow;
import com.tevinjeffrey.rutgersct.ui.base.BasePresenter;
import com.tevinjeffrey.rutgersct.ui.base.View;
import com.tevinjeffrey.rutgersct.utils.AndroidMainThread;
import com.tevinjeffrey.rutgersct.utils.BackgroundThread;
import com.tevinjeffrey.rutgersct.utils.RxUtils;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;

public class CoursePresenterImpl extends BasePresenter implements CoursePresenter {

  private static final String TAG = CoursePresenterImpl.class.getSimpleName();
  private final SearchFlow searchFlow;

  @Inject
  RetroUCT mRetroUCT;
  @Inject
  @AndroidMainThread
  Scheduler mMainThread;
  @Inject
  @BackgroundThread
  Scheduler mBackgroundThread;

  private Disposable disposable;
  private boolean isLoading;

  public CoursePresenterImpl(SearchFlow searchFlow) {
    this.searchFlow = searchFlow;
  }

  public CourseView getView() {
    return (CourseView) super.getView();
  }

  @Override
  public boolean isLoading() {
    return isLoading;
  }

  @Override
  public void loadCourses(boolean pullToRefresh) {
    if (getView() != null) {
      getView().showLoading(pullToRefresh);
    }

    cancePreviousSubscription();

    disposable = mRetroUCT.getCourses(searchFlow)
        .doOnSubscribe(disposable -> isLoading = true)
        .doOnTerminate(() -> isLoading = false)
        .subscribeOn(mBackgroundThread)
        .observeOn(mMainThread)
        .subscribe(courseList -> {
          if (getView() != null) {
            getView().setData(courseList);
          }

          if (courseList.size() > 0) {
            if (getView() != null) {
              getView().showLayout(View.LayoutType.LIST);
            }
          }
        }, throwable -> {
          //Removes the animated loading drawable
          if (getView() != null) {
            getView().showLoading(false);
          }
          //Lets the view decide what to display depending on what type of exception it is.
          if (getView() != null) {
            getView().showError(throwable);
          }
        }, () -> {
          if (getView() != null) {
            getView().showLoading(false);
          }
        });
  }

  @Override
  public String toString() {
    return TAG;
  }

  private void cancePreviousSubscription() {
    RxUtils.disposeIfNotNull(disposable);
  }
}
