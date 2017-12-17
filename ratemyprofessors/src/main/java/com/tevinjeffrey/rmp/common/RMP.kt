package com.tevinjeffrey.rmp.common

import com.tevinjeffrey.rmp.client.RMPClient
import com.tevinjeffrey.rmp.scraper.RMPScraper

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class RMP(private val rmpClient: RMPClient, private val rmpScraper: RMPScraper) {

  fun getProfessor(params: Parameter): Observable<Professor> {
    val professorFromClient = rmpClient.findProfessor(params)
        //Since this is first in the chain, don't try an error just yet.
        .onErrorResumeNext(Observable.empty())
        .subscribeOn(Schedulers.io())
    val professorFromScraper = rmpScraper.findBestProfessor(params)
        .subscribeOn(Schedulers.io())

    return Observable.concat(professorFromClient, professorFromScraper)
        .firstElement()
        .toObservable()
  }

  companion object {
    val RMP_BASE_URL = "http://www.ratemyprofessors.com"
  }
}
