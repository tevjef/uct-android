package com.tevinjeffrey.rutgersct.ui.sectioninfo;

import com.tevinjeffrey.rutgersct.ui.base.StatefulPresenter;

public interface SectionInfoPresenter extends StatefulPresenter {

  void setFabState(boolean animate);

  void toggleFab();

  void loadRMP();

  void removeSection();

  void addSection();
}
