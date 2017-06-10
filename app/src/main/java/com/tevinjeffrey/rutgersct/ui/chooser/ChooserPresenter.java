package com.tevinjeffrey.rutgersct.ui.chooser;

import com.tevinjeffrey.rutgersct.data.model.Semester;
import com.tevinjeffrey.rutgersct.data.model.University;
import com.tevinjeffrey.rutgersct.ui.base.StatefulPresenter;

public interface ChooserPresenter extends StatefulPresenter {
  Semester getDefaultSemester();

  University getDefaultUniversity();

  boolean isLoading();

  void loadAvailableSemesters(String universityTopicName);

  void loadUniversities();

  void updateDefaultUniversity(University university);

  void updateSemester(Semester semester);
}
