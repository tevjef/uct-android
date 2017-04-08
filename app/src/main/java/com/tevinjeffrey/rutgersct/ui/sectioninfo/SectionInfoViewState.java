package com.tevinjeffrey.rutgersct.ui.sectioninfo;

import android.os.Parcel;
import android.os.Parcelable;
import com.tevinjeffrey.rutgersct.ui.base.BaseViewState;

public class SectionInfoViewState extends BaseViewState<SectionInfoView> implements Parcelable {

  public static final Creator<SectionInfoViewState> CREATOR = new Creator<SectionInfoViewState>() {
    @Override
    public SectionInfoViewState createFromParcel(Parcel source) {
      return new SectionInfoViewState(source);
    }

    @Override
    public SectionInfoViewState[] newArray(int size) {
      return new SectionInfoViewState[size];
    }
  };
  public boolean shouldAnimateFabIn = true;
  public boolean isSectionAdded = false;

  public SectionInfoViewState() {
  }

  protected SectionInfoViewState(Parcel in) {
    this.shouldAnimateFabIn = in.readByte() != 0;
    this.isSectionAdded = in.readByte() != 0;
  }

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
    dest.writeByte(this.shouldAnimateFabIn ? (byte) 1 : (byte) 0);
    dest.writeByte(this.isSectionAdded ? (byte) 1 : (byte) 0);
  }
}
