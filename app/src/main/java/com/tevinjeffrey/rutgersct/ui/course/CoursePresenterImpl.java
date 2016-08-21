package com.tevinjeffrey.rutgersct.ui.course;

import com.tevinjeffrey.rutgersct.data.uctapi.RetroUCT;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Course;
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

    private Subscription mSubscription;
    private boolean isLoading;

    public CoursePresenterImpl(SearchFlow searchFlow) {
        this.searchFlow = searchFlow;
    }

    @Override
    public void loadCourses(boolean pullToRefresh) {
        if (getView() != null)
            getView().showLoading(pullToRefresh);

        cancePreviousSubscription();

        Subscriber<List<Course>> mSubscriber = new Subscriber<List<Course>>() {
            @Override
            public void onCompleted() {
                if (getView() != null)
                    getView().showLoading(false);
            }

            @Override
            public void onError(Throwable e) {
                //Removes the animated loading drawable
                if (getView() != null)
                    getView().showLoading(false);
                //Lets the view decide what to display depending on what type of exception it is.
                if (getView() != null)
                    getView().showError(e);
            }

            @Override
            public void onNext(List<Course> courseList) {
                if (getView() != null)
                    getView().setData(courseList);

                if (courseList.size() > 0) {
                    if (getView() != null)
                        getView().showLayout(View.LayoutType.LIST);
                }
            }
        };

        mSubscription = mRetroUCT.getCourses(searchFlow)
                .doOnSubscribe(() -> isLoading = true)
                .doOnTerminate(() -> isLoading = false)
                .subscribeOn(mBackgroundThread)
                .observeOn(mMainThread)
                .subscribe(mSubscriber);
    }

    private void cancePreviousSubscription() {
        RxUtils.unsubscribeIfNotNull(mSubscription);
    }

    public CourseView getView() {
        return (CourseView) super.getView();
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public String toString() {
        return TAG;
    }
}
