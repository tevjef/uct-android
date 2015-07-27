package com.tevinjeffrey.rutgersct.rutgersapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import com.tevinjeffrey.rutgersct.database.TrackedSection;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Subject;
import com.tevinjeffrey.rutgersct.rutgersapi.model.SystemMessage;
import com.tevinjeffrey.rutgersct.rutgersapi.utils.UrlUtils;
import com.tevinjeffrey.rutgersct.utils.RxUtils;
import com.tevinjeffrey.rutgersct.utils.exceptions.RutgersServerIOException;

import java.net.UnknownHostException;
import java.util.List;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.converter.ConversionException;
import retrofit.converter.GsonConverter;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.tevinjeffrey.rutgersct.rutgersapi.model.Course.Section;

public class RetroRutgers {

    private static final int SERVER_RETRY_COUNT = 3;
    private static final int RETRY_DELAY_MILLIS = 3000;

    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    private final RestAdapter mRestAdapter;

    RestAdapter.Builder restBuilder = new RestAdapter.Builder()
            .setEndpoint("http://sis.rutgers.edu/soc/")
            .setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS)
            .setErrorHandler(new MyErrorHandler())
            .setConverter(new GsonConverter(gson));

    List<Subject> mSubjectsList;

    public RetroRutgers(OkHttpClient client) {
        mRestAdapter = restBuilder
                .setClient(new OkClient(client))
                .build();

        mSubjectsList = initSubjectList();
    }

    public RestAdapter getRestAdapter() {
        return mRestAdapter;
    }

    public Observable<List<Subject>> getSubjects(Request request) {
        return getRetroRutgersService().getSubjects(UrlUtils.buildSubjectQuery(request));
    }

    class MyErrorHandler implements ErrorHandler {
        @Override
        public Throwable handleError(RetrofitError cause) {
            if (cause.getCause() instanceof ConversionException)
                return new RutgersServerIOException();
            if (cause.getCause() instanceof UnknownHostException)
                return cause.getCause();
            return cause;
        }
    }

    RetroRutgersService getRetroRutgersService() {
        return getRestAdapter().create(RetroRutgersService.class);
    }

    public Observable<SystemMessage> getSystemMessage() {
        return getRetroRutgersService().getSystemMessage();
    }

    public Observable<Course.Section> getTrackedSections(final List<TrackedSection> allTrackedSections) {
        return createRequestObservableFromTrackedSections(allTrackedSections)
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
                                .subscribeOn(Schedulers.io());


                    }
                })
                        //Convert every completed request in into a list and check  if all were completed successfully.
                        //Sometimes the servers bug out and course and section information will not be available.
                        // Earlier in the the flow, this would simple result in a JsonSyntaxException or an
                        // RutgersDataException. However, it can make it to this point where no section has been
                        // found. Instead of emiting an empty observable, I've converted it to a list which
                        // will then be compared to the original list of requests. Emit any items, before
                        //deciding wheter or not to pass an exeception to onError()
                .toList()
                .flatMap(new Func1<List<Course.Section>, Observable<Course.Section>>() {
                    @Override
                    public Observable<Course.Section> call(final List<Course.Section> sectionList) {
                        return Observable.create(new Observable.OnSubscribe<List<Course.Section>>() {
                            @Override
                            public void call(Subscriber<? super List<Course.Section>> subscriber) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onNext(sectionList);
                                    if (sectionList.size() != allTrackedSections.size()) {
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
        return getRetroRutgersService().getCourses(UrlUtils.buildCourseQuery(request))
                .flatMap(new Func1<List<Course>, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(final List<Course> courses) {
                        return configureCourses(courses, request);
                    }
                }).toList()
                .retryWhen(new RxUtils.RetryWithDelay(SERVER_RETRY_COUNT, RETRY_DELAY_MILLIS));
    }

    private Observable<Course> configureCourses(List<Course> courses, final Request request) {
        return Observable.from(courses)
                .map(setSubjectInCourse())
                .map(setRequestAndStubCourseInSection(request))
                .map(filterUnprintedSections());
    }

    private Func1<Course, Course> filterUnprintedSections() {
        return new Func1<Course, Course>() {
            @Override
            public Course call(Course course) {
                Observable.from(course.getSections())
                        .filter(new Func1<Course.Section, Boolean>() {
                            @Override
                            public Boolean call(Section section) {
                                return !section.isPrinted();
                            }
                        }).last();
                return course;
            }
        };
    }

    public Observable<Request> createRequestObservableFromTrackedSections(Iterable<TrackedSection> allTrackedSections) {
        return Observable.from(allTrackedSections)
                .flatMap(new Func1<TrackedSection, Observable<Request>>() {
                    @Override
                    public Observable<Request> call(TrackedSection trackedSection) {
                        return createRequest(trackedSection);
                    }
                });
    }

    private Observable<Request> createRequest(TrackedSection trackedSection) {
        return Observable.just(UrlUtils.getRequestFromTrackedSections(trackedSection));
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
        return gson.fromJson(RetroRutgersService.SUBJECT_JSON, new TypeToken<List<Subject>>() {
        }.getType());
    }
}
