package com.tevinjeffrey.rutgersct.ui.subject;

import com.tevinjeffrey.rutgersct.rutgersapi.RetroRutgers;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Subject;
import com.tevinjeffrey.rutgersct.ui.base.BasePresenter;
import com.tevinjeffrey.rutgersct.ui.base.View;
import com.tevinjeffrey.rutgersct.utils.RxUtils;
import com.tevinjeffrey.rutgersct.utils.exceptions.RutgersDataIOException;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class SubjectPresenterImpl extends BasePresenter implements SubjectPresenter {

    private static final String TAG = SubjectPresenter.class.getSimpleName();

    @Inject
    RetroRutgers mRetroRutgers;

    private Subscription mSubscription;
    private Request mRequest;
    private boolean isLoading;

    public SubjectPresenterImpl(Request mRequest) {
        this.mRequest = mRequest;
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
                    getView().showError(e);
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
                        getView().showError(new RutgersDataIOException());

                    if (subjectList.size() > 0)
                        getView().showLayout(View.LayoutType.LIST);
                }
            }
        };

        mSubscription = mRetroRutgers.getSubjects(mRequest)
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSubscriber);
    }

    private void cancePreviousSubscription() {
        RxUtils.unsubscribeIfNotNull(mSubscription);
    }

    public SubjectView getView() {
        return (SubjectView) super.getView();
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }
}
