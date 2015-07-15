package com.tevinjeffrey.rutgersct.animator;

import android.transition.Transition;

import timber.log.Timber;

/**
 * Created by Tevin on 6/26/2015.
 */
public class TransitionListener implements Transition.TransitionListener{

    @Override
    public void onTransitionStart(Transition transition) {
        Timber.d("-> onTransitionStart");
    }

    @Override
    public void onTransitionEnd(Transition transition) {
        Timber.d("-> onTransitionEnd");

    }

    @Override
    public void onTransitionCancel(Transition transition) {
        Timber.d("-> onTransitionCancel");

    }

    @Override
    public void onTransitionPause(Transition transition) {
        Timber.d("-> onTransitionPause");

    }

    @Override
    public void onTransitionResume(Transition transition) {
        Timber.d("-> onTransitionResume");

    }
}
