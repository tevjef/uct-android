package com.tevinjeffrey.rutgersct;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;
import com.tevinjeffrey.rutgersct.ui.chooser.ChooserPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.course.CoursePresenterImpl;
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.subject.SubjectPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsPresenterImpl;
import com.tevinjeffrey.rutgersct.utils.AndroidMainThread;
import com.tevinjeffrey.rutgersct.utils.BackgroundThread;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.mock;

@Module(injects = {
    TrackedSectionsPresenterImpl.class,
    SubjectPresenterImpl.class,
    SectionInfoPresenterImpl.class,
    CoursePresenterImpl.class,
    ChooserPresenterImpl.class,
}
    , library = true
    , complete = false)
public class TestModule {
  @Provides
  @Singleton
  @AndroidMainThread
  public Scheduler provideAndroidMainThread() {
    return Schedulers.immediate();
  }

  @Provides
  @Singleton
  @BackgroundThread
  public Scheduler provideBackgroundThread() {
    return Schedulers.immediate();
  }

  @Provides
  @Singleton
  public Bus provideBus() {
    return mock(Bus.class);
  }

  @Provides
  @Singleton
  public Gson providesGson() {
    return new GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .create();
  }

  @Provides
  @Singleton
  public OkHttpClient providesOkHttpClient() {
    return new OkHttpClient();
  }
}
