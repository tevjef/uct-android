package com.tevinjeffrey.rutgersct.ui.chooser;

import android.os.Parcel;
import android.os.Parcelable;

import com.tevinjeffrey.rutgersct.ui.base.BaseViewState;

public class ChooserViewState extends BaseViewState<ChooserView> implements Parcelable {

    @Override
    public void apply(ChooserView view, boolean retainedState) {
        view.initToolbar();
        view.initSpinner();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public ChooserViewState() {
    }

    protected ChooserViewState(Parcel in) {

    }

    public static final Parcelable.Creator<ChooserViewState> CREATOR = new Parcelable.Creator<ChooserViewState>() {
        public ChooserViewState createFromParcel(Parcel source) {
            return new ChooserViewState(source);
        }

        public ChooserViewState[] newArray(int size) {
            return new ChooserViewState[size];
        }
    };
}
