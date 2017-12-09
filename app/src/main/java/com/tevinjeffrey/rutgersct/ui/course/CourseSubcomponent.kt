package com.tevinjeffrey.rutgersct.ui.course

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface CourseSubcomponent : AndroidInjector<CourseFragment> {

  fun inject(coursePresenter: CourseViewModel)

  @Subcomponent.Builder
  abstract class Builder : AndroidInjector.Builder<CourseFragment>() {
    abstract override fun build(): CourseSubcomponent
  }
}
