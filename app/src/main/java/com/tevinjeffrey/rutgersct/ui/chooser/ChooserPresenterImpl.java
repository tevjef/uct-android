package com.tevinjeffrey.rutgersct.ui.chooser;

import com.tevinjeffrey.rutgersct.data.uctapi.RetroUCT;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Semester;
import com.tevinjeffrey.rutgersct.data.uctapi.model.University;
import com.tevinjeffrey.rutgersct.ui.base.BasePresenter;
import com.tevinjeffrey.rutgersct.utils.AndroidMainThread;
import com.tevinjeffrey.rutgersct.utils.BackgroundThread;
import com.tevinjeffrey.rutgersct.utils.RxUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;

public class ChooserPresenterImpl extends BasePresenter implements ChooserPresenter {

    private static final String TAG = ChooserPresenterImpl.class.getSimpleName();

    @Inject
    RetroUCT mRetroUCT;
    @Inject
    @AndroidMainThread
    Scheduler mMainThread;
    @Inject
    @BackgroundThread
    Scheduler mBackgroundThread;

    @Inject
    RetroUCT retroUCT;

    private Subscription mSubsciption;
    private boolean isLoading;

    public ChooserPresenterImpl() {

    }


    private void cancePreviousSubscription() {
        RxUtils.unsubscribeIfNotNull(mSubsciption);
    }


    @Override
    public University getDefaultUniversity() {
        return retroUCT.getDefaultUniversity();
    }

    @Override
    public void updateDefaultUniversity(University university) {
        retroUCT.setDefaultUniversity(university);
    }

    @Override
    public Semester getDefaultSemester() {
        return retroUCT.getDefaultSemester();
    }

    @Override
    public void updateSemester(Semester semester) {
        retroUCT.setDefaultSemester(semester);
    }

    @Override
    public void loadUniversities() {
        mSubsciption = mRetroUCT.getUniversities()
                .observeOn(mMainThread)
                .subscribe(new Subscriber<List<University>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(List<University> universities) {
                        if (getView() != null) {
                            getView().setUniversities(universities);
                        }
                    }
                });
    }

    @Override
    public void loadAvailableSemesters(String universityTopicName) {
        cancePreviousSubscription();
        mSubsciption = mRetroUCT.getUniversity(universityTopicName)
                .map(university -> university.available_semesters)
                .observeOn(mMainThread)
                .subscribe(new Subscriber<List<Semester>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null) {
                    //Show error in view getView().showError(e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onNext(List<Semester> semesters) {
                if (getView() !=  null) {
                    getView().setAvailableSemesters(semesters);
                }
            }
        });
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    public ChooserView getView() {
        return (ChooserView) super.getView();
    }


}
