package com.tevinjeffrey.rutgersct.data.uctapi;

import android.util.Log;

import com.orhanobut.hawk.Hawk;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Course;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Section;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Subject;
import com.tevinjeffrey.rutgersct.data.uctapi.model.University;
import com.tevinjeffrey.rutgersct.data.uctapi.notifications.SubscriptionManager;
import com.tevinjeffrey.rutgersct.data.uctapi.search.SearchFlow;
import com.tevinjeffrey.rutgersct.data.uctapi.search.Subscription;
import com.tevinjeffrey.rutgersct.utils.SimpleObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

public class RetroUCT {

    private final String TRACKED_SECTIONS = "trackedsections";
    private final SubscriptionManager subscriptionManager;

    RetroUCTService uctService;
    Scheduler backgroundThread;

    public RetroUCT(RetroUCTService retroUCTService, Scheduler backgroundThread, SubscriptionManager subscriptionManager) {
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
        return uctService.getSubjects(searchFlow.getUniversityTopic(), searchFlow.getSeason(),
                searchFlow.getYear()).map(response -> response.data.subjects);
    }

    public Observable<Subject> getSubject(SearchFlow searchFlow) {
        return uctService.getSubject(searchFlow.getSubjectTopic()).map(response -> response.data.subject);
    }

    public Observable<List<Course>> getCourses(SearchFlow searchFlow) {
        return uctService.getCourses(searchFlow.getSubjectTopic()).map(response -> response.data.courses);
    }

    public Observable<Course> getCourse(SearchFlow searchFlow) {
        return uctService.getCourse(searchFlow.getCourseTopic()).map(response -> response.data.course);
    }

    public Observable<Section> getSection(SearchFlow searchFlow) {
        return uctService.getCourse(searchFlow.getCourseTopic()).map(response -> response.data.section);
    }


    public Observable<Object> subscribe(Section section) {
        return Observable.defer(() -> {
            try {
                subscriptionManager.subscribe(section.topic_name);
            } catch (IOException e) {
                return Observable.error(e);
            }
            return Observable.just(section);
        })
                .subscribeOn(Schedulers.io())
                .flatMap(section1 -> addTopic(section1.topic_name));
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

    public Observable<Boolean> addTopic(Subscription subscription) {
        List<Subscription> topics = getTopics();
        topics.add(subscription);
        Log.d("Datamanager", "Adding topic: " +subscription);
        return Hawk.putObservable(TRACKED_SECTIONS, topics);
    }

    public Observable<Boolean> removeTopic(String topicName) {
        List<String> topics = getTopics();
        topics.remove(topicName);
        return Hawk.putObservable(TRACKED_SECTIONS, topics);
    }

    public boolean isTopicTracked(String topicName) {
        List<String> topics = getTopics();
        return topics.contains(topicName);
    }

    public void removeAll() {
        for (String s : getTopics()) {
            unsubscribe(s).subscribe(new SimpleObserver<Boolean>(){
                @Override
                public void onNext(Boolean aBoolean) {
                    removeTopic(s);
                }
            });
        }
    }

    public List<Subscription> getTopics() {
        List<Subscription> topics = Hawk.get(TRACKED_SECTIONS);
        if (topics == null) {
            topics = new ArrayList<>();
            Hawk.put(TRACKED_SECTIONS, topics);
        }
        return topics;
    }


}
