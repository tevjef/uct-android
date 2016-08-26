package com.tevinjeffrey.rutgersct.data.uctapi;

import com.orhanobut.hawk.Hawk;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Course;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Section;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Subject;
import com.tevinjeffrey.rutgersct.data.uctapi.model.University;
import com.tevinjeffrey.rutgersct.data.uctapi.notifications.SubscriptionManager;
import com.tevinjeffrey.rutgersct.data.uctapi.search.SearchFlow;
import com.tevinjeffrey.rutgersct.data.uctapi.search.UCTSubscription;
import com.tevinjeffrey.rutgersct.utils.BackgroundThread;
import com.tevinjeffrey.rutgersct.utils.SimpleObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@Singleton
public class RetroUCT {

    private final String DEFAULT_UNIVERSITY = "default_university";
    private final String TRACKED_SECTIONS = "trackedsections";
    private final SubscriptionManager subscriptionManager;

    RetroUCTService uctService;
    Scheduler backgroundThread;

    @Inject
    public RetroUCT(RetroUCTService retroUCTService, @BackgroundThread Scheduler backgroundThread, SubscriptionManager subscriptionManager) {
        this.uctService = retroUCTService;
        this.backgroundThread = backgroundThread;
        this.subscriptionManager = subscriptionManager;
    }


    public Observable<List<University>> getUniversities() {
        return uctService.getUniversities().map(response -> response.data.universities);
    }

    public Observable<University> getUniversity(String universityTopicName) {
        return uctService.getUniversity(universityTopicName)
                .map(response -> response.data.university);
    }

    public Observable<List<Subject>> getSubjects(SearchFlow searchFlow) {
        return uctService.getSubjects(searchFlow.university.topic_name, searchFlow.semester.season,
                String.valueOf(searchFlow.semester.year)).map(response -> response.data.subjects);
    }

    public Observable<Subject> getSubject(SearchFlow searchFlow) {
        return uctService.getSubject(searchFlow.subject.topic_name).map(response -> response.data.subject);
    }

    public Observable<List<Course>> getCourses(SearchFlow searchFlow) {
        return uctService.getCourses(searchFlow.subject.topic_name).map(response -> response.data.courses);
    }

    public Observable<Course> getCourse(SearchFlow searchFlow) {
        return uctService.getCourse(searchFlow.course.topic_name).map(response -> response.data.course);
    }

    public Observable<Section> getSection(SearchFlow searchFlow) {
        return getSection(searchFlow.getSection().topic_name);
    }

    public Observable<Section> getSection(String topicName) {
        return uctService.getSection(topicName).map(response -> response.data.section);
    }

    public Observable<UCTSubscription> refreshSubscription(UCTSubscription subscription) {
            return Observable.just(subscription)
                    .flatMap(subscription1 -> getSection(subscription1.getSectionTopicName()))
                    .map(section -> subscription.updateSection(section))
                    .flatMap(subscription1 -> updateSubscription(subscription1))
                    .flatMap(aBoolean -> getSubscription(subscription.getSectionTopicName()));
    }

    public Observable<Object> subscribe(UCTSubscription UCTSubscription) {
        return Observable.defer(() -> {
            try {
                subscriptionManager.subscribe(UCTSubscription.getSectionTopicName());
            } catch (IOException e) {
                return Observable.error(e);
            }
            return Observable.just(UCTSubscription);
        })
                .subscribeOn(Schedulers.io())
                .flatMap(subscription1 -> addSubscription(subscription1));
    }

    public Observable<Boolean> unsubscribe(String topicName) {
        return Observable.defer(() -> {
            try {
                subscriptionManager.unsubscribe(topicName);
            } catch (IOException e) {
                return Observable.error(e);
            }
            return Observable.just(topicName);
        })
                .subscribeOn(Schedulers.io())
                .flatMap(sectionModel1 -> removeTopic(topicName));
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

    private Observable<Boolean> addSubscription(UCTSubscription UCTSubscription) {
        List<UCTSubscription> topics = getTopics();
        topics.add(UCTSubscription);
        Timber.d("Adding subscription: " + UCTSubscription);
        return Hawk.putObservable(TRACKED_SECTIONS, topics);
    }

    private Observable<Boolean> updateSubscription(UCTSubscription subscription) {
        List<UCTSubscription> topics = getTopics();
        Timber.d("Updating subscrption: " + subscription.getSectionTopicName());
        return removeTopic(subscription.getSectionTopicName())
                .flatMap(aBoolean -> addSubscription(subscription))
                .flatMap(aBoolean -> Hawk.putObservable(TRACKED_SECTIONS, topics));
    }

    private Observable<Boolean> removeTopic(String topicName) {
        List<UCTSubscription> topics = getTopics();
        topics.remove(new UCTSubscription(topicName));
        return Hawk.putObservable(TRACKED_SECTIONS, topics);
    }

    public boolean isTopicTracked(String topicName) {
        List<UCTSubscription> topics = getTopics();
        return topics.contains(new UCTSubscription(topicName));
    }

    public void removeAll() {
        for (UCTSubscription s : getTopics()) {
            unsubscribe(s.getSectionTopicName()).subscribe(new SimpleObserver<Boolean>(){
                @Override
                public void onNext(Boolean aBoolean) {
                    removeTopic(s.getSectionTopicName()).subscribe();
                }
            });
        }
    }

    public List<UCTSubscription> getTopics() {
        List<UCTSubscription> topics = Hawk.get(TRACKED_SECTIONS);
        if (topics == null) {
            topics = new ArrayList<>();
            Hawk.put(TRACKED_SECTIONS, topics);
        }
        return topics;
    }

    public void setDefaultUniversity(University university) {
        Hawk.put(DEFAULT_UNIVERSITY, university);
    }

    public University getDefaultUniversity() {
        return Hawk.get(DEFAULT_UNIVERSITY);
    }

}
