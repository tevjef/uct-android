package com.tevinjeffrey.rutgersct.ui.subject

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tevinjeffrey.rutgersct.data.UCTApi
import com.tevinjeffrey.rutgersct.utils.RxUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class SubjectViewModel : ViewModel() {

  @Inject internal lateinit var uctApi: UCTApi

  private var disposable: Disposable? = null

  private lateinit var subjectLiveData: MutableLiveData<SubjectModel>

  fun loadSubjectData(university: String, season: String, year: String): LiveData<SubjectModel> {
    if (!this::subjectLiveData.isInitialized) {
      subjectLiveData = MutableLiveData()
      loadSubjects(university, season, year)
    }

    return subjectLiveData
  }

  fun loadSubjects(university: String, season: String, year: String) {
    RxUtils.disposeIfNotNull(disposable)
    disposable = uctApi.getSubjects(university, season, year)
        .doOnSubscribe { subjectLiveData.postValue(SubjectModel(isLoading = true)) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ subjectList ->
          subjectLiveData.postValue(SubjectModel(data = subjectList))
        }, { throwable ->
          subjectLiveData.postValue(SubjectModel(error = throwable))
          Timber.e(throwable)
        })
  }
}
