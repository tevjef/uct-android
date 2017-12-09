package com.tevinjeffrey.rutgersct.ui.course

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tevinjeffrey.rutgersct.data.UCTApi
import com.tevinjeffrey.rutgersct.utils.RxUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class CourseViewModel : ViewModel() {

  @Inject internal lateinit var uctApi: UCTApi

  private var disposable: Disposable? = null

  lateinit var courseLiveData: MutableLiveData<CourseModel>

  fun loadCourses(topicName: String) {
    RxUtils.disposeIfNotNull(disposable)
    disposable = uctApi.getCourses(topicName)
        .doOnSubscribe { courseLiveData.postValue(CourseModel(isLoading = true)) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ courseList ->
          courseLiveData.postValue(CourseModel(data = courseList))
        }, { throwable ->
          courseLiveData.postValue(CourseModel(error = throwable))
          Timber.e(throwable)
        })
  }
}
