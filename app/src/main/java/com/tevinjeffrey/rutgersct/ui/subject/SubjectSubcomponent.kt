package com.tevinjeffrey.rutgersct.ui.subject

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface SubjectSubcomponent : AndroidInjector<SubjectFragment> {

  fun inject(subjectViewModel: SubjectViewModel)

  @Subcomponent.Builder
  abstract class Builder : AndroidInjector.Builder<SubjectFragment>() {
    abstract override fun build(): SubjectSubcomponent
  }
}
