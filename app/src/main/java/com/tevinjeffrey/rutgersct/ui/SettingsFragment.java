package com.tevinjeffrey.rutgersct.ui;

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

import com.afollestad.materialdialogs.MaterialDialog;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.tevinjeffrey.rutgersct.BuildConfig;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.services.Alarm;

import java.net.UnknownHostException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeoutException;

import timber.log.Timber;

public class SettingsFragment extends PreferenceFragment {

    private SharedPreferences mPref;
    private Preference syncInterval;

    private Toolbar mToolbar;
    private LinearLayout root;

    MainActivity getParentActivity() {
        return (MainActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        mPref = PreferenceManager.getDefaultSharedPreferences(getParentActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = (LinearLayout) container.getParent().getParent();
        mToolbar = (Toolbar) inflater.inflate(R.layout.preference_toolbar, root, false);
        mToolbar.setTitleTextAppearance(getParentActivity(), R.style.toolbar_title);
        mToolbar.setSubtitleTextAppearance(getParentActivity(), R.style.toolbar_subtitle);
        root.addView(mToolbar, 0); // insert at top
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                root.removeView(mToolbar);
                getParentActivity().onBackPressed();
            }
        });

        setupIntervalPref();
        setupAboutPref();
        setupLicensesPref();
        mToolbar.setTitle("Settings");

        return super.onCreateView(inflater, container, savedInstanceState);
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
                tv.setTextColor(getResources().getColor(R.color.primary_text));
                tv.setText(Html.fromHtml("Designed and developed by <b> Tevin Jeffrey</b> <br><br>" +
                        "<a href=\"http://tevinjeffrey.com/\">Website</a> "));

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
        syncInterval = findPreference("sync_interval");
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
                                setSummary(syncInterval, String.format("Every %s",
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
                Ion.with(SettingsFragment.this)
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
                                        Timber.e(e, "Crash while attempting to complete request in %s to %s"
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
                        });
                return true;
            }
        });
    }

    private void setSummary(Preference preference, String summary) {
        preference.setSummary(summary);
    }

    int getInterval() {
        return mPref.getInt(getResources().getString(R.string.sync_interval), 1);
    }

    void setInterval(int intervalIndex) {
        mPref.edit().putInt(getResources().getString(R.string.sync_interval), intervalIndex).commit();
    }

    @Override
    public void onDestroyView() {
        root.removeView(mToolbar);
        super.onDestroyView();
    }
}
