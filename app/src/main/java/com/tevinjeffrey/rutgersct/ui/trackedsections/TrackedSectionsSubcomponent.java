package com.tevinjeffrey.rutgersct.ui.trackedsections;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface TrackedSectionsSubcomponent
    extends AndroidInjector<TrackedSectionsFragment> {

  void inject(TrackedSectionsPresenterImpl trackedSectionsPresenter);

  @Subcomponent.Builder
  abstract class Builder extends AndroidInjector.Builder<TrackedSectionsFragment> {
    @Override public abstract TrackedSectionsSubcomponent build();
  }
}
