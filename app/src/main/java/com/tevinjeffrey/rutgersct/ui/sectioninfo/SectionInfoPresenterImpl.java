package com.tevinjeffrey.rutgersct.ui.sectioninfo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.tevinjeffrey.rmp.common.Parameter;
import com.tevinjeffrey.rmp.common.Professor;
import com.tevinjeffrey.rmp.common.RMP;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.rutgersapi.RetroRutgers;
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

import javax.inject.Inject;

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

    @Inject
    RMP rmp;

    @Inject
    DatabaseHandler mDatabaseHandler;

    @Inject
    RetroRutgers mRetroRutgers;

    @Inject
    Bus mBus;

    private final Section mSection;

    private Subscription mSubscription;

    public SectionInfoPresenterImpl(Section section) {
        this.mSection = section;
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

        mSubscription = buildSearchParameters(sectionData).flatMap(new Func1<Parameter, Observable<Professor>>() {
            @Override
            public Observable<Professor> call(Parameter parameter) {
                return rmp.getProfessor(parameter);
            }})
                //Should need this to busness code.
                .doOnNext(new Action1<Professor>() {
                    @Override
                    public void call(Professor professor) {
                        for (final Iterator<Instructors> iterator = professorsNotFound.iterator(); iterator.hasNext(); ) {
                            Instructors i = iterator.next();
                            if (StringUtils.getJaroWinklerDistance(i.getLastName(), professor.getLastName()) > .70
                                    || StringUtils.getJaroWinklerDistance(i.getLastName(), professor.getFirstName()) > .70) {
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

    private Observable<Parameter> buildSearchParameters(final Section section) {
        return Observable.from(section.getInstructors())
                .filter(filterGenericInstructors())
                .flatMap(new Func1<Instructors, Observable<Parameter>>() {
                    @Override
                    public Observable<Parameter> call(Instructors instructor) {
                        String university = "rutgers";
                        String department = getMatchingSubject().getDescription();
                        String location = mSection.getRequest().getLocations().get(0);
                        String courseNumber = section.getCourse().getCourseNumber();
                        String firstName = instructor.getFirstName();
                        String lastName = instructor.getLastName();

                        final Parameter params = new Parameter(university, department, location,
                                courseNumber, firstName, lastName);

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
