package com.tevinjeffrey.rutgersct.ui.sectioninfo;

import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;

public interface SectionInfoPresenter extends DatabaseHandler.DatabaseListener {

    void setFabState(boolean animate);

    void toggleFab();

    void loadRMP(Course.Section sectionData);

    void removeSection(Request request);

    void addSection(Request request);
}
