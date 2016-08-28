package com.tevinjeffrey.rutgersct.ui.chooser;

import com.tevinjeffrey.rutgersct.data.uctapi.model.Semester;
import com.tevinjeffrey.rutgersct.data.uctapi.model.University;
import com.tevinjeffrey.rutgersct.ui.base.StatefulPresenter;

public interface ChooserPresenter extends StatefulPresenter {
    University getDefaultUniversity();
    void updateDefaultUniversity(University university);
    Semester getDefaultSemester();
    void updateSemester(Semester semester);
    void loadUniversities();
    void loadAvailableSemesters(String universityTopicName);
    boolean isLoading();
}
