package com.tevinjeffrey.rutgersct.data.rutgersapi;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tevinjeffrey.rutgersct.data.rutgersapi.exceptions.RutgersServerIOException;
import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Subject;
import com.tevinjeffrey.rutgersct.data.rutgersapi.model.SystemMessage;
import com.tevinjeffrey.rutgersct.data.rutgersapi.utils.UrlUtils;
import com.tevinjeffrey.rutgersct.utils.BackgroundThread;
import com.tevinjeffrey.rutgersct.utils.RxUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.tevinjeffrey.rutgersct.data.rutgersapi.model.Course.Section;

@Singleton
public class RetroRutgers {

    private static final int SERVER_RETRY_COUNT = 3;
    private static final int RETRY_DELAY_MILLIS = 3000;
    private final RetroRutgersService mRetroRutgersService;
    private final Scheduler mBackgroundThread;
    final List<Subject> mSubjectsList;

    @Inject
    public RetroRutgers(RetroRutgersService retroRutgersService, @BackgroundThread Scheduler backgroundThread) {
        this.mBackgroundThread = backgroundThread;
        this.mRetroRutgersService = retroRutgersService;
        mSubjectsList = initSubjectList();
    }


    public Observable<List<Subject>> getSubjects(Request request) {
        return mRetroRutgersService.getSubjects(UrlUtils.buildSubjectQuery(request));
    }

    public Observable<SystemMessage> getSystemMessage() {
        return mRetroRutgersService.getSystemMessage();
    }

    public Observable<Course.Section> getTrackedSections(final List<Request> allTrackedSections) {
        return Observable.from(allTrackedSections)
                .flatMap(new Func1<Request, Observable<Section>>() {
                    @Override
                    public Observable<Section> call(final Request request) {
                        return getCourses(request)
                                //Emit the sections within the course that was emitted.
                                .flatMap(new Func1<List<Course>, Observable<Section>>() {
                                    @Override
                                    public Observable<Section> call(final List<Course> courseList) {
                                        return Observable.from(courseList)
                                                .flatMap(new Func1<Course, Observable<Section>>() {
                                                    @Override
                                                    public Observable<Section> call(Course course) {
                                                        return Observable.from(course.getSections());
                                                    }
                                                });
                                    }
                                })
                                        //Filters out the sections we were not looking for. If the condition
                                        //true the item will be allow through
                                .filter(new Func1<Section, Boolean>() {
                                    @Override
                                    public Boolean call(Section section) {
                                        return section.getIndex().equals(request.getIndex());
                                    }
                                })
                                .subscribeOn(mBackgroundThread);


                    }
                })
                        // Convert every completed request in into a list and check if all were completed successfully.
                        // Sometimes the servers bug out and course and section information will not be available.
                        // Earlier in the the flow, this would simple result in a JsonSyntaxException or an
                        // RutgersDataException. However, it can make it to this point where no section has been
                        // found. Instead of emiting an empty observable, I converted it to a list whose size
                        // will then be compared to the original list of requests. Emit any items, before
                        // deciding wheter or not to pass an exeception to onError(). Comparing by the size of the
                        // retrieved list is an indication that all requests weren't made.
                .toList()
                .flatMap(new Func1<List<Course.Section>, Observable<Course.Section>>() {
                    @Override
                    public Observable<Course.Section> call(final List<Course.Section> sectionList) {
                        return Observable.create(new Observable.OnSubscribe<List<Course.Section>>() {
                            @Override
                            public void call(Subscriber<? super List<Course.Section>> subscriber) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onNext(sectionList);
                                    if (sectionList.size() < allTrackedSections.size()) {
                                        subscriber.onError(new RutgersServerIOException("Could not retrieve all sections"));
                                    } else {
                                        subscriber.onCompleted();
                                    }
                                }
                            }
                        }).flatMap(new Func1<List<Course.Section>, Observable<Course.Section>>() {
                            @Override
                            public Observable<Course.Section> call(List<Course.Section> sectionList) {
                                return Observable.from(sectionList);
                            }
                        });
                    }
                });
    }

    public Observable<List<Course>> getCourses(final Request request) {
        return mRetroRutgersService.getCourses(UrlUtils.buildCourseQuery(request))
                .subscribeOn(mBackgroundThread)
                .flatMap(new Func1<List<Course>, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(final List<Course> courses) {
                        return configureCourses(courses, request);
                    }
                }).toSortedList()
                .flatMap(new Func1<List<Course>, Observable<? extends List<Course>>>() {
                    @Override
                    public Observable<? extends List<Course>> call(final List<Course> courseList) {
                        return Observable.create(new Observable.OnSubscribe<List<Course>>() {
                            @Override
                            public void call(Subscriber<? super List<Course>> subscriber) {
                                if (!subscriber.isUnsubscribed()) {
                                    if (courseList.isEmpty()) {
                                        subscriber.onError(new RutgersServerIOException());
                                    } else {
                                        subscriber.onNext(courseList);
                                        subscriber.onCompleted();
                                    }
                                }
                            }
                        });
                    }
                })
                .retryWhen(new RxUtils.RetryWithDelay(SERVER_RETRY_COUNT, RETRY_DELAY_MILLIS));
    }

    private Observable<Course> configureCourses(List<Course> courses, final Request request) {
        return Observable.from(courses)
                .flatMap(filterUnprintedSections())
                .filter(filterEmptySections())
                .map(setSubjectInCourse())
                .map(setRequestAndStubCourseInSection(request));
    }

    @NonNull
    private Func1<Course, Boolean> filterEmptySections() {
        return new Func1<Course, Boolean>() {
            @Override
            public Boolean call(Course course) {
                return course.getSectionsTotal() > 0;
            }
        };
    }

    private Func1<Course, Observable<Course>> filterUnprintedSections() {
        return new Func1<Course, Observable<Course>>() {
            @Override
            public Observable<Course> call(final Course course) {
                return Observable.from(course.getSections())
                        .filter(new Func1<Course.Section, Boolean>() {
                            @Override
                            public Boolean call(Section section) {
                                return section.isPrinted();
                            }
                        }).toList()
                        .map(new Func1<List<Section>, Course>() {
                            @Override
                            public Course call(List<Section> sections) {
                                course.setSections(sections);
                                return course;
                            }
                        });
            }
        };
    }

    private Func1<Course, Course> setRequestAndStubCourseInSection(final Request request) {
        return new Func1<Course, Course>() {
            @Override
            public Course call(final Course course) {
                Observable.from(course.getSections())
                        .forEach(new Action1<Course.Section>() {
                            @Override
                            public void call(Section section) {
                                section.setRequest(request);
                                section.setCourse(new Course(course));
                            }
                        });
                return course;
            }
        };
    }

    private Func1<Course, Course> setSubjectInCourse() {
        return new Func1<Course, Course>() {
            @Override
            public Course call(Course course) {
                course.setEnclosingSubject(getSubjectFromJson(course.getSubject()));
                return course;
            }
        };
    }

    public Subject getSubjectFromJson(String code) {
        List<Subject> subjectsList = mSubjectsList;
        Subject temp;
        for (int i = 0, size = subjectsList.size(); i < size; i++) {
            temp = subjectsList.get(i);
            if (temp.getCode().equals(code)) {
                return temp;
            }
        }
        return null;
    }

    private List<Subject> initSubjectList() {
        return new Gson().fromJson(RetroRutgersService.SUBJECT_JSON, new TypeToken<List<Subject>>() {
        }.getType());
    }
}
