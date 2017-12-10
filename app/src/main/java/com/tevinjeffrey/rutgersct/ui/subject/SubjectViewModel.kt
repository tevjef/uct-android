package com.tevinjeffrey.rutgersct.ui.subject

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tevinjeffrey.rutgersct.data.UCTApi
import com.tevinjeffrey.rutgersct.utils.RxUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SubjectViewModel : ViewModel() {

  @Inject internal lateinit var uctApi: UCTApi

  private var disposable: Disposable? = null

  val subjectData = MutableLiveData<SubjectModel>()

  fun loadSubjects(university: String, season: String, year: String) {
    RxUtils.disposeIfNotNull(disposable)
    subjectData.postValue(SubjectModel(isLoading = true))
    disposable = uctApi.getSubjects(university, season, year)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { subjectData.postValue(SubjectModel(data = it)) },
            { subjectData.postValue(SubjectModel(error = it)) }
        )
  }
}
