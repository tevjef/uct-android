package com.tevinjeffrey.rutgersct.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.annotation.StringRes

import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.dagger.PerApp
import javax.inject.Inject

@PerApp
class PreferenceUtils @Inject constructor(val context: Context) {

  val canPlaySound: Boolean
    get() {
      val context = this.context

      val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
      return sharedPreferences.getBoolean(
          context.getString(R.string.pref_sound_key),
          context.resources.getBoolean(R.bool.pref_sound_default_value)
      )
    }

  var syncInterval: Int
    get() {
      val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
      return sharedPreferences.getInt(
          getString(R.string.pref_sync_interval_key),
          getInt(R.string.pref_sync_interval_default_value)
      )
    }
    set(value) {
      val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
      sharedPreferences.edit()
          .putInt(getString(R.string.pref_sync_interval_key), value)
          .commit()
    }

  private fun getInt(@StringRes strRes: Int): Int {
    return Integer.parseInt(getString(strRes))
  }

  private fun getString(@StringRes strRes: Int): String {
    return context.resources.getString(strRes)
  }
}
