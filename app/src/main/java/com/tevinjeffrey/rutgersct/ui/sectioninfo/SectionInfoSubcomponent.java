package com.tevinjeffrey.rutgersct.ui.sectioninfo;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface SectionInfoSubcomponent
    extends AndroidInjector<SectionInfoFragment> {

  void inject(SectionInfoPresenterImpl sectionInfoPresenter);

  @Subcomponent.Builder
  abstract class Builder extends AndroidInjector.Builder<SectionInfoFragment> {
    @Override public abstract SectionInfoSubcomponent build();
  }
}
