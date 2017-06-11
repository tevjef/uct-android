package com.tevinjeffrey.rutgersct.data;

import android.support.v4.util.Pair;
import com.orhanobut.hawk.Hawk;
import com.tevinjeffrey.rutgersct.data.database.PreferenceDao;
import com.tevinjeffrey.rutgersct.data.database.UCTSubscriptionDao;
import com.tevinjeffrey.rutgersct.data.model.Course;
import com.tevinjeffrey.rutgersct.data.model.Section;
import com.tevinjeffrey.rutgersct.data.model.Semester;
import com.tevinjeffrey.rutgersct.data.model.Subject;
import com.tevinjeffrey.rutgersct.data.model.University;
import com.tevinjeffrey.rutgersct.data.notifications.SubscriptionManager;
import com.tevinjeffrey.rutgersct.data.preference.DefaultSemester;
import com.tevinjeffrey.rutgersct.data.preference.DefaultUniversity;
import com.tevinjeffrey.rutgersct.data.search.SearchFlow;
import com.tevinjeffrey.rutgersct.data.search.UCTSubscription;
import com.tevinjeffrey.rutgersct.utils.BackgroundThread;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import javax.inject.Inject;
import jonathanfinerty.once.Amount;
import jonathanfinerty.once.Once;
import retrofit2.HttpException;
import timber.log.Timber;

public class UCTApi {

  private final String DEFAULT_UNIVERSITY = "default_university";
  private final String DEFAULT_SEMESTER = "default_semester";

  private final String TRACKED_SECTIONS = "trackedsections";
  private final String TRACKED_SECTIONS_MIGRATION = "trackedsectionsmigration";
  private final SubscriptionManager subscriptionManager;
  private final UCTSubscriptionDao subscriptionDao;
  private final PreferenceDao preferenceDao;
  private final UCTService uctService;
  private final Scheduler backgroundThread;

  @Inject
  public UCTApi(
      UCTService UCTService,
      @BackgroundThread Scheduler backgroundThread,
      SubscriptionManager subscriptionManager,
      UCTSubscriptionDao UCTSubscriptionDao,
      PreferenceDao preferenceDao) {
    this.uctService = UCTService;
    this.backgroundThread = backgroundThread;
    this.subscriptionManager = subscriptionManager;
    this.subscriptionDao = UCTSubscriptionDao;
    this.preferenceDao = preferenceDao;

    if (!Once.beenDone(TRACKED_SECTIONS_MIGRATION, Amount.exactly(1))) {
      List<UCTSubscription> subscriptions = Hawk.get(TRACKED_SECTIONS, new ArrayList<>());

      Single.fromCallable((Callable<SingleSource<Boolean>>) () -> {
        if (!subscriptions.isEmpty()) {
          subscriptionDao.insertAll(subscriptions);
        }
        return Single.just(true);
      })
          .observeOn(this.backgroundThread)
          .subscribe(ignore -> Once.markDone(TRACKED_SECTIONS_MIGRATION), Timber::e);
    }
  }

  public Observable<List<Course>> getCourses(SearchFlow searchFlow) {
    return uctService
        .getCourses(searchFlow.subject.topic_name)
        .map(response -> response.data.courses);
  }

  public Semester getDefaultSemester() {
    DefaultSemester defaultSemester = preferenceDao.getDefaultSemester();
    if (defaultSemester == null) {
      defaultSemester = new DefaultSemester(null);
    }
    Semester semester = defaultSemester.getSemester();
    Timber.d("Getting semester: %s", semester);
    return semester;
  }

  public void setDefaultSemester(Semester semester) {
    Timber.d("Setting semester: %s", semester);
    preferenceDao.updateDefaultSemester(new DefaultSemester(semester));
  }

  public University getDefaultUniversity() {
    DefaultUniversity defaultUniversity = preferenceDao.getDefaultUniversity();
    if (defaultUniversity == null) {
      defaultUniversity = new DefaultUniversity(null);
    }
    University university = defaultUniversity.getUniversity();
    Timber.d("Getting university: %s", university != null ? university.topic_name : null);
    return university;
  }

  public void setDefaultUniversity(University university) {
    Timber.d("Setting university: %s", university.topic_name);
    preferenceDao.updateDefaultUniversity(new DefaultUniversity(university));
  }

  public Observable<Section> getSection(String topicName) {
    return uctService.getSection(topicName).map(response -> response.data.section)
        .onErrorResumeNext(throwable -> {
          // Catch 404 exceptions
          if (throwable instanceof HttpException) {
            HttpException exception = (HttpException) throwable;
            if (exception.code() == 404) {
              return Observable.empty();
            }
          }

          return Observable.error(throwable);
        });
  }

  public Observable<List<Subject>> getSubjects(SearchFlow searchFlow) {
    return uctService.getSubjects(searchFlow.university.topic_name, searchFlow.semester.season,
        String.valueOf(searchFlow.semester.year)
    ).map(response -> response.data.subjects);
  }

  public Observable<List<University>> getUniversities() {
    return uctService.getUniversities().map(response -> response.data.universities);
  }

  public Observable<University> getUniversity(String universityTopicName) {
    return uctService.getUniversity(universityTopicName)
        .map(response -> response.data.university);
  }

  public boolean isTopicTracked(String topicName) {
    return subscriptionDao.isSectionTracked(topicName);
  }

  public Observable<UCTSubscription> refreshSubscriptions() {
    return Observable.fromIterable(subscriptionDao.getAll())
        .flatMap(subscription ->
            getSection(
                subscription.getSectionTopicName())
                .map(section -> Pair.create(subscription, section)))
        .map(pair -> {
          University newUni = pair.first.getUniversity();
          UCTSubscription newSub = new UCTSubscription(pair.second.topic_name);
          newSub.setUniversity(newUni);
          return newSub;
        })
        .toList()
        .flatMap(this::addAllSubscription)
        .toObservable()
        .flatMap(ignore -> Observable.fromIterable(subscriptionDao.getAll()));
  }

  public Single<Boolean> subscribe(UCTSubscription subscription) {
    Timber.d("Subscribing to: %s", subscription);

    return Single.defer(() -> {
      try {
        subscriptionManager.subscribe(subscription.getSectionTopicName());
      } catch (IOException e) {
        return Single.error(e);
      }
      return Single.just(subscription);
    })
        .subscribeOn(Schedulers.io())
        .flatMap(this::addSubscription);
  }

  public Single<Boolean> unsubscribe(String topicName) {
    Timber.d("Unsubscribing from: %s", topicName);

    return Single.defer(() -> {
      try {
        subscriptionManager.unsubscribe(topicName);
      } catch (IOException e) {
        return Single.error(e);
      }
      return Single.just(topicName);
    })
        .subscribeOn(Schedulers.io())
        .flatMap(ignore -> removeSubscription(topicName));
  }

  private Single<Boolean> addAllSubscription(List<UCTSubscription> subscription) {
    return Single.defer(() -> {
      subscriptionDao.insertAll(subscription);
      return Single.just(true);
    });
  }

  private Single<Boolean> addSubscription(UCTSubscription subscription) {
    return Single.defer(() -> {
      subscriptionDao.insertAll(Arrays.asList(subscription));
      return Single.just(true);
    });
  }

  private Single<Boolean> removeSubscription(String topicName) {
    return Single.defer(() -> {
      subscriptionDao.delete(new UCTSubscription(topicName));
      return Single.just(true);
    });
  }
}
