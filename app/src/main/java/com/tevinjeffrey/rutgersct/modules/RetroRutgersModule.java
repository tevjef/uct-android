package com.tevinjeffrey.rutgersct.modules;

import com.google.gson.Gson;
import com.tevinjeffrey.rutgersct.data.rutgersapi.RetroRutgersService;
import com.tevinjeffrey.rutgersct.ui.chooser.ChooserPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.course.CoursePresenterImpl;
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.subject.SubjectPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsPresenterImpl;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

@Module(injects = {
    TrackedSectionsPresenterImpl.class,

    SectionInfoPresenterImpl.class,

    ChooserPresenterImpl.class,

    SubjectPresenterImpl.class,

    CoursePresenterImpl.class,
}, complete = false,
        library = true)

public class RetroRutgersModule {

  @Provides
  @Singleton
  RetroRutgersService providesRutgersRestAdapter(Gson gson) {
    return new Retrofit.Builder()
        .baseUrl("http://sis.rutgers.edu/soc/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()
        .create(RetroRutgersService.class);
  }
}
