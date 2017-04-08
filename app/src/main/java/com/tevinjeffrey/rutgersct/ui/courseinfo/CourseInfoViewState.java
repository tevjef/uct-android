package com.tevinjeffrey.rutgersct.ui.courseinfo;

import android.os.Parcel;
import android.os.Parcelable;
import com.tevinjeffrey.rutgersct.ui.base.BaseViewState;

public class CourseInfoViewState extends BaseViewState<CourseInfoView> implements Parcelable {

  public static final Parcelable.Creator<CourseInfoViewState> CREATOR =
      new Parcelable.Creator<CourseInfoViewState>() {
        public CourseInfoViewState createFromParcel(Parcel source) {
          return new CourseInfoViewState(source);
        }

        public CourseInfoViewState[] newArray(int size) {
          return new CourseInfoViewState[size];
        }
      };

  public CourseInfoViewState() {
  }

  protected CourseInfoViewState(Parcel in) {
  }

  @Override
  public void apply(CourseInfoView view, boolean retainedState) {
    view.initToolbar();
    view.initHeaderView();
    view.initRecyclerView();
    view.initViews();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
  }
}
