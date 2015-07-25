package com.tevinjeffrey.rutgersct.ui.search;

import com.tevinjeffrey.rutgersct.ui.base.StatefulPresenter;

public interface ChooserPresenter extends StatefulPresenter {
    void loadSystemMessage();

    boolean isLoading();
}
