package com.tevinjeffrey.rutgersct.ui.sectioninfo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.tevinjeffrey.rmp.common.Parameter;
import com.tevinjeffrey.rmp.common.Professor;
import com.tevinjeffrey.rmp.common.RMP;
import com.tevinjeffrey.rutgersct.data.rutgersapi.RetroRutgers;
import com.tevinjeffrey.rutgersct.data.uctapi.RetroUCT;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Section;
import com.tevinjeffrey.rutgersct.data.uctapi.search.SearchFlow;
import com.tevinjeffrey.rutgersct.ui.base.BasePresenter;
import com.tevinjeffrey.rutgersct.database.DatabaseUpdateEvent;
import com.tevinjeffrey.rutgersct.utils.AndroidMainThread;
import com.tevinjeffrey.rutgersct.utils.BackgroundThread;
import com.tevinjeffrey.rutgersct.utils.RxUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;

import javax.inject.Inject;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

public class SectionInfoPresenterImpl extends BasePresenter implements SectionInfoPresenter {

    private final String TAG = this.getClass().getSimpleName();
    private final SearchFlow searchFlow;

    @Inject
    RMP rmp;

    @Inject
    RetroUCT mRetroUCT;

    @Inject
    Bus mBus;

    @Inject
    @AndroidMainThread
    Scheduler mMainThread;
    @Inject
    @BackgroundThread
    Scheduler mBackgroundThread;

    private Subscription mSubscription;

    public SectionInfoPresenterImpl(SearchFlow searchFlow) {
        this.searchFlow = searchFlow;
    }

    public void setFabState(boolean animate) {
        if (getView() != null) {
            boolean sectionTracked = mRetroUCT.isTopicTracked(searchFlow.getSectionTopic());
            getView().showSectionTracked(sectionTracked, animate);
        }
    }

    public void toggleFab() {
        boolean sectionTracked = mRetroUCT.isTopicTracked(searchFlow.getSectionTopic());
        if (sectionTracked) {
            removeSection(mSection.getRequest());
        } else {
            Answers.getInstance().logCustom(new CustomEvent("Tracked Section")
                    .putCustomAttribute("Subject", mSection.getCourse().getEnclosingSubject().getDescription())
                    .putCustomAttribute("Course", mSection.getCourse().getTrueTitle()));
            addSection(mSection.getRequest());
        }
    }


    @Override
    public void removeSection() {
        mRetroUCT.removeTopic(searchFlow.getSectionTopic());
    }

    @Override
    public void addSection() {
        mRetroUCT.removeTopic(searchFlow.getSectionTopic());
    }

    public void loadRMP() {
        final Iterable<Instructors> professorsNotFound = new ArrayList<>(mSection.getInstructors());

        cancePreviousSubscription();

        Subscriber<Professor> subscriber = new Subscriber<Professor>() {
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
        };

        mSubscription = buildSearchParameters(mSection)
                .flatMap(new Func1<Parameter, Observable<Professor>>() {
                    @Override
                    public Observable<Professor> call(Parameter parameter) {
                        return rmp.getProfessor(parameter);
                    }
                })
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
                .subscribeOn(mBackgroundThread)
                .observeOn(mMainThread)
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
                .subscribe(subscriber);

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
