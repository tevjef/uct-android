package com.tevinjeffrey.rutgersct.ui.base;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

public abstract class BasePresenter implements Presenter, StatefulPresenter {

    @Nullable
    private WeakReference<View> mBaseView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    }

    @Override
    public void onDestroyView(boolean retainedState) {
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
    }

    @Override
    public void onPause() {
    }

    @Nullable
    public View getView() {
        if (mBaseView != null)
            return mBaseView.get();
        return null;
    }

    public View attachView(View view) {
        mBaseView = new WeakReference<>(view);
        return mBaseView.get();
    }

    public void detachView(boolean retainedInstance) {
        if (mBaseView != null) {
            mBaseView.clear();
        }
    }

    @Override
    public String toString() {
        return "BasePresenter";
    }
}
