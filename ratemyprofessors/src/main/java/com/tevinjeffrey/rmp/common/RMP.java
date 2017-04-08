package com.tevinjeffrey.rmp.common;

import com.tevinjeffrey.rmp.client.RMPClient;
import com.tevinjeffrey.rmp.scraper.RMPScraper;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
        .takeFirst(new Func1<Professor, Boolean>() {
          @Override
          public Boolean call(Professor professor) {
            return true;
          }
        })
        .filter(new Func1<Professor, Boolean>() {
          @Override
          public Boolean call(Professor professor) {
            return professor != null;
          }
        });
  }
}
