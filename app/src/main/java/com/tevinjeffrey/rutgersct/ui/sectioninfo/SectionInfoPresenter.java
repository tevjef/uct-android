package com.tevinjeffrey.rutgersct.ui.sectioninfo;

import com.tevinjeffrey.rutgersct.ui.base.StatefulPresenter;

public interface SectionInfoPresenter extends StatefulPresenter {

  void addSection();

  void loadRMP();

  void removeSection();

  void setFabState(boolean animate);

  void toggleFab();
}
