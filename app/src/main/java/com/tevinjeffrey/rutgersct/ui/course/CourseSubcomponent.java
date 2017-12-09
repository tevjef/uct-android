package com.tevinjeffrey.rutgersct.ui.course;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface CourseSubcomponent
    extends AndroidInjector<CourseFragment> {

  void inject(CourseViewModel coursePresenter);

  @Subcomponent.Builder
  abstract class Builder extends AndroidInjector.Builder<CourseFragment> {
    @Override public abstract CourseSubcomponent build();
  }
}
