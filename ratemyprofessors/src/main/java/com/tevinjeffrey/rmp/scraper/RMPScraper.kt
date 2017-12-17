package com.tevinjeffrey.rmp.scraper

import com.tevinjeffrey.rmp.common.Parameter
import com.tevinjeffrey.rmp.common.Professor
import com.tevinjeffrey.rmp.common.RMP.Companion.RMP_BASE_URL
import com.tevinjeffrey.rmp.scraper.search.Decider
import com.tevinjeffrey.rmp.scraper.search.RatingParser
import com.tevinjeffrey.rmp.scraper.search.SearchParser
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class RMPScraper @Inject
constructor(internal var client: OkHttpClient) {

  private fun createUrlFromParams(params: Parameter): String {
    return RMP_BASE_URL + "/search.jsp?stateselect=nj&query=" + params.lastName +
        if (params.university!!.toLowerCase().contains("rutgers")) "+rutgers" else ""
  }

  fun findBestProfessor(params: Parameter): Observable<Professor> {
    val firstChoice = getProfessors(params)
    params.university = ""
    val secondChoice = getProfessors(params)

    return Observable.concat(firstChoice, secondChoice)
        .firstElement().toObservable()
  }

  fun getProfessors(params: Parameter): Observable<out Professor> {
    return performSearch(createUrlFromParams(params))
        .flatMap({
          this.getProfessors(it)
        })
        .toList()
        .toObservable()
        .flatMap { professors ->
          Observable.fromIterable(Decider.determineProfessor(
              professors,
              params
          ))
        }
  }

  private fun makeRequest(url: String, tag: String): Observable<String> {
    return makeGetCall(Request.Builder()
        .tag(tag)
        .url(url)
        .build())
        .flatMap({ this.executeGetCall(it) })
        .flatMap({ this.mapResponseToString(it) })
  }

  private fun performSearch(url: String): Observable<String> {
    return makeRequest(url, TAG)
        .flatMap { initalSearch ->
          val numberOfProfessors = SearchParser.getNumberOfProfessors(initalSearch)
          when {
            numberOfProfessors <= 20 -> {
              Observable.just(initalSearch)
            }
            numberOfProfessors > 20 -> {
              Observable.create<String>({ e ->
                if (!e.isDisposed) {
                  var i = 0
                  while (i <= numberOfProfessors) {
                    e.onNext(url + "&offset=" + i)
                    i += 20
                  }
                  e.onComplete()
                }
              })
                  .flatMap { s -> makeRequest(s, s) }
            }
            else -> Observable.empty()
          }
        }
  }

  private fun executeGetCall(call: Call): Observable<Response> {
    return Observable.create { e ->
      if (!e.isDisposed) {
        try {
          e.onNext(call.execute())
          e.onComplete()
        } catch (thowable: IOException) {
          e.onError(thowable)
        }
      }
    }
  }

  private fun getProfessors(response: String): Observable<Professor> {
    return Observable.fromIterable(SearchParser.getSearchResults(response))
        .filter { listing -> !listing.url.contains("Add") }
        .flatMap { listing ->
          makeRequest(RMP_BASE_URL + listing.url, TAG)
              .flatMap { s -> Observable.just(RatingParser.findProfessor(listing, s)) }
              .subscribeOn(Schedulers.io())
        }
  }

  private fun makeGetCall(request: Request): Observable<Call> {
    return Observable.create { e ->
      if (!e.isDisposed) {
        e.onNext(client.newCall(request))
        e.onComplete()
      }
    }
  }

  private fun mapResponseToString(response: Response): Observable<String> {
    println(response.request().toString())
    return Observable.create { e ->
      if (!e.isDisposed) {
        try {
          e.onNext(response.body()!!.string())
          e.onComplete()
        } catch (throwable: IOException) {
          e.onError(throwable)
        }
      }
    }
  }

  companion object {
    internal val TAG = RMPScraper::class.java.simpleName
  }
}
