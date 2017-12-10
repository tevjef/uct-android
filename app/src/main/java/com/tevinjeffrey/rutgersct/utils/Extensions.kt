package com.tevinjeffrey.rutgersct.utils

import android.support.annotation.StringRes
import android.view.View

fun View.getString(@StringRes resId: Int): String {
  return this.context.getString(resId)
}