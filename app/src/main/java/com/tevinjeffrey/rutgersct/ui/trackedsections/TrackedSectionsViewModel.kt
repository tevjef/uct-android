package com.tevinjeffrey.rutgersct.ui.trackedsections

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tevinjeffrey.rutgersct.data.UCTApi
import com.tevinjeffrey.rutgersct.utils.RxUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class TrackedSectionsViewModel : ViewModel() {

  @Inject lateinit var uctApi: UCTApi

  private var disposable: Disposable? = null

  val trackedSectionsLiveData = MutableLiveData<TrackedSectionsModel>()

  fun loadTrackedSections() {
    RxUtils.disposeIfNotNull(disposable)
    disposable = uctApi.refreshSubscriptions()
        .doOnSubscribe { trackedSectionsLiveData.postValue(TrackedSectionsModel(isLoading = true)) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .toSortedList()
        .subscribe({ uctSubscriptions ->
          trackedSectionsLiveData.postValue(TrackedSectionsModel(data = uctSubscriptions))
        }, { throwable ->
          trackedSectionsLiveData.postValue(TrackedSectionsModel(error = throwable))
          Timber.e(throwable)
        })
  }

  private fun cancePreviousSubscription() {
    RxUtils.disposeIfNotNull(disposable)
  }
}
