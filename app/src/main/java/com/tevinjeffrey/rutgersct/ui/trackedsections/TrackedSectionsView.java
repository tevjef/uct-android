package com.tevinjeffrey.rutgersct.ui.trackedsections;

import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.ui.base.BaseToolbarView;
import com.tevinjeffrey.rutgersct.ui.base.View;

import java.util.List;

@SuppressWarnings("BooleanParameter")
public interface TrackedSectionsView extends View, BaseToolbarView {

    String REQUEST = "REQUEST";

    void showLoading(boolean pullToRefresh);

    void setData(List<Course.Section> data);

    void showError(Throwable e);

    void showLayout(LayoutType showEmptyLayout);

    void initRecyclerView();

    void initSwipeLayout();
}
