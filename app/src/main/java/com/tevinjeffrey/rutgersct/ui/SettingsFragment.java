package com.tevinjeffrey.rutgersct.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tevinjeffrey.rutgersct.BuildConfig;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.services.Alarm;
import com.tevinjeffrey.rutgersct.ui.base.BasePreferenceActivity;
import com.tevinjeffrey.rutgersct.utils.PreferenceUtils;

import java.net.UnknownHostException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeoutException;

import timber.log.Timber;

public class SettingsFragment extends PreferenceFragment {

    private Preference syncInterval;

    private BasePreferenceActivity getParentActivity() {
        return (BasePreferenceActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setupIntervalPref();
        setupAboutPref();
        setupLicensesPref();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        ListView list = (ListView) getView().findViewById(android.R.id.list);
        list.setDivider(new ColorDrawable(Color.parseColor("#f5f5f5")));
        list.setDividerHeight((int) getParentActivity().getResources().getDisplayMetrics().density);

        super.onActivityCreated(savedInstanceState);
    }

    private void setupAboutPref() {
        final Preference about = findPreference("about");
        about.setSummary("v" + BuildConfig.VERSION_NAME);
        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                TextView tv = new TextView(getParentActivity());
                tv.setMovementMethod(new LinkMovementMethod());
                tv.setLinkTextColor(getResources().getColor(R.color.accent));
                tv.setTextSize(18);
                tv.setTextColor(getResources().getColor(R.color.secondary_text));
                tv.setText(Html.fromHtml("Designed and developed by <b> Tevin Jeffrey</b> <br><br>" +
                        "<a href=\"http://tevinjeffrey.com/\">Website</a>" + "       <a href=\"mailto:tev.jeffrey@gmail.com\">Email</a>"));

                new MaterialDialog.Builder(getParentActivity())
                        .title(getResources().getString(R.string.application_name))
                        .iconRes(R.mipmap.ic_launcher)
                        .positiveColorRes(R.color.accent)
                        .customView(tv, true)
                        .positiveText("OK")
                        .show();
                return true;
            }
        });
    }

    private void setupIntervalPref() {
        syncInterval = findPreference(getString(R.string.pref_sync_interval_key));
        setSummary(syncInterval, getResources().getStringArray(R.array.intervals)[getInterval()]);
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
                                setSummary(syncInterval, String.format("%s",
                                        getResources().getStringArray(R.array.intervals)[getInterval()]));
                                new Alarm(getParentActivity().getApplicationContext()).setAlarm();
                            }
                        })
                        .positiveColorRes(R.color.accent)
                        .positiveText("DONE")
                        .show();

                return true;
            }
        });
    }

    private void setupLicensesPref() {
        Preference licenses = findPreference("licenses");
        licenses.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                final MaterialDialog progressDialog = new MaterialDialog.Builder(getParentActivity())
                        .title(R.string.gettingData)
                        .content(R.string.please_wait)
                        .progress(true, 0)
                        .show();

                final String url = "http://tevinjeffrey.com/licenses.txt";
                /*Ion.with(SettingsFragment.this)
                        .load(url)
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String response) {
                                String content = null;
                                if (e == null && response.length() > 0) {
                                    content = response;
                                } else {
                                    if (e instanceof UnknownHostException) {
                                        content = getResources().getString(R.string.no_internet);
                                    } else if (e instanceof IllegalStateException && !(e instanceof CancellationException)) {
                                        content = getResources().getString(R.string.server_down);
                                    } else if (e instanceof TimeoutException) {
                                        content = getResources().getString(R.string.timed_out);
                                    } else if (!(e instanceof CancellationException)) {
                                        Timber.e(e, "Crash while attempting to complete mRequest in %s to %s"
                                                , SettingsFragment.this.toString(), url);
                                    }
                                }

                                TextView tv = new TextView(SettingsFragment.this.getParentActivity());
                                tv.setTextSize(14);
                                tv.setTextColor(getResources().getColor(R.color.primary_text));
                                tv.setText(Html.fromHtml(content));
                                tv.setMovementMethod(LinkMovementMethod.getInstance());
                                tv.setLinkTextColor(getResources().getColor(R.color.accent));
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                    new MaterialDialog.Builder(getParentActivity())
                                            .customView(tv, true)
                                            .show();
                                }
                            }
                        });*/
                return true;
            }
        });
    }

    private void setSummary(Preference preference, CharSequence summary) {
        preference.setSummary(summary);
    }

    private int getInterval() {
        return PreferenceUtils.getSyncInterval();
    }

    private void setInterval(int intervalIndex) {
        PreferenceUtils.setSyncInterval(intervalIndex);
    }
}
