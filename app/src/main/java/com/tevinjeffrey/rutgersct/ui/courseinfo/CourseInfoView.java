package com.tevinjeffrey.rutgersct.ui.courseinfo;

import com.tevinjeffrey.rutgersct.ui.base.BaseToolbarView;
import com.tevinjeffrey.rutgersct.ui.base.View;

public interface CourseInfoView extends View, BaseToolbarView {
    void initHeaderView();

    void initRecyclerView();

    void initViews();
}
