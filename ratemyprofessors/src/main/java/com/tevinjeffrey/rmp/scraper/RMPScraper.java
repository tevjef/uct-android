package com.tevinjeffrey.rmp.scraper;

import com.tevinjeffrey.rmp.common.Parameter;
import com.tevinjeffrey.rmp.common.Professor;
import com.tevinjeffrey.rmp.scraper.search.Decider;
import com.tevinjeffrey.rmp.scraper.search.RatingParser;
import com.tevinjeffrey.rmp.scraper.search.SearchParser;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.tevinjeffrey.rmp.common.RMP.RMP_BASE_URL;

public class RMPScraper {

  final static String TAG = RMPScraper.class.getSimpleName();

  OkHttpClient client;

  @Inject
  public RMPScraper(OkHttpClient client) {
    this.client = client;
  }

  public String createUrlFromParams(Parameter params) {
    return RMP_BASE_URL + "/search.jsp?stateselect=nj&query=" + params.lastName +
        (params.university.toLowerCase().contains("rutgers") ? "+rutgers" : "");
  }

  public Observable<Professor> findBestProfessor(final Parameter params) {
    Observable<? extends Professor> firstChoice = getProfessors(params);
    params.university = "";
    Observable<? extends Professor> secondChoice = getProfessors(params);

    return Observable.concat(firstChoice, secondChoice)
        .firstElement().toObservable();
  }

  public Observable<? extends Professor> getProfessors(final Parameter params) {
    return performSearch(createUrlFromParams(params))
        .flatMap(this::getProfessors)
        .filter(professor -> professor != null)
        .toList()
        .toObservable()
        .flatMap(professors -> Observable.fromIterable(Decider.determineProfessor(
            professors,
            params
        )));
  }

  public Observable<String> makeRequest(String url, String tag) {
    return makeGetCall(new Request.Builder()
        .tag(tag)
        .url(url)
        .build())
        .flatMap(this::executeGetCall)
        .flatMap(this::mapResponseToString);
  }

  public Observable<String> performSearch(final String url) {
    return makeRequest(url, TAG)
        .flatMap(initalSearch -> {
          final int numberOfProfessors = SearchParser.getNumberOfProfessors(initalSearch);
          if (numberOfProfessors <= 20) {
            return Observable.just(initalSearch);
          } else if (numberOfProfessors > 20) {
            return Observable.create((ObservableOnSubscribe<String>) e -> {
              if (!e.isDisposed()) {
                for (int i = 0; i <= numberOfProfessors; i += 20) {
                  e.onNext(url + "&offset=" + i);
                }
                e.onComplete();
              }
            }).flatMap(s -> makeRequest(url, TAG));
          } else {
            return Observable.empty();
          }
        });
  }

  private Observable<Response> executeGetCall(final Call call) {
    return Observable.create(e -> {
      if (e != null && !e.isDisposed()) {
        try {
          e.onNext(call.execute());
          e.onComplete();
        } catch (IOException thowable) {
          e.onError(thowable);
        }
      }
    });
  }

  private Observable<ScrapeProfessor> getProfessors(String response) {
    return Observable.fromIterable(SearchParser.getSearchResults(response))
        .filter(listing -> !listing.getUrl().contains("Add"))
        .flatMap(listing -> makeRequest(RMP_BASE_URL + listing.getUrl(), TAG)
            .flatMap(s -> Observable.just(RatingParser.findProfessor(listing, s)))
            .subscribeOn(Schedulers.io()));
  }

  private Observable<Call> makeGetCall(final Request request) {
    return Observable.create(e -> {
      if (e != null && !e.isDisposed()) {
        e.onNext(client.newCall(request));
        e.onComplete();
      }
    });
  }

  private Observable<String> mapResponseToString(final Response response) {
    System.out.println(response.request().toString());
    return Observable.create(e -> {
      if (e != null && !e.isDisposed()) {
        try {
          e.onNext(response.body().string());
          e.onComplete();
        } catch (IOException throwable) {
          e.onError(throwable);
        }
      }
    });
  }
}