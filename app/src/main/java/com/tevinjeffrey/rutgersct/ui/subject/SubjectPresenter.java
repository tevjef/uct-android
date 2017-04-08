package com.tevinjeffrey.rutgersct.ui.subject;

import com.tevinjeffrey.rutgersct.ui.base.StatefulPresenter;

public interface SubjectPresenter extends StatefulPresenter {
  boolean isLoading();

  //When reacting to a
  void loadSubjects(boolean pullToRefresh);
}
