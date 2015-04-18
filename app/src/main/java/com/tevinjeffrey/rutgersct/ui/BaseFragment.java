package com.tevinjeffrey.rutgersct.ui;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.koushikdutta.ion.Ion;
import com.nispok.snackbar.SnackbarManager;
import com.splunk.mint.Mint;
import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.R;

import butterknife.ButterKnife;
import timber.log.Timber;

public class BaseFragment extends Fragment {

    private SharedPreferences mPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Timber.i("%s started with savedIntanceState %s and arguments %s",
                this.toString(), savedInstanceState == null ? "null" : savedInstanceState.toString()
                , this.getArguments() == null ? "null" : this.getArguments().toString());
        mPref = PreferenceManager.getDefaultSharedPreferences(getParentActivity());
        Mint.addExtraData(RutgersCTApp.REFRESH_INTERVAL, String.valueOf(getInterval()));
        Crashlytics.setString(RutgersCTApp.REFRESH_INTERVAL, String.valueOf(getInterval()));

    }

    private int getInterval() {
        return mPref.getInt(getResources().getString(R.string.sync_interval), 1);
    }

    public MainActivity getParentActivity() {
        return (MainActivity) getActivity();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_track:
                getFragmentManager().popBackStack("TrackedSectionsFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                return true;
            case R.id.action_settings:
                getParentActivity().startActivity(new Intent(getParentActivity(), BasePreferenceActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    void cancelRequests() {
        Ion.getDefault(getParentActivity()).cancelAll(getParentActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Timber.i("%s paused with outState %s and arguments %s",
                this.toString(), outState == null ? "null" : outState.toString()
                , this.getArguments() == null ? "null" : this.getArguments().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        cancelRequests();
        SnackbarManager.dismiss();
        ButterKnife.reset(this);
        super.onDestroyView();
    }
}
