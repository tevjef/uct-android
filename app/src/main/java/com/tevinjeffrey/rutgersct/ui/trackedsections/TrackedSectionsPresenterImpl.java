package com.tevinjeffrey.rutgersct.ui.trackedsections;

import android.os.Bundle;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.database.TrackedSection;
import com.tevinjeffrey.rutgersct.rutgersapi.RetroRutgers;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course.Section;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.ui.base.BasePresenter;
import com.tevinjeffrey.rutgersct.utils.DatabaseUpdateEvent;
import com.tevinjeffrey.rutgersct.utils.RxUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

import static com.tevinjeffrey.rutgersct.ui.base.View.LayoutType.EMPTY;
import static com.tevinjeffrey.rutgersct.ui.base.View.LayoutType.LIST;

public class TrackedSectionsPresenterImpl extends BasePresenter implements TrackedSectionsPresenter {

    private static final String TAG = TrackedSectionsPresenterImpl.class.getSimpleName();

    @Inject
    DatabaseHandler mDatabaseHandler;
    @Inject
    RetroRutgers mRetroRutgers;
    @Inject
    Bus mBus;

    private boolean mHasDataFlag = false;
    private Subscription mSubscription;
    private Subscriber<List<Section>> mSubscriber;
    private boolean isLoading = false;

    public TrackedSectionsPresenterImpl() {
    }

    public void loadTrackedSections(final boolean pullToRefresh) {
        if (getView() != null)
            getView().showLoading(pullToRefresh);

        cancePreviousSubscription();

        mSubscriber = new Subscriber<List<Section>>() {
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
            public void onNext(List<Section> sectionList) {
                if (getView() != null) {
                    getView().setData(sectionList);

                    if (!mHasDataFlag || sectionList.size() == 0)
                        getView().showLayout(EMPTY);

                    else if (sectionList.size() > 0)
                        getView().showLayout(LIST);

                }
            }
        };

        getObservableSections()
                .subscribe(new Subscriber<List<TrackedSection>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<TrackedSection> trackedSections) {
                        mHasDataFlag = trackedSections.size() > 0;
                        mSubscription =
                                mRetroRutgers.getTrackedSections(trackedSections)
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
                                        .toSortedList()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(mSubscriber);
                    }
                });
    }

    private void cancePreviousSubscription() {
        RxUtils.unsubscribeIfNotNull(mSubscription);
    }

    private Observable<List<TrackedSection>> getObservableSections() {
        return mDatabaseHandler.getAllSections();
    }

    public TrackedSectionsView getView() {
        return (TrackedSectionsView) super.getView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onPause() {
        super.onPause();
        mBus.unregister(this);
    }

    @Override
    public void onResume() {
        mBus.register(this);
    }

    @Subscribe
    public void onDbUpdateEvent(DatabaseUpdateEvent event) {
        loadTrackedSections(false);
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
