package com.tevinjeffrey.rutgersct.ui.trackedsections;

import com.tevinjeffrey.rutgersct.database.DatabaseHandlerImpl;
import com.tevinjeffrey.rutgersct.ui.base.StatefulPresenter;

public interface TrackedSectionsPresenter extends StatefulPresenter, DatabaseHandlerImpl.DatabaseListener {
    void loadTrackedSections(boolean pullToRefresh);

    boolean isLoading();
}
