package com.tevinjeffrey.rutgersct.ui.chooser;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface ChooserSubcomponent extends AndroidInjector<ChooserFragment> {

  void inject(ChooserPresenterImpl chooserPresenter);

  @Subcomponent.Builder
  abstract class Builder extends AndroidInjector.Builder<ChooserFragment> {
    @Override public abstract ChooserSubcomponent build();
  }
}
