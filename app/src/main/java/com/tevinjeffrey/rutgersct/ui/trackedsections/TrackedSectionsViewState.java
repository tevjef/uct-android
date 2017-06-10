package com.tevinjeffrey.rutgersct.ui.trackedsections;

import android.os.Parcel;
import android.os.Parcelable;
import com.tevinjeffrey.rutgersct.data.search.UCTSubscription;
import com.tevinjeffrey.rutgersct.ui.base.BaseViewState;
import com.tevinjeffrey.rutgersct.ui.base.View.LayoutType;
import java.util.ArrayList;
import java.util.List;

public class TrackedSectionsViewState extends BaseViewState<TrackedSectionsView>
    implements Parcelable {

  public static final Creator<TrackedSectionsViewState> CREATOR =
      new Creator<TrackedSectionsViewState>() {
        @Override
        public TrackedSectionsViewState createFromParcel(Parcel source) {
          return new TrackedSectionsViewState(source);
        }

        @Override
        public TrackedSectionsViewState[] newArray(int size) {
          return new TrackedSectionsViewState[size];
        }
      };
  public boolean isRefreshing = false;
  public LayoutType layoutType = LayoutType.LIST;
  public List<UCTSubscription> data = new ArrayList<>(10);
  public boolean snackBarShowing = false;
  public String errorMessage;

  public TrackedSectionsViewState() {
  }

  protected TrackedSectionsViewState(Parcel in) {
    this.isRefreshing = in.readByte() != 0;
    int tmpLayoutType = in.readInt();
    this.layoutType = tmpLayoutType == -1 ? null : LayoutType.values()[tmpLayoutType];
    this.data = in.createTypedArrayList(UCTSubscription.CREATOR);
    this.snackBarShowing = in.readByte() != 0;
    this.errorMessage = in.readString();
  }

  @Override
  public void apply(TrackedSectionsView view, boolean retainedState) {
    view.initToolbar();
    view.initRecyclerView();
    view.initSwipeLayout();

    if (retainedState) {
      view.showLoading(isRefreshing);
      view.showLayout(layoutType);
      view.setData(data);
      if ((snackBarShowing || layoutType == LayoutType.ERROR)
          && errorMessage != null) {
        view.showError(new Exception(errorMessage));
      }
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public String toString() {
    return "TrackedSectionsViewState{" +
        "isRefreshing=" + isRefreshing +
        ", layoutType=" + layoutType +
        ", data=" + data +
        ", snackBarShowing=" + snackBarShowing +
        ", errorMessage='" + errorMessage + '\'' +
        '}';
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeByte(this.isRefreshing ? (byte) 1 : (byte) 0);
    dest.writeInt(this.layoutType == null ? -1 : this.layoutType.ordinal());
    dest.writeTypedList(this.data);
    dest.writeByte(this.snackBarShowing ? (byte) 1 : (byte) 0);
    dest.writeString(this.errorMessage);
  }
}
