package com.tevinjeffrey.rutgersct.ui.base;

public interface ViewState<V extends View> {
    void apply(V view, boolean retainedState);
}
