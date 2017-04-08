package com.tevinjeffrey.rutgersct.ui.sectioninfo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.squareup.otto.Bus;
import com.tevinjeffrey.rmp.common.Parameter;
import com.tevinjeffrey.rmp.common.Professor;
import com.tevinjeffrey.rmp.common.RMP;
import com.tevinjeffrey.rmp.common.utils.JaroWinklerDistance;
import com.tevinjeffrey.rutgersct.data.uctapi.RetroUCT;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Instructor;
import com.tevinjeffrey.rutgersct.data.uctapi.model.extensions.Utils;
import com.tevinjeffrey.rutgersct.data.uctapi.search.SearchFlow;
import com.tevinjeffrey.rutgersct.ui.base.BasePresenter;
import com.tevinjeffrey.rutgersct.utils.AndroidMainThread;
import com.tevinjeffrey.rutgersct.utils.BackgroundThread;
import com.tevinjeffrey.rutgersct.utils.RxUtils;
import java.util.ArrayList;
import java.util.Iterator;
import javax.inject.Inject;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func1;
import timber.log.Timber;

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

  @Override
  public void onResume() {
    mBus.register(this);
  }

  @Override
  public void onPause() {
    super.onPause();
    mBus.unregister(this);
  }

  @Override
  public void addSection() {
    mRetroUCT.subscribe(searchFlow.buildSubscription())
        .observeOn(mMainThread)
        .subscribe(new Subscriber<Boolean>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {
            Timber.e(e);
          }

          @Override
          public void onNext(Boolean aBoolean) {
            setFabState(true);
          }
        });
  }

  @Nullable
  public SectionInfoView getView() {
    return (SectionInfoView) super.getView();
  }

  public void loadRMP() {
    final Iterable<Instructor> professorsNotFound =
        new ArrayList<>(searchFlow.getSection().instructors);

    cancePreviousSubscription();

    Subscriber<Professor> subscriber = new Subscriber<Professor>() {
      @Override
      public void onCompleted() {
      }

      @Override
      public void onError(Throwable e) {
        Timber.e(e);
      }

      @Override
      public void onNext(Professor professor) {
        if (getView() != null) {
          getView().addRMPProfessor(professor);
        }
      }
    };

    mSubscription = buildSearchParameters(searchFlow)
        .flatMap(parameter -> rmp.getProfessor(parameter))
        //Should need this to busness code.
        .doOnNext(professor -> {
          for (final Iterator<Instructor> iterator = professorsNotFound.iterator();
              iterator.hasNext(); ) {
            Instructor i = iterator.next();
            if (JaroWinklerDistance.getDistance(
                Utils.InstructorUtils.getLastName(i),
                professor.getLastName(),
                0.70f
            ) > .30
                || JaroWinklerDistance.getDistance(
                Utils.InstructorUtils.getLastName(i),
                professor.getFirstName(),
                0.70f
            ) > .30) {
              iterator.remove();
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
              for (Instructor i : professorsNotFound) {
                getView().addErrorProfessor(Utils.InstructorUtils.getName(i));
              }
            }
          }
        })
        .subscribe(subscriber);
  }

  @Override
  public void removeSection() {
    mRetroUCT.unsubscribe(searchFlow.section.topic_name)
        .observeOn(mMainThread)
        .subscribe(new Subscriber<Boolean>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {
            Timber.e(e);
          }

          @Override
          public void onNext(Boolean aBoolean) {
            setFabState(true);
          }
        });
  }

  public void setFabState(boolean animate) {
    if (getView() != null) {
      boolean sectionTracked = mRetroUCT.isTopicTracked(searchFlow.section.topic_name);
      getView().showSectionTracked(sectionTracked, animate);
    }
  }

  @Override
  public String toString() {
    return TAG;
  }

  public void toggleFab() {
    boolean sectionTracked = mRetroUCT.isTopicTracked(searchFlow.section.topic_name);
    if (sectionTracked) {
      removeSection();
    } else {
      Answers.getInstance().logCustom(new CustomEvent("Tracked Section")
          .putCustomAttribute("Subject", searchFlow.getSubject().name)
          .putCustomAttribute("Course", searchFlow.getCourse().name));
      addSection();
    }
  }

  private Observable<Parameter> buildSearchParameters(final SearchFlow searchFlow) {
    return Observable.from(searchFlow.getSection().instructors)
        .filter(filterGenericInstructors())
        .flatMap(instructor -> {
          String university = "rutgers";
          String department = searchFlow.getSubject().name;
          String location = searchFlow.getUniversity().name;
          String courseNumber = searchFlow.getCourse().number;
          String firstName = Utils.InstructorUtils.getFirstName(instructor);
          String lastName = Utils.InstructorUtils.getLastName(instructor);

          final Parameter params = new Parameter(university, department, location,
              courseNumber, firstName, lastName
          );

          return Observable.just(params);
        });
  }

  private void cancePreviousSubscription() {
    RxUtils.unsubscribeIfNotNull(mSubscription);
  }

  @NonNull
  private Func1<Instructor, Boolean> filterGenericInstructors() {
    return instructor -> !Utils.InstructorUtils.getLastName(instructor).equals("STAFF");
  }
}
