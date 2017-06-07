package com.tevinjeffrey.rmp.common;

import com.tevinjeffrey.rmp.client.RMPClient;
import com.tevinjeffrey.rmp.scraper.RMPScraper;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class RMP {
  public static final String RMP_BASE_URL = "http://www.ratemyprofessors.com";
  private final RMPClient mRMPClient;
  private final RMPScraper mRMPScraper;

  public RMP(RMPClient mRMPClient, RMPScraper mRMPScraper) {
    this.mRMPClient = mRMPClient;
    this.mRMPScraper = mRMPScraper;
  }

  public Observable<Professor> getProfessor(final Parameter params) {
    Observable<Professor> professorFromClient = mRMPClient.findProfessor(params)
        //Since this is first in the chain, don't try an error just yet.
        .onErrorResumeNext(Observable.<Professor>empty())
        .subscribeOn(Schedulers.io());
    Observable<Professor> professorFromScraper = mRMPScraper.findBestProfessor(params)
        .subscribeOn(Schedulers.io());

    return Observable.concat(professorFromClient, professorFromScraper)
        .firstElement()
        .toObservable()
        .filter(professor ->  professor != null);
  }
}
