package com.tevinjeffrey.rutgerssoc.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tevinjeffrey.rutgerssoc.R;

import butterknife.ButterKnife;

public class SettingsFragment extends PreferenceFragment {

    SharedPreferences mPref;
    Preference syncInterval;
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
        Toolbar bar = (Toolbar) inflater.inflate(R.layout.preference_toolbar, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentActivity().onBackPressed();
                root.removeViewAt(0);

            }
        });

        syncInterval = findPreference("sync_interval");
        setSummary();

        ViewGroup vg = ButterKnife.findById(root, syncInterval.getLayoutResource());
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
                            }
                        })
                        .positiveText("Done")
                        .show();

                return true;
            }
        });
        bar.setTitle("Settings");

        return super.onCreateView(inflater, container, savedInstanceState);
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        root.removeViewAt(0);
    }
}
