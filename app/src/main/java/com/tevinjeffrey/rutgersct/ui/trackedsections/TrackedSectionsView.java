package com.tevinjeffrey.rutgersct.ui.trackedsections;

import com.tevinjeffrey.rutgersct.data.uctapi.search.UCTSubscription;
import com.tevinjeffrey.rutgersct.ui.base.BaseToolbarView;
import com.tevinjeffrey.rutgersct.ui.base.View;
import java.util.List;

@SuppressWarnings("BooleanParameter")
public interface TrackedSectionsView extends View, BaseToolbarView {

  void initRecyclerView();

  void initSwipeLayout();

  void setData(List<UCTSubscription> data);

  void showError(Throwable e);

  void showLayout(LayoutType showEmptyLayout);

  void showLoading(boolean pullToRefresh);
}
