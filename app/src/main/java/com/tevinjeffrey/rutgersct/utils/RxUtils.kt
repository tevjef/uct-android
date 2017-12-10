package com.tevinjeffrey.rutgersct.utils

import io.reactivex.disposables.Disposable

object RxUtils {
  fun disposeIfNotNull(disposable: Disposable?) {
    disposable?.dispose()
  }
}
