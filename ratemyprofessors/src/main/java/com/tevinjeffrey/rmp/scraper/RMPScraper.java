package com.tevinjeffrey.rmp.scraper;


import com.tevinjeffrey.rmp.common.Parameter;
import com.tevinjeffrey.rmp.common.Professor;
import com.tevinjeffrey.rmp.scraper.search.Decider;
import com.tevinjeffrey.rmp.scraper.search.Listing;
import com.tevinjeffrey.rmp.scraper.search.RatingParser;
import com.tevinjeffrey.rmp.scraper.search.SearchParser;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.tevinjeffrey.rmp.common.RMP.RMP_BASE_URL;


public class RMPScraper {

    final static String TAG = RMPScraper.class.getSimpleName();

    OkHttpClient client;

    @Inject
    public RMPScraper(OkHttpClient client) {
        this.client = client;
    }

    public Observable<Professor> findBestProfessor(final Parameter params) {
        Observable<? extends Professor> firstChoice = getProfessors(params);
        params.university = "";
        Observable<? extends Professor> secondChoice = getProfessors(params);
        
        return Observable.concat(firstChoice, secondChoice)
                .takeFirst(new Func1<Professor, Boolean>() {
                    @Override
                    public Boolean call(Professor professor) {
                        System.out.println("RMPScraper: findBestProfessor" +  professor.toString());

                        return true;
                    }
                });
    }

    public Observable<? extends Professor> getProfessors(final Parameter params) {
        return performSearch(createUrlFromParams(params))
                .flatMap(new Func1<String, Observable<ScrapeProfessor>>() {
                    @Override
                    public Observable<ScrapeProfessor> call(String response) {
                        return getProfessors(response);
                    }
                })
                .filter(new Func1<ScrapeProfessor, Boolean>() {
                    @Override
                    public Boolean call(ScrapeProfessor professor) {
                        return professor != null;
                    }
                })
                .toList()
                .flatMap(new Func1<List<ScrapeProfessor>, Observable<ScrapeProfessor>>() {
                    @Override
                    public Observable<ScrapeProfessor> call(List<ScrapeProfessor> professors) {
                        return Observable.from(Decider.determineProfessor(professors, params));
                    }
                });
    }

    public String createUrlFromParams(Parameter params) {
        return RMP_BASE_URL + "/search.jsp?stateselect=nj&query=" + params.lastName +
                                    (params.university.toLowerCase().contains("rutgers") ? "+rutgers":"");
    }

    public Observable<String> performSearch(final String url) {
        return makeRequest(url, TAG)
            .flatMap(new Func1<String, Observable<String>>() {
                @Override
                public Observable<String> call(String initalSearch) {
                    final int numberOfProfessors = SearchParser.getNumberOfProfessors(initalSearch);

                    if (numberOfProfessors <= 20) {
                        return Observable.just(initalSearch);
                    } else if (numberOfProfessors > 20) {
                        return Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {
                                if (!subscriber.isUnsubscribed()) {
                                    for (int i = 0; i <= numberOfProfessors; i += 20) {
                                        subscriber.onNext(url + "&offset=" + i);
                                    }
                                    subscriber.onCompleted();
                                }
                            }
                        }).flatMap(new Func1<String, Observable<? extends String>>() {
                            @Override
                            public Observable<? extends String> call(String url) {
                                return makeRequest(url, TAG);
                            }
                        });
                    } else {
                        return Observable.empty();
                    }
                }
            });
    }

    public Observable<String> makeRequest(String url, String tag) {
        return makeGetCall(new Request.Builder()
                .tag(tag)
                .url(url)
                .build())
                .flatMap(new Func1<Call, Observable<Response>>() {
                    @Override
                    public Observable<Response> call(Call call) {
                        return executeGetCall(call);
                    }
                })
                .flatMap(new Func1<Response, Observable<String>>() {
            @Override
            public Observable<String> call(Response response) {
                return mapResponseToString(response);
            }
        });
    }

    private Observable<ScrapeProfessor> getProfessors(String response)  {
        return Observable.from(SearchParser.getSearchResults(response))
                .filter(new Func1<Listing, Boolean>() {
                    @Override
                    public Boolean call(Listing listing) {
                        return !listing.getUrl().contains("Add");
                    }
                })
                .flatMap(new Func1<Listing, Observable<? extends ScrapeProfessor>>() {
                    @Override
                    public Observable<? extends ScrapeProfessor> call(final Listing listing) {
                        return makeRequest(RMP_BASE_URL + listing.getUrl(), TAG)
                                .flatMap(new Func1<String, Observable<ScrapeProfessor>>() {
                                    @Override
                                    public Observable<ScrapeProfessor> call(String s) {
                                        return Observable.just(RatingParser.findProfessor(listing, s));
                                    }
                                })
                                .subscribeOn(Schedulers.io());
                    }
                });
    }


    private Observable<String> mapResponseToString(final Response response) {
        System.out.println(response.request().toString());
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    try {
                        subscriber.onNext(response.body().string());
                        subscriber.onCompleted();
                    } catch (IOException e) {
                        subscriber.onError(e);
                    }
                }
            }
        });
    }

    private Observable<Call> makeGetCall(final Request request) {
        return Observable.create(new Observable.OnSubscribe<Call>() {
            @Override
            public void call(Subscriber<? super Call> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(client.newCall(request));
                    subscriber.onCompleted();
                }
            }
        });
    }

    private Observable<Response> executeGetCall(final Call call) {
        return Observable.create(new Observable.OnSubscribe<Response>() {
            @Override
            public void call(Subscriber<? super Response> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    try {
                        subscriber.onNext(call.execute());
                        subscriber.onCompleted();
                    } catch (IOException e) {
                        subscriber.onError(e);
                    }
                }
            }
        });
    }
}