package com.tevinjeffrey.rutgersct.ui.search;

import android.os.Parcel;
import android.os.Parcelable;

import com.tevinjeffrey.rutgersct.rutgersapi.model.SystemMessage;
import com.tevinjeffrey.rutgersct.rutgersapi.utils.SemesterUtils;
import com.tevinjeffrey.rutgersct.ui.base.BaseViewState;

public class ChooserViewState extends BaseViewState<ChooserView> implements Parcelable {

    public SystemMessage systemMessage;
    public String otherSemesterText;
    public SemesterUtils.Semester otherSemesterTag;

    @Override
    public void apply(ChooserView view, boolean retainedState) {
        view.initToolbar();
        view.initPicker();

        if (systemMessage != null) {
            view.showMessage(systemMessage);
        }
        if (otherSemesterTag != null && otherSemesterText != null) {
            view.restoreOtherSemester(otherSemesterText, otherSemesterTag);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.systemMessage, 0);
        dest.writeString(this.otherSemesterText);
        dest.writeParcelable(this.otherSemesterTag, 0);
    }

    public ChooserViewState() {
    }

    protected ChooserViewState(Parcel in) {
        this.systemMessage = in.readParcelable(SystemMessage.class.getClassLoader());
        this.otherSemesterText = in.readString();
        this.otherSemesterTag = in.readParcelable(SemesterUtils.Semester.class.getClassLoader());
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
