package com.tevinjeffrey.rutgersct.ui.chooser

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface ChooserSubcomponent : AndroidInjector<ChooserFragment> {

  fun inject(viewModel: ChooserViewModel)

  @Subcomponent.Builder
  abstract class Builder : AndroidInjector.Builder<ChooserFragment>() {
    abstract override fun build(): ChooserSubcomponent
  }
}
