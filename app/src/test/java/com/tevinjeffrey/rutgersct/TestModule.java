package com.tevinjeffrey.rutgersct;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;
import com.tevinjeffrey.rutgersct.data.rutgersapi.RetroRutgers;
import com.tevinjeffrey.rutgersct.data.rutgersapi.RetroRutgersService;
import com.tevinjeffrey.rutgersct.data.rutgersapi.RetroRutgersTest;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.database.MockDatabaseHandler;
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
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.wire.WireConverterFactory;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.mock;

@Module(injects = {
    TrackedSectionsPresenterImpl.class,
    SubjectPresenterImpl.class,
    SectionInfoPresenterImpl.class,
    CoursePresenterImpl.class,
    ChooserPresenterImpl.class,
    RetroRutgersTest.class
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
  public DatabaseHandler provideDatabaseHandler() {
    return new MockDatabaseHandler();
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

  @Provides
  @Singleton
  public RetroRutgers providesRetroRutgers(
      RetroRutgersService service,
      @BackgroundThread Scheduler thread) {
    return new RetroRutgers(service, thread);
  }

  @Provides
  @Singleton
  public RetroRutgersService providesRutgersRestAdapter(OkHttpClient client, Gson gson) {
    return new Retrofit.Builder()
        .client(client)
        .addConverterFactory(WireConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
        .baseUrl("http://sis.rutgers.edu/soc/")
        .build()
        .create(RetroRutgersService.class);
  }
}
