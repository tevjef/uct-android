package com.tevinjeffrey.rutgersct.ui.course;

import com.tevinjeffrey.rutgersct.rutgersapi.RutgersApi;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.ui.base.BasePresenter;
import com.tevinjeffrey.rutgersct.ui.base.View;
import com.tevinjeffrey.rutgersct.utils.RxUtils;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class CoursePresenterImpl extends BasePresenter implements CoursePresenter {

    private static final String TAG = CoursePresenterImpl.class.getSimpleName();

    private final RutgersApi mRutgersApi;
    private Subscription mSubscription;
    private Request mRequest;
    private boolean isLoading;

    public CoursePresenterImpl(RutgersApi api, Request mRequest) {
        this.mRequest = mRequest;
        this.mRutgersApi = api;

        mRutgersApi.setTag(TAG);
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

        mSubscription = mRutgersApi.getCourses(mRequest)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        isLoading = true;
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        isLoading = false;
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSubscriber);
    }

    private void cancePreviousSubscription() {
        RxUtils.unsubscribeIfNotNull(mSubscription);
        mRutgersApi.getClient().cancel(TAG);
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
