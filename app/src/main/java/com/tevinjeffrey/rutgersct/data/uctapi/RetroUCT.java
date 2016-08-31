package com.tevinjeffrey.rutgersct.data.uctapi;

import com.orhanobut.hawk.Hawk;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Course;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Section;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Semester;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Subject;
import com.tevinjeffrey.rutgersct.data.uctapi.model.University;
import com.tevinjeffrey.rutgersct.data.uctapi.notifications.SubscriptionManager;
import com.tevinjeffrey.rutgersct.data.uctapi.search.SearchFlow;
import com.tevinjeffrey.rutgersct.data.uctapi.search.UCTSubscription;
import com.tevinjeffrey.rutgersct.utils.BackgroundThread;
import com.tevinjeffrey.rutgersct.utils.SimpleObserver;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Scheduler;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@Singleton
public class RetroUCT {

    private final String DEFAULT_UNIVERSITY = "default_university";
    private final String DEFAULT_SEMESTER = "default_semester";

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

    public Observable<UCTSubscription> refreshSubscriptions() {
        List<UCTSubscription> subscriptions = getTopics();
            return Observable.from(subscriptions)
                    .flatMap(new Func1<UCTSubscription, Observable<Section>>() {
                        @Override
                        public Observable<Section> call(UCTSubscription subscription) {
                            return getSection(subscription.getSectionTopicName());
                        }
                    })
                    .map(section -> {
                        int index = subscriptions.indexOf(new UCTSubscription(section.topic_name));
                        University newUni = subscriptions.get(index).updateSection(section);
                        UCTSubscription newSub = new UCTSubscription(section.topic_name);
                        newSub.setUniversity(newUni);
                        return newSub;
                    })
                    .toList()
                    .flatMap(new Func1<List<UCTSubscription>, Observable<Boolean>>() {
                        @Override
                        public Observable<Boolean> call(List<UCTSubscription> uctSubscriptions) {
                            return addAllSubscription(uctSubscriptions);
                        }
                    })
                    .flatMap(new Func1<Boolean, Observable<UCTSubscription>>() {
                        @Override
                        public Observable<UCTSubscription> call(Boolean aBoolean) {
                            return Observable.from(getTopics());
                        }
                    });
    }

    public Observable<Boolean> subscribe(UCTSubscription subscription) {
        Timber.d("Subscribing to: %s", subscription);

        return Observable.defer(() -> {
            try {
                subscriptionManager.subscribe(subscription.getSectionTopicName());
            } catch (IOException e) {
                return Observable.error(e);
            }
            return Observable.just(subscription);
        })
                .subscribeOn(Schedulers.io())
                .flatMap(subscription1 -> addSubscription(subscription1));
    }

    public Observable<Boolean> unsubscribe(String topicName) {
        Timber.d("Unsubscribing from: %s", topicName);

        return Observable.defer(() -> {
            try {
                subscriptionManager.unsubscribe(topicName);
            } catch (IOException e) {
                return Observable.error(e);
            }
            return Observable.just(topicName);
        })
                .subscribeOn(Schedulers.io())
                .flatMap(sectionModel1 -> removeSubscription(topicName));
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

    private Observable<Boolean> addAllSubscription(List<UCTSubscription> subscription) {
        return Hawk.putObservable(TRACKED_SECTIONS, subscription);
    }

    private Observable<Boolean> addSubscription(UCTSubscription subscription) {
        List<UCTSubscription> topics = getTopics();
        topics.add(subscription);
        return Hawk.putObservable(TRACKED_SECTIONS, topics);
    }

    private Observable<Boolean> updateSubscription(UCTSubscription subscription) {
        Timber.d("Updating subscription: %s", subscription.getSectionTopicName());
        return removeSubscription(subscription.getSectionTopicName())
                .flatMap(aBoolean -> addSubscription(subscription));
    }

    private Observable<Boolean> removeSubscription(String topicName) {
        List<UCTSubscription> topics = getTopics();
        topics.remove(new UCTSubscription(topicName));
        return Hawk.putObservable(TRACKED_SECTIONS, topics);
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

    public boolean isTopicTracked(String topicName) {
        List<UCTSubscription> topics = getTopics();
        return topics.contains(new UCTSubscription(topicName));
    }

    public void removeAll() {
        for (UCTSubscription s : getTopics()) {
            unsubscribe(s.getSectionTopicName()).subscribe(new SimpleObserver<Boolean>(){
                @Override
                public void onNext(Boolean aBoolean) {
                    removeSubscription(s.getSectionTopicName()).subscribe();
                }
            });
        }
    }

    public void setDefaultUniversity(University university) {
        Timber.d("Setting university: %s", university.topic_name);
        Hawk.put(DEFAULT_UNIVERSITY, university);
    }

    public University getDefaultUniversity() {
        University university = Hawk.get(DEFAULT_UNIVERSITY);
        if (university == null) {
            return null;
        }
        Timber.d("Getting university: %s", university.topic_name);
        return university.newBuilder().build();
    }

    public void setDefaultSemester(Semester semester) {
        Timber.d("Setting semester: %s", semester);
        Hawk.put(DEFAULT_SEMESTER, semester);
    }

    public Semester getDefaultSemester() {
        Semester semester = Hawk.get(DEFAULT_SEMESTER);
        if (semester == null) {
            return null;
        }
        Timber.d("Getting semester: %s", semester);
        return semester.newBuilder().build();
    }

}
