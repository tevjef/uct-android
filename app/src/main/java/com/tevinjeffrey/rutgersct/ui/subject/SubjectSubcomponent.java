package com.tevinjeffrey.rutgersct.ui.subject;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface SubjectSubcomponent
    extends AndroidInjector<SubjectFragment> {

  void inject(SubjectPresenterImpl subjectPresenter);

  @Subcomponent.Builder
  abstract class Builder extends AndroidInjector.Builder<SubjectFragment> {
    @Override public abstract SubjectSubcomponent build();
  }
}
