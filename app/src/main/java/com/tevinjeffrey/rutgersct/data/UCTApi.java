package com.tevinjeffrey.rutgersct.data;

import com.orhanobut.hawk.Hawk;
import com.tevinjeffrey.rutgersct.data.model.Course;
import com.tevinjeffrey.rutgersct.data.model.Section;
import com.tevinjeffrey.rutgersct.data.model.Semester;
import com.tevinjeffrey.rutgersct.data.model.Subject;
import com.tevinjeffrey.rutgersct.data.model.University;
import com.tevinjeffrey.rutgersct.data.notifications.SubscriptionManager;
import com.tevinjeffrey.rutgersct.data.search.SearchFlow;
import com.tevinjeffrey.rutgersct.data.search.UCTSubscription;
import com.tevinjeffrey.rutgersct.utils.BackgroundThread;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import retrofit2.HttpException;
import timber.log.Timber;

public class UCTApi {

  private final String DEFAULT_UNIVERSITY = "default_university";
  private final String DEFAULT_SEMESTER = "default_semester";

  private final String TRACKED_SECTIONS = "trackedsections";
  private final SubscriptionManager subscriptionManager;

  UCTService uctService;
  Scheduler backgroundThread;

  @Inject
  public UCTApi(
      UCTService UCTService,
      @BackgroundThread Scheduler backgroundThread,
      SubscriptionManager subscriptionManager) {
    this.uctService = UCTService;
    this.backgroundThread = backgroundThread;
    this.subscriptionManager = subscriptionManager;
  }

  public Observable<Course> getCourse(SearchFlow searchFlow) {
    return uctService.getCourse(searchFlow.course.topic_name).map(response -> response.data.course);
  }

  public Observable<List<Course>> getCourses(SearchFlow searchFlow) {
    return uctService
        .getCourses(searchFlow.subject.topic_name)
        .map(response -> response.data.courses);
  }

  public Semester getDefaultSemester() {
    Semester semester = Hawk.get(DEFAULT_SEMESTER);
    if (semester == null) {
      return null;
    }
    Timber.d("Getting semester: %s", semester);
    return semester.newBuilder().build();
  }

  public void setDefaultSemester(Semester semester) {
    Timber.d("Setting semester: %s", semester);
    Hawk.put(DEFAULT_SEMESTER, semester);
  }

  public University getDefaultUniversity() {
    University university = Hawk.get(DEFAULT_UNIVERSITY);
    if (university == null) {
      return null;
    }
    Timber.d("Getting university: %s", university.topic_name);
    return university.newBuilder().build();
  }

  public void setDefaultUniversity(University university) {
    Timber.d("Setting university: %s", university.topic_name);
    Hawk.put(DEFAULT_UNIVERSITY, university);
  }

  public Observable<Section> getSection(SearchFlow searchFlow) {
    return getSection(searchFlow.getSection().topic_name);
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

  public Observable<Subject> getSubject(SearchFlow searchFlow) {
    return uctService
        .getSubject(searchFlow.subject.topic_name)
        .map(response -> response.data.subject);
  }

  public Observable<List<Subject>> getSubjects(SearchFlow searchFlow) {
    return uctService.getSubjects(searchFlow.university.topic_name, searchFlow.semester.season,
        String.valueOf(searchFlow.semester.year)
    ).map(response -> response.data.subjects);
  }

  public List<UCTSubscription> getTopics() {
    List<UCTSubscription> topics = Hawk.get(TRACKED_SECTIONS);
    if (topics == null) {
      topics = new ArrayList<>();
      Hawk.put(TRACKED_SECTIONS, topics);
    }

    for (UCTSubscription subscription : topics) {
      subscription.setUniversity(subscription.getUniversity().newBuilder().build());
    }
    Timber.d("Getting all subscriptions: %s", StringUtils.join(topics, "\n"));
    return topics;
  }

  public Observable<List<University>> getUniversities() {
    return uctService.getUniversities().map(response -> response.data.universities);
  }

  public Observable<University> getUniversity(String universityTopicName) {
    return uctService.getUniversity(universityTopicName)
        .map(response -> response.data.university);
  }

  public boolean isTopicTracked(String topicName) {
    List<UCTSubscription> topics = getTopics();
    return topics.contains(new UCTSubscription(topicName));
  }

  public Observable<UCTSubscription> refreshSubscriptions() {
    List<UCTSubscription> subscriptions = getTopics();
    return Observable.fromIterable(subscriptions)
        .flatMap(subscription -> getSection(subscription.getSectionTopicName()))
        .map(section -> {
          int index = subscriptions.indexOf(new UCTSubscription(section.topic_name));
          University newUni = subscriptions.get(index).updateSection(section);
          UCTSubscription newSub = new UCTSubscription(section.topic_name);
          newSub.setUniversity(newUni);
          return newSub;
        })
        .toList()
        .flatMap(this::addAllSubscription)
        .toObservable()
        .flatMap(ignore -> Observable.fromIterable(getTopics()));
  }

  public void removeAll() {
    for (UCTSubscription s : getTopics()) {
      unsubscribe(s.getSectionTopicName()).subscribe(ignore ->
          removeSubscription(s.getSectionTopicName()).subscribe());
    }
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
        .flatMap(subscription1 -> addSubscription(subscription1));
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
    return Single.defer(() -> Single.just(Hawk.put(TRACKED_SECTIONS, subscription)));
  }

  private Single<Boolean> addSubscription(UCTSubscription subscription) {
    List<UCTSubscription> topics = getTopics();
    topics.add(subscription);
    return Single.defer(() -> Single.just(Hawk.put(TRACKED_SECTIONS, topics)));
  }

  private Observable<UCTSubscription> getSubscription(String topicName) {
    List<UCTSubscription> subscriptions = getTopics();
    int index = subscriptions.indexOf(new UCTSubscription(topicName));
    if (index != -1) {
      return Observable.just(subscriptions.get(index));
    } else {
      return Observable.empty();
    }
  }

  private Single<Boolean> removeSubscription(String topicName) {
    List<UCTSubscription> topics = getTopics();
    topics.remove(new UCTSubscription(topicName));
    return Single.defer(() -> Single.just(Hawk.put(TRACKED_SECTIONS, topics)));
  }

  private Single<Boolean> updateSubscription(UCTSubscription subscription) {
    Timber.d("Updating subscription: %s", subscription.getSectionTopicName());
    return removeSubscription(subscription.getSectionTopicName())
        .flatMap(aBoolean -> addSubscription(subscription));
  }
}
