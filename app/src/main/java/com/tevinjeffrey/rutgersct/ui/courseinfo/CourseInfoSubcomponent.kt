package com.tevinjeffrey.rutgersct.ui.courseinfo

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface CourseInfoSubcomponent : AndroidInjector<CourseInfoFragment> {

  @Subcomponent.Builder
  abstract class Builder : AndroidInjector.Builder<CourseInfoFragment>() {
    abstract override fun build(): CourseInfoSubcomponent
  }
}
