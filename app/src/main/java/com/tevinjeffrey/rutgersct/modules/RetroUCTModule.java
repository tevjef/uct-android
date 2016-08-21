package com.tevinjeffrey.rutgersct.modules;

import com.google.gson.Gson;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.tevinjeffrey.rutgersct.data.rutgersapi.exceptions.RutgersServerIOException;
import com.tevinjeffrey.rutgersct.data.uctapi.RetroUCT;
import com.tevinjeffrey.rutgersct.data.uctapi.RetroUCTService;
import com.tevinjeffrey.rutgersct.ui.chooser.ChooserPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.course.CoursePresenterImpl;
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.subject.SubjectPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsPresenterImpl;
import com.tevinjeffrey.rutgersct.utils.BackgroundThread;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.converter.ConversionException;
import retrofit.converter.ProtoConverter;
import rx.Scheduler;

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
    public RetroUCT providesRetroUCT(RetroUCTService retroUCTService, @BackgroundThread Scheduler backgroundThread) {
        return new RetroUCT(retroUCTService, backgroundThread);
    }

    @Provides
    @Singleton
    public RetroUCTService providesUCTRestAdapter(OkHttpClient client, Gson gson) {
        OkHttpClient okClient = client.clone();
        return new RestAdapter.Builder()
                .setEndpoint("https://uct.tevindev.me/")
                .setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS)
                .setClient(new OkClient(okClient))
                .setConverter(new ProtoConverter())
                .build().create(RetroUCTService.class);
    }

}
