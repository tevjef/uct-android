package com.tevinjeffrey.rutgersct.ui;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.koushikdutta.ion.Ion;
import com.tevinjeffrey.rutgersct.MyApplication;
import com.tevinjeffrey.rutgersct.R;

import butterknife.ButterKnife;

public class MainFragment extends Fragment {

    SharedPreferences mPref;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mPref = PreferenceManager.getDefaultSharedPreferences(getParentActivity());
        Crashlytics.setInt(MyApplication.REFRESH_INTERVAL, getInterval());
    }

    int getInterval() {
        return mPref.getInt(getResources().getString(R.string.sync_interval), 1);
    }
    public MainActivity getParentActivity() {
        return (MainActivity) getActivity();
    }

    @Override
    public String toString() {
        return  this.getClass().getSimpleName();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_track:
                getFragmentManager().popBackStack("TrackedSectionsFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                return true;
            case R.id.action_settings:
                getFragmentManager().beginTransaction().replace(R.id.container, new SettingsFragment()).addToBackStack("SettingsFragment").commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    protected void cancelRequests() {
        Ion.getDefault(getParentActivity()).cancelAll(getParentActivity());
    }

    @Override
    public void onDestroyView() {
        cancelRequests();
        ButterKnife.reset(this);
        super.onDestroyView();
    }
}
