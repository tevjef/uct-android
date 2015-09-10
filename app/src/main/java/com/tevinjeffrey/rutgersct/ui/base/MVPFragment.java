package com.tevinjeffrey.rutgersct.ui.base;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.ui.MainActivity;
import com.tevinjeffrey.rutgersct.ui.settings.SettingsActivity;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsFragment;

import butterknife.ButterKnife;
import dagger.ObjectGraph;
import icepick.Icepick;
import timber.log.Timber;

public abstract class MVPFragment extends Fragment implements View {

    public boolean mIsInitialLoad = true;

    public BasePresenter mBasePresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        Icepick.restoreInstanceState(this, savedInstanceState);
        Timber.d("%s started with savedIntanceState %s and arguments %s",
                this.toString(), savedInstanceState == null ? "null" : savedInstanceState.toString()
                , this.getArguments() == null ? "null" : this.getArguments().toString());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mBasePresenter != null) {
            mBasePresenter.onActivityCreated(savedInstanceState);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBasePresenter != null) {
            mBasePresenter.onResume();
        }
        mIsInitialLoad = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBasePresenter != null) {
            mBasePresenter.onPause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (mBasePresenter != null) {
            mBasePresenter.detachView();
        }
        //RefWatcher refWatcher = RutgersCTApp.getRefWatcher(getActivity());
        //refWatcher.watch(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_track:
                //noinspection ClassReferencesSubclass
                getParentActivity().mBackstackCount = 0;
                getFragmentManager().popBackStackImmediate(TrackedSectionsFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                return true;
            case R.id.action_settings:
                getParentActivity().startActivity(new Intent(getParentActivity(), SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Timber.d("%s paused with outState %s and arguments %s",
                this.toString(), outState == null ? "null" : outState.toString()
                , this.getArguments() == null ? "null" : this.getArguments().toString());
        super.onSaveInstanceState(outState);
        if (mBasePresenter != null) {
            mBasePresenter.onSaveInstanceState(outState);
        }
        Icepick.saveInstanceState(this, outState);
    }

    public Context getContext() {
        return getParentActivity().getApplicationContext();
    }

    public void setToolbar(Toolbar toolbar) {
        toolbar.setTitleTextAppearance(getParentActivity(), R.style.ToolbarTitleStyle);
        toolbar.setSubtitleTextAppearance(getParentActivity(), R.style.ToolbarSubtitleStyle_TextApperance);

        getParentActivity().setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                getParentActivity().onBackPressed();
            }
        });

        ActionBar actionBar = getParentActivity().getSupportActionBar();

        if (actionBar != null && getParentActivity().getBackstackCount() > 0) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

        }
    }

    public void setToolbarTitle(Toolbar toolbar, CharSequence title) {
        toolbar.setTitle(title);
    }

    public void setToolbarSubtitle(Toolbar toolbar, CharSequence subtitle) {
        toolbar.setSubtitle(subtitle);
    }

    public void startFragment(Fragment outgoingFragment, Fragment incomingFragment, FragmentTransaction ft) {
        mIsInitialLoad = true;
        ft.addToBackStack(outgoingFragment.toString()).replace(R.id.container, incomingFragment)
                .commitAllowingStateLoss();
        getParentActivity().incrementBackstackCount();
        getFragmentManager().executePendingTransactions();
    }

    public MainActivity getParentActivity() {
        return (MainActivity) getActivity();
    }


    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public ObjectGraph getObjectGraph() {
        return ((RutgersCTApp) getParentActivity().getApplication()).getObjectGraph();
    }
}
