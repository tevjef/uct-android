package com.tevinjeffrey.rutgersct.ui.chooser;

import com.tevinjeffrey.rutgersct.data.uctapi.model.Semester;
import com.tevinjeffrey.rutgersct.data.uctapi.model.University;
import com.tevinjeffrey.rutgersct.ui.base.BaseToolbarView;
import com.tevinjeffrey.rutgersct.ui.base.View;
import java.util.List;

public interface ChooserView extends BaseToolbarView, View {
  void initSpinner();

  void setAvailableSemesters(List<Semester> semesters);

  void setUniversities(List<University> universities);
}