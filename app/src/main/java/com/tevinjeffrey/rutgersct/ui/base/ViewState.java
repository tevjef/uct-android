package com.tevinjeffrey.rutgersct.ui.base;

import android.os.Parcelable;

//An interface that defines a class that interacts with a view to restore it's state.
public interface ViewState<V extends View> extends Parcelable {
    void apply(V view, boolean retainedState);
}
