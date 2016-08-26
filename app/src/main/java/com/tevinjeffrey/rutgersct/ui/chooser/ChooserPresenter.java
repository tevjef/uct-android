package com.tevinjeffrey.rutgersct.ui.chooser;

import com.tevinjeffrey.rutgersct.data.uctapi.model.University;
import com.tevinjeffrey.rutgersct.ui.base.StatefulPresenter;

public interface ChooserPresenter extends StatefulPresenter {
    University getDefaultUniversity();
    void updateDefaultUniversity(University university);
    void loadUniversities();
    void loadAvailableSemesters(String universityTopicName);
    boolean isLoading();
}
