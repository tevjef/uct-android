package com.tevinjeffrey.rutgersct.ui.subject;

import com.tevinjeffrey.rutgersct.rutgersapi.RutgersApi;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Subject;
import com.tevinjeffrey.rutgersct.ui.base.BasePresenter;
import com.tevinjeffrey.rutgersct.ui.base.View;
import com.tevinjeffrey.rutgersct.utils.RxUtils;
import com.tevinjeffrey.rutgersct.utils.exceptions.RutgersDataIOException;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class SubjectPresenterImpl extends BasePresenter implements SubjectPresenter {

    private static final String TAG = SubjectPresenter.class.getSimpleName();

    private Subscription mSubscription;
    private Request mRequest;
    private RutgersApi mRutgersApi;
    private boolean isLoading;

    public SubjectPresenterImpl(RutgersApi mRutgersApi, Request mRequest) {
        this.mRutgersApi = mRutgersApi;
        this.mRequest = mRequest;

        mRutgersApi.setTag(TAG);
    }

    @Override
    public void loadSubjects(boolean pullToRefresh) {
        if (getView() != null)
            getView().showLoading(pullToRefresh);

        cancePreviousSubscription();

        Subscriber<List<Subject>> mSubscriber = new Subscriber<List<Subject>>() {
            @Override
            public void onCompleted() {
                if (getView() != null)
                    getView().showLoading(false);
            }

            @Override
            public void onError(Throwable e) {
                //Lets the view decide what to display depending on what type of exception it is.
                if (getView() != null)
                    getView().showError(e, true);
                //Removes the animated loading drawable
                if (getView() != null) {
                    getView().showLoading(false);
                }

            }

            @Override
            public void onNext(List<Subject> subjectList) {
                if (getView() != null) {

                    getView().setData(subjectList);

                    if (subjectList.size() == 0)
                        getView().showError(new RutgersDataIOException(), true);

                    if (subjectList.size() > 0)
                        getView().showLayout(View.LayoutType.LIST);
                }
            }
        };

        mSubscription =
                mRutgersApi.getSubjects(mRequest)
                        .toList()
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
                        .onBackpressureDrop()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(mSubscriber);
    }

    private void cancePreviousSubscription() {
        RxUtils.unsubscribeIfNotNull(mSubscription);
        mRutgersApi.getClient().cancel(TAG);
    }

    public SubjectView getView() {
        return (SubjectView) super.getView();
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }
}
