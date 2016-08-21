package com.tevinjeffrey.rutgersct.ui.chooser;

import com.tevinjeffrey.rutgersct.ui.base.StatefulPresenter;

public interface ChooserPresenter extends StatefulPresenter {
    void loadUniversities();
    void loadAvailableSemesters(String universityTopicName);
    boolean isLoading();
}
