package com.tevinjeffrey.rutgersct.dagger;

import android.support.v4.app.Fragment;

import com.tevinjeffrey.rutgersct.ui.chooser.ChooserFragment;
import com.tevinjeffrey.rutgersct.ui.chooser.ChooserSubcomponent;
import com.tevinjeffrey.rutgersct.ui.course.CourseFragment;
import com.tevinjeffrey.rutgersct.ui.course.CourseSubcomponent;
import com.tevinjeffrey.rutgersct.ui.courseinfo.CourseInfoFragment;
import com.tevinjeffrey.rutgersct.ui.courseinfo.CourseInfoSubcomponent;
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoFragment;
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoSubcomponent;
import com.tevinjeffrey.rutgersct.ui.subject.SubjectFragment;
import com.tevinjeffrey.rutgersct.ui.subject.SubjectSubcomponent;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsFragment;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsSubcomponent;
import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {
    TrackedSectionsSubcomponent.class,
    SubjectSubcomponent.class,
    CourseInfoSubcomponent.class,
    CourseSubcomponent.class,
    SectionInfoSubcomponent.class,
    ChooserSubcomponent.class,
    CourseSubcomponent.class
})
public abstract class FragmentBindingModule {
  @Binds
  @IntoMap
  @FragmentKey(CourseFragment.class)
  abstract AndroidInjector.Factory<? extends Fragment>
  bindCourseFragmentInjectorFactory(CourseSubcomponent.Builder builder);

  @Binds
  @IntoMap
  @FragmentKey(ChooserFragment.class)
  abstract AndroidInjector.Factory<? extends Fragment>
  bindChooserFragmentInjectorFactory(ChooserSubcomponent.Builder builder);

  @Binds
  @IntoMap
  @FragmentKey(CourseInfoFragment.class)
  abstract AndroidInjector.Factory<? extends Fragment>
  bindCourseInfoFragmentInjectorFactory(CourseInfoSubcomponent.Builder builder);

  @Binds
  @IntoMap
  @FragmentKey(SectionInfoFragment.class)
  abstract AndroidInjector.Factory<? extends Fragment>
  bindSectionInfoFragmentInjectorFactory(SectionInfoSubcomponent.Builder builder);

  @Binds
  @IntoMap
  @FragmentKey(SubjectFragment.class)
  abstract AndroidInjector.Factory<? extends Fragment>
  bindSubjectFragmentInjectorFactory(SubjectSubcomponent.Builder builder);

  @Binds
  @IntoMap
  @FragmentKey(TrackedSectionsFragment.class)
  abstract AndroidInjector.Factory<? extends Fragment>
  bindTrackedSectionsFragmentInjectorFactory(TrackedSectionsSubcomponent.Builder builder);
}
