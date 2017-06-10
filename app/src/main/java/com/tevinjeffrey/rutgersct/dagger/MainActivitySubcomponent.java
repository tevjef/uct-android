package com.tevinjeffrey.rutgersct.dagger;

import com.tevinjeffrey.rutgersct.ui.MainActivity;
import com.tevinjeffrey.rutgersct.ui.subject.SubjectPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsPresenterImpl;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent(modules = { MainActivityModule.class, FragmentBindingModule.class })
public interface MainActivitySubcomponent extends AndroidInjector<MainActivity> {

  void inject(TrackedSectionsPresenterImpl trackedSectionsPresenter);

  void inject(SubjectPresenterImpl subjectPresenter);

  @Subcomponent.Builder
  abstract class Builder extends AndroidInjector.Builder<MainActivity> {
    @Override public abstract MainActivitySubcomponent build();
  }
}
