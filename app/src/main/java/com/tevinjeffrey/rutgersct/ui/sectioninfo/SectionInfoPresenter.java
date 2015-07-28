package com.tevinjeffrey.rutgersct.ui.sectioninfo;

import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.ui.base.StatefulPresenter;

public interface SectionInfoPresenter extends StatefulPresenter {

    void setFabState(boolean animate);

    void toggleFab();

    void loadRMP(Course.Section sectionData);

    void removeSection(Request request);

    void addSection(Request request);
}
