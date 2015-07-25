package com.tevinjeffrey.rutgersct.ui.subject;

import android.os.Parcel;
import android.os.Parcelable;

import com.tevinjeffrey.rutgersct.rutgersapi.model.Subject;
import com.tevinjeffrey.rutgersct.ui.base.BaseViewState;
import com.tevinjeffrey.rutgersct.ui.base.View.LayoutType;

import java.util.ArrayList;
import java.util.List;

public class SubjectViewState extends BaseViewState<SubjectView> implements Parcelable {
    public LayoutType layoutType = LayoutType.LIST;
    public boolean snackBarShowing = false;
    public List<Subject> data = new ArrayList<>(50);
    public boolean isRefreshing = false;
    public String errorMessage;

    @Override
    public void apply(SubjectView view, boolean retainedState) {
        view.initToolbar();
        view.initRecyclerView();
        view.initSwipeLayout();

        if (retainedState) {
            view.showLoading(isRefreshing);
            view.showLayout(layoutType);
            view.setData(data);
            if ((snackBarShowing || layoutType == LayoutType.ERROR)
                    && errorMessage != null)
                view.showError(new Exception(errorMessage), false);

        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.layoutType == null ? -1 : this.layoutType.ordinal());
        dest.writeByte(snackBarShowing ? (byte) 1 : (byte) 0);
        dest.writeTypedList(data);
        dest.writeByte(isRefreshing ? (byte) 1 : (byte) 0);
        dest.writeString(this.errorMessage);
    }

    public SubjectViewState() {
    }

    protected SubjectViewState(Parcel in) {
        int tmpLayoutType = in.readInt();
        this.layoutType = tmpLayoutType == -1 ? LayoutType.LIST : LayoutType.values()[tmpLayoutType];
        this.snackBarShowing = in.readByte() != 0;
        this.data = in.createTypedArrayList(Subject.CREATOR);
        this.isRefreshing = in.readByte() != 0;
        this.errorMessage = in.readString();
    }

    public static final Parcelable.Creator<SubjectViewState> CREATOR = new Parcelable.Creator<SubjectViewState>() {
        public SubjectViewState createFromParcel(Parcel source) {
            return new SubjectViewState(source);
        }

        public SubjectViewState[] newArray(int size) {
            return new SubjectViewState[size];
        }
    };
}
