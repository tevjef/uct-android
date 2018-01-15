package com.tevinjeffrey.rutgersct.ui.chooser

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tevinjeffrey.rutgersct.data.UCTApi
import com.tevinjeffrey.rutgersct.data.model.Semester
import com.tevinjeffrey.rutgersct.data.model.University
import com.tevinjeffrey.rutgersct.utils.RxUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ChooserViewModel : ViewModel() {

  @Inject lateinit var uctApi: UCTApi

  private var disposable: Disposable? = null

  var semesterData = MutableLiveData<ChooserSemesterModel>()
  var universityData = MutableLiveData<ChooserUniversityModel>()

  var defaultSemester: Semester?
    get() = uctApi.defaultSemester
    set(value) {
      uctApi.defaultSemester = value
    }

  var defaultUniversity: University?
    get() = uctApi.defaultUniversity
    set(value) {
      uctApi.defaultUniversity = value
    }

  fun loadAvailableSemesters(universityTopicName: String) {
    cancePreviousSubscription()
    disposable = Observable.just(universityData.value?.data
        ?.firstOrNull{ it.topic_name == universityTopicName }
        ?.available_semesters.orEmpty())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            {
              if (semesterData.value?.data != it) {
                semesterData.postValue(ChooserSemesterModel(data = it))
              }
            },
            { semesterData.postValue(ChooserSemesterModel(error = it)) }
        )
  }

  fun loadUniversities() {
    uctApi.universities()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { universityData.postValue(ChooserUniversityModel(data = it)) },
            { universityData.postValue(ChooserUniversityModel(error = it)) }
        )
  }

  private fun cancePreviousSubscription() {
    RxUtils.disposeIfNotNull(disposable)
  }
}
