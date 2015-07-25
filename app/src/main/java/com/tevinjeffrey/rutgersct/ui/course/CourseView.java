package com.tevinjeffrey.rutgersct.ui.course;

import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.ui.base.BaseToolbarView;
import com.tevinjeffrey.rutgersct.ui.base.View;

import java.util.List;

@SuppressWarnings("BooleanParameter")
public interface CourseView extends BaseToolbarView, View {

    void showLoading(boolean pullToRefresh);

    void setData(List<Course> data);

    void showError(Throwable e);

    //Shows either the default LIST layout, ERROR or EMPTY layout.
    void showLayout(LayoutType showEmptyLayout);

    void initRecyclerView();

    void initSwipeLayout();
}
