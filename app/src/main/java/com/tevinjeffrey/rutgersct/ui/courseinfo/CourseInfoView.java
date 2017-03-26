package com.tevinjeffrey.rutgersct.ui.courseinfo;

import com.tevinjeffrey.rutgersct.ui.base.BaseToolbarView;
import com.tevinjeffrey.rutgersct.ui.base.View;

public interface CourseInfoView extends View, BaseToolbarView {
  String SELECTED_SECTION = "SELECTED_SECTION";

  void initHeaderView();

  void initRecyclerView();

  void initViews();
}
