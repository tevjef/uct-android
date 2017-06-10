package com.tevinjeffrey.rutgersct.ui.courseinfo;

import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoFragment;
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoPresenterImpl;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface CourseInfoSubcomponent
    extends AndroidInjector<CourseInfoFragment> {

  @Subcomponent.Builder
  abstract class Builder extends AndroidInjector.Builder<CourseInfoFragment> {
    @Override public abstract CourseInfoSubcomponent build();
  }
}
