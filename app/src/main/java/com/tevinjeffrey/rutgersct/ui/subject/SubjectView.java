package com.tevinjeffrey.rutgersct.ui.subject;

import com.tevinjeffrey.rutgersct.data.uctapi.model.Subject;
import com.tevinjeffrey.rutgersct.ui.base.BaseToolbarView;
import com.tevinjeffrey.rutgersct.ui.base.View;
import java.util.List;

public interface SubjectView extends View, BaseToolbarView {

  String SELECTED_SUBJECT = "SELECTED_SUBJECT";

  void showLoading(boolean pullToRefresh);

  void setData(List<Subject> data);

  void showError(Throwable e);

  void showLayout(LayoutType showEmptyLayout);

  void initRecyclerView();

  void initSwipeLayout();
}
