package com.tevinjeffrey.rutgerssoc.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.splunk.mint.Mint;
import com.tevinjeffrey.rutgerssoc.R;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeoutException;

public class SettingsFragment extends PreferenceFragment {

    SharedPreferences mPref;
    Preference syncInterval;
    Preference licenses;
    Preference about;

    Toolbar bar;
    LinearLayout root;
    public MainActivity getParentActivity() {
        return (MainActivity) getActivity();
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        mPref = PreferenceManager.getDefaultSharedPreferences(getParentActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = (LinearLayout) container.getParent().getParent();
        bar = (Toolbar) inflater.inflate(R.layout.preference_toolbar, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                root.removeView(bar);
                getParentActivity().onBackPressed();
            }
        });

        setupIntervalPref();
        setupAboutPref();
        setupLicensesPref();
        bar.setTitle("Settings");

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setupAboutPref() {
        about = findPreference("about");
        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                new MaterialDialog.Builder(getParentActivity())
                        .title(getResources().getString(R.string.application_name) + " v0.3")
                        .content(Html.fromHtml("Designed and developed by <b> Tevin Jeffrey</b> <br> " +
                                "<a href=\"http://tevinjeffrey.com/\">Website</a> "))
                        .positiveText("Ok")
                        .show();

                return true;
            }
        });
    }

    private void setupIntervalPref() {
        syncInterval = findPreference("sync_interval");
        setSummary();
        syncInterval.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                new MaterialDialog.Builder(getParentActivity())
                        .title("Set sync interval")
                        .items(R.array.intervals).alwaysCallSingleChoiceCallback()
                        .itemsCallbackSingleChoice(getInterval(), new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                setInterval(which);
                                getParentActivity().setAlarm();
                            }
                        })
                        .positiveText("Done")
                        .show();

                return true;
            }
        });
    }

    private void setupLicensesPref() {
        licenses = findPreference("licenses");
        licenses.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                final MaterialDialog progressDialog = new MaterialDialog.Builder(getParentActivity())
                        .title(R.string.gettingData)
                        .content(R.string.please_wait)
                        .progress(true, 0)
                        .show();

                Ion.with(SettingsFragment.this)
                        .load("http://tevinjeffrey.com/licenses.txt")
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String response) {
                                String content = null;

                                if (e == null && response.length() > 0) {
                                    content = response;
                                } else {
                                    if (e instanceof UnknownHostException) {
                                        content = "No internet connection.";
                                    } else if (e instanceof CancellationException) {
                                        //
                                    } else if (e instanceof IllegalStateException) {
                                        content = "The server is currently down. Try again later.";
                                    } else if (e instanceof TimeoutException) {
                                        content = "Connection timed out. Check internet connection.";
                                    } else {
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("Error", (e != null ? e.getMessage() : "An error occurred"));
                                        Mint.logExceptionMap(map, e);
                                        if (e != null)
                                            Toast.makeText(getParentActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                TextView tv = new TextView(SettingsFragment.this.getParentActivity());
                                tv.setTextSize(14);
                                tv.setTextColor(getResources().getColor(R.color.primary_text));
                                tv.setText(Html.fromHtml(content));
                                tv.setMovementMethod(LinkMovementMethod.getInstance());

                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();

                                    new MaterialDialog.Builder(getParentActivity())
                                            //.content(Html.fromHtml(content)).contentColorRes(R.color.primary_text)
                                            .customView(tv, true)
                                            .show();
                                }
                            }
                        });


                return true;
            }
        });
    }

    private void setSummary() {
        syncInterval.setSummary(getResources().getStringArray(R.array.intervals)[getInterval()]);
    }

    void setInterval(int intervalIndex) {
        mPref.edit().putInt(getResources().getString(R.string.sync_interval), intervalIndex).commit();
        setSummary();
    }
    int getInterval() {
        return mPref.getInt(getResources().getString(R.string.sync_interval), 1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        root.removeView(bar);
    }
}
