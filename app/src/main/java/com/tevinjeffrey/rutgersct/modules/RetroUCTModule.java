package com.tevinjeffrey.rutgersct.modules;


import com.tevinjeffrey.rutgersct.data.uctapi.RetroUCTService;
import com.tevinjeffrey.rutgersct.ui.chooser.ChooserPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.course.CoursePresenterImpl;
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.subject.SubjectPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsPresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.wire.WireConverterFactory;
import rx.schedulers.Schedulers;

@Module(injects = {
        TrackedSectionsPresenterImpl.class,

        SectionInfoPresenterImpl.class,

        ChooserPresenterImpl.class,

        SubjectPresenterImpl.class,

        CoursePresenterImpl.class,

}, complete = false,
        library = true )

public class RetroUCTModule {

    @Provides
    @Singleton
    public RetroUCTService providesUCTRestAdapter(OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(WireConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl("https://uct.tevindev.me/")
                .build()
                .create(RetroUCTService.class);
    }

}
