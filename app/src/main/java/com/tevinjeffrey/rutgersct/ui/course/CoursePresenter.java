package com.tevinjeffrey.rutgersct.ui.course;

import com.tevinjeffrey.rutgersct.ui.base.StatefulPresenter;

public interface CoursePresenter extends StatefulPresenter {
    void loadCourses(boolean pullToRefresh);
    boolean isLoading();
}
