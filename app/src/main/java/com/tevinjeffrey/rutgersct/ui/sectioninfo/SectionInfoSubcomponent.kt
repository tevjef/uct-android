package com.tevinjeffrey.rutgersct.ui.sectioninfo

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface SectionInfoSubcomponent : AndroidInjector<SectionInfoFragment> {

  fun inject(sectionInfoViewModel: SectionInfoViewModel)

  @Subcomponent.Builder
  abstract class Builder : AndroidInjector.Builder<SectionInfoFragment>() {
    abstract override fun build(): SectionInfoSubcomponent
  }
}
