package com.tevinjeffrey.rutgersct.ui.sectioninfo;

import android.os.Parcel;
import android.os.Parcelable;

import com.tevinjeffrey.rutgersct.ui.base.BaseViewState;

public class SectionInfoViewState extends BaseViewState<SectionInfoView> implements Parcelable {

    public boolean shouldAnimateFabIn = true;

    @Override
    public void apply(SectionInfoView view, boolean retainedState) {
        view.initToolbar();
        view.initViews();
        view.showFab(shouldAnimateFabIn);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(shouldAnimateFabIn ? (byte) 1 : (byte) 0);
    }

    public SectionInfoViewState() {
    }

    protected SectionInfoViewState(Parcel in) {
        this.shouldAnimateFabIn = in.readByte() != 0;
    }

    public static final Parcelable.Creator<SectionInfoViewState> CREATOR = new Parcelable.Creator<SectionInfoViewState>() {
        public SectionInfoViewState createFromParcel(Parcel source) {
            return new SectionInfoViewState(source);
        }

        public SectionInfoViewState[] newArray(int size) {
            return new SectionInfoViewState[size];
        }
    };
}
