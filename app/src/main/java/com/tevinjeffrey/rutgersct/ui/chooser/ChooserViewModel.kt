package com.tevinjeffrey.rutgersct.ui.chooser

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tevinjeffrey.rutgersct.data.UCTApi
import com.tevinjeffrey.rutgersct.data.model.Semester
import com.tevinjeffrey.rutgersct.data.model.University
import com.tevinjeffrey.rutgersct.utils.RxUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ChooserViewModel : ViewModel() {

  @Inject lateinit var uctApi: UCTApi

  private var disposable: Disposable? = null

  lateinit var chooserSemesterLiveData: MutableLiveData<ChooserSemesterModel>
  lateinit var chooserUniversityLiveData: MutableLiveData<ChooserUniversityModel>

  val defaultSemester: Semester
    get() = uctApi.defaultSemester

  val defaultUniversity: University?
    get() = uctApi.defaultUniversity

  fun loadAvailableSemesters(universityTopicName: String) {
    cancePreviousSubscription()
    disposable = uctApi
        .getUniversity(universityTopicName)
        .map { university -> university.available_semesters }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { semesters ->
              chooserSemesterLiveData.postValue(ChooserSemesterModel(data = semesters))
            },
            {
              chooserSemesterLiveData.postValue(ChooserSemesterModel(error = it))
            }
        )
  }

  fun loadUniversities() {
    uctApi.universities
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { semesters ->
              chooserUniversityLiveData.postValue(ChooserUniversityModel(data = semesters))
            },
            {
              chooserUniversityLiveData.postValue(ChooserUniversityModel(error = it))
            }
        )
  }

  fun updateDefaultUniversity(university: University) {
    uctApi.defaultUniversity = university
  }

  fun updateSemester(semester: Semester) {
    uctApi.defaultSemester = semester
  }

  private fun cancePreviousSubscription() {
    RxUtils.disposeIfNotNull(disposable)
  }
}
