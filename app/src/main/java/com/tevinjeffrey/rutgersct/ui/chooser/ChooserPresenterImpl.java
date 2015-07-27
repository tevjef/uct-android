package com.tevinjeffrey.rutgersct.ui.chooser;

import com.tevinjeffrey.rutgersct.rutgersapi.RetroRutgers;
import com.tevinjeffrey.rutgersct.rutgersapi.model.SystemMessage;
import com.tevinjeffrey.rutgersct.ui.base.BasePresenter;
import com.tevinjeffrey.rutgersct.utils.RxUtils;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class ChooserPresenterImpl extends BasePresenter implements ChooserPresenter {

    private static final String TAG = ChooserPresenterImpl.class.getSimpleName();

    @Inject
    RetroRutgers mRetroRutgers;

    private Subscription mSubsciption;
    private boolean isLoading;

    public ChooserPresenterImpl() {
    }

    @Override
    public void loadSystemMessage() {

        cancePreviousSubscription();
        Subscriber<SystemMessage> mSubscriber = new Subscriber<SystemMessage>() {
            @Override
            public void onCompleted() {
                //Noop
            }

            @Override
            public void onError(Throwable e) {
                //Noop
            }

            @Override
            public void onNext(SystemMessage systemMessage) {
                if (getView() != null) {
                    getView().showMessage(systemMessage);
                }
            }
        };

        mSubsciption = mRetroRutgers.getSystemMessage()
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
        RxUtils.unsubscribeIfNotNull(mSubsciption);
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    public ChooserView getView() {
        return (ChooserView) super.getView();
    }


}
