package com.tevinjeffrey.rutgersct.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import com.tevinjeffrey.rutgersct.R;
import javax.inject.Inject;

public class PreferenceUtils {
  final Context context;

  public PreferenceUtils(Context context) {
    this.context = context;
  }

  public boolean getCanPlaySound() {
    Context context = this.context;

    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    return sharedPreferences.getBoolean(
        context.getString(R.string.pref_sound_key),
        context.getResources().getBoolean(R.bool.pref_sound_default_value)
    );
  }

  public int getSyncInterval() {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    return sharedPreferences.getInt(
        getString(R.string.pref_sync_interval_key),
        getInt(R.string.pref_sync_interval_default_value)
    );
  }

  public void setSyncInterval(int value) {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    sharedPreferences.edit()
        .putInt(getString(R.string.pref_sync_interval_key), value)
        .commit();
  }

  private int getInt(@StringRes int strRes) {
    return Integer.parseInt(getString(strRes));
  }

  @NonNull
  private String getString(@StringRes int strRes) {
    return context.getResources().getString(strRes);
  }
}
