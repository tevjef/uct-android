package com.tevinjeffrey.rutgersct.ui.sectioninfo

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.tevinjeffrey.rmp.common.Parameter
import com.tevinjeffrey.rmp.common.Professor
import com.tevinjeffrey.rmp.common.RMP
import com.tevinjeffrey.rmp.common.utils.JaroWinklerDistance
import com.tevinjeffrey.rutgersct.data.UCTApi
import com.tevinjeffrey.rutgersct.data.model.Instructor
import com.tevinjeffrey.rutgersct.data.model.firstName
import com.tevinjeffrey.rutgersct.data.model.lastName
import com.tevinjeffrey.rutgersct.ui.SearchViewModel
import com.tevinjeffrey.rutgersct.utils.RxUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.ArrayList
import javax.inject.Inject

class SectionInfoViewModel : ViewModel() {

  @Inject lateinit var rmp: RMP
  @Inject lateinit var uctApi: UCTApi

  private var disposable: Disposable? = null

  var sectionInfoData = MutableLiveData<SectionInfoModel>()
  var rmpData = MutableLiveData<RMPModel>()

  lateinit var searchViewModel: SearchViewModel

  private fun addSection() {
    uctApi
        .subscribeTo(searchViewModel.buildSubscription())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { sectionInfoData.postValue(SectionInfoModel(isSectionAdded = true)) },
            { Timber.e(it) }
        )
  }

  private fun removeSection() {
    uctApi
        .unsubscribeFrom(searchViewModel.section?.topic_name.orEmpty())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { sectionInfoData.postValue(SectionInfoModel(isSectionAdded = false)) },
            { Timber.e(it) }
        )
  }

  fun loadRMP() {
    val professorsNotFound = ArrayList(searchViewModel.section?.instructors)

    RxUtils.disposeIfNotNull(disposable)
    disposable = buildSearchParameters()
        .flatMap<Professor> { parameter -> rmp.getProfessor(parameter) }
        //Should need this to busness code.
        .doOnNext { professor ->
          val iterator = professorsNotFound.iterator()
          while (iterator.hasNext()) {
            val i = iterator.next()
            if (JaroWinklerDistance.getDistance(i.lastName(),
                professor.lastName.orEmpty(),
                0.70f
            ) > .30 || JaroWinklerDistance.getDistance(i.lastName(),
                professor.firstName.orEmpty(),
                0.70f
            ) > .30) {
              iterator.remove()
            }
          }
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnTerminate {
          rmpData.postValue(RMPModel(
              showRatingsLayout = true,
              ratingsLayoutLoading = false,
              professorNotFound = professorsNotFound.map { it.name }
          ))
        }
        .toList()
        .subscribe(
            {
              rmpData.postValue(RMPModel(
                  showRatingsLayout = true,
                  ratingsLayoutLoading = false,
                  professor = it
              ))
            },
            { Timber.e(it) })
  }

  fun setFabState(animate: Boolean) {
    val sectionTracked = uctApi.isTopicTracked(searchViewModel.section?.topic_name.orEmpty())
    sectionInfoData.postValue(SectionInfoModel(
        isSectionAdded = sectionTracked,
        shouldAnimateFabIn = animate
    ))
  }

  fun toggleFab() {
    val sectionTracked = uctApi.isTopicTracked(searchViewModel.section?.topic_name.orEmpty())
    if (sectionTracked) {
      removeSection()
    } else {
      Answers.getInstance().logCustom(CustomEvent("Tracked Section")
          .putCustomAttribute("Subject", searchViewModel.subject?.name)
          .putCustomAttribute("Course", searchViewModel.course?.name))
      addSection()
    }
  }

  private fun buildSearchParameters(): Observable<Parameter> {
    return Observable.fromIterable(searchViewModel.section?.instructors)
        .filter(filterGenericInstructors())
        .flatMap { instructor ->
          val university = "rutgers"
          val department = searchViewModel.subject?.name
          val location = searchViewModel.university?.name
          val courseNumber = searchViewModel.course?.number
          val firstName = instructor.firstName()
          val lastName = instructor.lastName()

          val params = Parameter(university, department, location,
              courseNumber, firstName, lastName
          )

          Observable.just(params)
        }
  }

  private fun filterGenericInstructors(): Predicate<Instructor> {
    return Predicate { instructor -> instructor.lastName() != "STAFF" }
  }
}
