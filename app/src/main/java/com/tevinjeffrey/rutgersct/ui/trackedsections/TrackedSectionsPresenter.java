package com.tevinjeffrey.rutgersct.ui.trackedsections;

import com.tevinjeffrey.rutgersct.ui.base.StatefulPresenter;

public interface TrackedSectionsPresenter extends StatefulPresenter {
    void loadTrackedSections(boolean pullToRefresh);
    boolean isLoading();
}
