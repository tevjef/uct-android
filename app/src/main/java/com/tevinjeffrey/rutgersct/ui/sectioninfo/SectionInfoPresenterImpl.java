package com.tevinjeffrey.rutgersct.ui.sectioninfo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.tevinjeffrey.rmp.RMP;
import com.tevinjeffrey.rmp.professor.Professor;
import com.tevinjeffrey.rmp.search.Decider;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.rutgersapi.RetroRutgers;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course.Section;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course.Section.Instructors;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Subject;
import com.tevinjeffrey.rutgersct.ui.base.BasePresenter;
import com.tevinjeffrey.rutgersct.utils.DatabaseUpdateEvent;
import com.tevinjeffrey.rutgersct.utils.RxUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SectionInfoPresenterImpl extends BasePresenter implements SectionInfoPresenter {

    private final String TAG = this.getClass().getSimpleName();

    private final RMP rmp;
    private final DatabaseHandler mDatabaseHandler;
    private final Section mSection;
    private final RetroRutgers mRetroRutgers;
    private final Bus mBus;
    private Subscription mSubscription;

    public SectionInfoPresenterImpl(RetroRutgers retroRutgers, RMP rmp, Section mSection, DatabaseHandler databaseHandler, Bus bus) {
        this.mDatabaseHandler = databaseHandler;
        this.mSection = mSection;
        this.rmp = rmp;
        this.mRetroRutgers = retroRutgers;
        this.mBus = bus;
    }

    public void setFabState(boolean animate) {
        if (getView() != null) {
            boolean sectionTracked = mDatabaseHandler.isSectionTracked(mSection.getRequest());
            getView().showSectionTracked(sectionTracked, animate);
        }
    }

    public void toggleFab() {
        boolean sectionTracked = mDatabaseHandler.isSectionTracked(mSection.getRequest());

        if (sectionTracked) {
            removeSection(mSection.getRequest());
        } else {
            addSection(mSection.getRequest());
        }
    }


    @Override
    public void removeSection(Request request) {
        mDatabaseHandler.removeSectionFromDb(request);
    }

    @Override
    public void addSection(Request request) {
        mDatabaseHandler.addSectionToDb(request);
    }

    public void loadRMP(final Section sectionData) {
        final Iterable<Instructors> professorsNotFound = new ArrayList<>(sectionData.getInstructors());
        cancePreviousSubscription();

        mSubscription = buildSearchParameters(sectionData).flatMap(new Func1<Decider.Parameter, Observable<Professor>>() {
            @Override
            public Observable<Professor> call(Decider.Parameter parameter) {
                return rmp.findBestProfessor(parameter);
            }
        })
                //Should need this to busness code.
                .doOnNext(new Action1<Professor>() {
                    @Override
                    public void call(Professor professor) {
                        for (final Iterator<Instructors> iterator = professorsNotFound.iterator(); iterator.hasNext(); ) {
                            Instructors i = iterator.next();
                            if (StringUtils.getJaroWinklerDistance(i.getLastName(), professor.getFullName().getLast()) > .70
                                    || StringUtils.getJaroWinklerDistance(i.getLastName(), professor.getFullName().getFirst()) > .70) {
                                iterator.remove();
                            }
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        if (getView() != null) {
                            getView().showRatingsLayout();
                            getView().hideRatingsLoading();
                            for (Instructors i : professorsNotFound) {
                                getView().addErrorProfessor(i.getName());
                            }
                        }
                    }
                })
                .subscribe(new Subscriber<Professor>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Professor professor) {
                        if (getView() != null)
                            getView().addRMPProfessor(professor);
                    }
                });

    }

    private Observable<Decider.Parameter> buildSearchParameters(final Section section) {
        return Observable.from(section.getInstructors())
                .filter(filterGenericInstructors())
                .flatMap(new Func1<Instructors, Observable<Decider.Parameter>>() {
                    @Override
                    public Observable<Decider.Parameter> call(Instructors instructor) {
                        String campus = mSection.getRequest().getLocations().get(0);

                        Subject matchingSubject = getMatchingSubject();

                        final Decider.Parameter params = new Decider.Parameter("rutgers",
                                matchingSubject != null ? matchingSubject.getDescription() : "",
                                campus, new Professor.Name(instructor.getFirstName(),
                                instructor.getLastName()));

                        return Observable.just(params);
                    }

                    public Subject getMatchingSubject() {
                        return mRetroRutgers
                                .getSubjectFromJson(section.getCourse().getSubject());
                    }
                });
    }

    @NonNull
    private Func1<Instructors, Boolean> filterGenericInstructors() {
        return new Func1<Instructors, Boolean>() {
            @Override
            public Boolean call(Instructors instructors) {
                return !instructors.getLastName().equals("STAFF");
            }
        };
    }

    private void cancePreviousSubscription() {
        RxUtils.unsubscribeIfNotNull(mSubscription);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBus.unregister(this);
    }

    @Subscribe
    public void onDbUpdateEvent(DatabaseUpdateEvent event) {
        setFabState(true);
    }

    @Nullable
    public SectionInfoView getView() {
        return (SectionInfoView) super.getView();
    }

    @Override
    public String toString() {
        return TAG;
    }

}
