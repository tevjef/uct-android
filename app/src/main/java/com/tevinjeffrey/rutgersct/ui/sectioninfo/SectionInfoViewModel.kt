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
import com.tevinjeffrey.rutgersct.data.model.extensions.Utils
import com.tevinjeffrey.rutgersct.ui.SearchViewModel
import com.tevinjeffrey.rutgersct.utils.RxUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class SectionInfoViewModel : ViewModel() {

  private val TAG = this.javaClass.simpleName

  @Inject lateinit var rmp: RMP
  @Inject lateinit var uctApi: UCTApi

  private var disposable: Disposable? = null

  lateinit var sectionInfoLiveData: MutableLiveData<SectionInfoModel>
  lateinit var rmpLiveData: MutableLiveData<RMPModel>

  lateinit var searchViewModel: SearchViewModel

  fun addSection() {
    uctApi
        .subscribe(searchViewModel.buildSubscription())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { b -> SectionInfoModel(isSectionAdded = b) },
            { Timber.e(it) }
        )
  }

  fun removeSection() {
    uctApi.unsubscribe(searchViewModel.section?.topic_name)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { b -> SectionInfoModel(isSectionAdded = b) },
            { Timber.e(it) }
        )
  }

  fun loadRMP() {
    val professorsNotFound = ArrayList(searchViewModel.section?.instructors)

    cancePreviousSubscription()

    disposable = buildSearchParameters()
        .flatMap<Professor> { parameter -> rmp.getProfessor(parameter) }
        //Should need this to busness code.
        .doOnNext { professor ->
          val iterator = professorsNotFound.iterator()
          while (iterator.hasNext()) {
            val i = iterator.next()
            if (JaroWinklerDistance.getDistance(
                Utils.InstructorUtils.getLastName(i),
                professor.getLastName(),
                0.70f
            ) > .30 || JaroWinklerDistance.getDistance(
                Utils.InstructorUtils.getLastName(i),
                professor.getFirstName(),
                0.70f
            ) > .30) {
              iterator.remove()
            }
          }
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnTerminate {
          rmpLiveData.postValue(RMPModel(
              showRatingsLayout = true,
              ratingsLayoutLoading = false,
              professorNotFound = professorsNotFound.map {
                Utils.InstructorUtils.getName(it)
              }
          ))
        }
        .toList()
        .subscribe(
            { professor ->
              rmpLiveData.postValue(RMPModel(
                  showRatingsLayout = true,
                  ratingsLayoutLoading = false,
                  professor = professor
              ))
            },
            { Timber.e(it) })
  }

  fun setFabState(animate: Boolean) {
    val sectionTracked = uctApi.isTopicTracked(searchViewModel.section?.topic_name)
    sectionInfoLiveData.postValue(SectionInfoModel(
        isSectionAdded = sectionTracked,
        shouldAnimateFabIn = animate
    ))
  }

  override fun toString(): String {
    return TAG
  }

  fun toggleFab() {
    val sectionTracked = uctApi.isTopicTracked(searchViewModel.section?.topic_name)
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
          val firstName = Utils.InstructorUtils.getFirstName(instructor)
          val lastName = Utils.InstructorUtils.getLastName(instructor)

          val params = Parameter(university, department, location,
              courseNumber, firstName, lastName
          )

          Observable.just(params)
        }
  }

  private fun cancePreviousSubscription() {
    RxUtils.disposeIfNotNull(disposable)
  }

  private fun filterGenericInstructors(): Predicate<Instructor> {
    return Predicate { instructor -> Utils.InstructorUtils.getLastName(instructor) != "STAFF" }
  }
}
