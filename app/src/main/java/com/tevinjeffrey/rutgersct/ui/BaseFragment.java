package com.tevinjeffrey.rutgersct.ui;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.koushikdutta.ion.Ion;
import com.nispok.snackbar.SnackbarManager;
import com.squareup.leakcanary.RefWatcher;
import com.tevinjeffrey.rmp.RMP;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.rutgersapi.RutgersApi;

import butterknife.ButterKnife;
import icepick.Icepick;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class BaseFragment extends Fragment {

    CompositeSubscription mCompositeSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Icepick.restoreInstanceState(this, savedInstanceState);

        Timber.i("%s started with savedIntanceState %s and arguments %s",
                this.toString(), savedInstanceState == null ? "null" : savedInstanceState.toString()
                , this.getArguments() == null ? "null" : this.getArguments().toString());
    }

    @Override
    public void onStart() {
        super.onStart();
        mCompositeSubscription = new CompositeSubscription();

    }

    void setToolbar(Toolbar toolbar) {
        toolbar.setTitleTextAppearance(getParentActivity(), R.style.ToolbarTitleStyle);
        toolbar.setSubtitleTextAppearance(getParentActivity(), R.style.ToolbarSubtitleStyle_TextApperance);
        getParentActivity().setSupportActionBar(toolbar);
    }

    void setToolbarTitle(Toolbar toolbar, CharSequence title) {
        toolbar.setTitle(title);
    }

    void setToolbarSubtitle(Toolbar toolbar, CharSequence subtitle) {
        toolbar.setSubtitle(subtitle);
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
            case android.R.id.home:
                getParentActivity().onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    void cancelRequests() {
        RutgersCTApp.getClient().cancel(RutgersApi.ACTIVITY_TAG);
        RutgersCTApp.getClient().cancel(RMP.class.getSimpleName());

        Ion.getDefault(getParentActivity()).cancelAll(getParentActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Timber.i("%s paused with outState %s and arguments %s",
                this.toString(), outState == null ? "null" : outState.toString()
                , this.getArguments() == null ? "null" : this.getArguments().toString());
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);

    }

    @Override
    public void onDestroyView() {
        mCompositeSubscription.unsubscribe();
        cancelRequests();
        SnackbarManager.dismiss();
        ButterKnife.reset(this);

        RefWatcher refWatcher = RutgersCTApp.getRefWatcher(getActivity());
        refWatcher.watch(this);

        super.onDestroyView();
    }
}
