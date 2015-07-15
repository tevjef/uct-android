package com.tevinjeffrey.rutgersct.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.BoolRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.RutgersCTApp;

public class PreferenceUtils {

    public static boolean getLearnedServerIssues() {
        Context context = RutgersCTApp.getInstance().getApplicationContext();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(context.getString(R.string.pref_learned_server_issues_key),
                context.getResources().getBoolean(R.bool.pref_learned_server_issues_default_value));
    }

    public static void setLearnedServerIssues(boolean learned) {
        Context context = RutgersCTApp.getInstance().getApplicationContext();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit()
                .putBoolean(getString(R.string.pref_learned_server_issues_key), learned)
                .commit();

    }

    public static int getSyncInterval() {
        Context context = RutgersCTApp.getInstance().getApplicationContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(getString(R.string.pref_sync_interval_key), getInt(R.string.pref_sync_interval_default_value));
    }

    public static void setSyncInterval(int value) {
        Context context = RutgersCTApp.getInstance().getApplicationContext();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit()
                .putInt(getString(R.string.pref_sync_interval_key), value)
                .commit();

    }


    @NonNull
    private static String getString(@StringRes int strRes) {
        Context context = RutgersCTApp.getInstance().getApplicationContext();
        return context.getResources().getString(strRes);
    }

    private static int getInt(@StringRes int strRes) {
        return Integer.parseInt(getString(strRes));
    }

    private static boolean getBool(@BoolRes int boolRes) {
        Context context = RutgersCTApp.getInstance().getApplicationContext();
        return context.getResources().getBoolean(boolRes);
    }
}
