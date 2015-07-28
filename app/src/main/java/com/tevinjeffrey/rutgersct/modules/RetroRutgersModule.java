package com.tevinjeffrey.rutgersct.modules;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.tevinjeffrey.rutgersct.rutgersapi.RetroRutgers;
import com.tevinjeffrey.rutgersct.ui.chooser.ChooserPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.course.CoursePresenterImpl;
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.subject.SubjectPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsPresenterImpl;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = {
        TrackedSectionsPresenterImpl.class,

        SectionInfoPresenterImpl.class,

        ChooserPresenterImpl.class,

        SubjectPresenterImpl.class,

        CoursePresenterImpl.class,

}, complete = false,
        library = true )

public class RetroRutgersModule {

    @Provides
    @Singleton
    public RetroRutgers providesRetroRutgers(OkHttpClient client) {
        OkHttpClient okClient = client.clone();
        okClient.networkInterceptors().add(getCacheControlInterceptor(TimeUnit.SECONDS.toMillis(5  )));

        return new RetroRutgers(okClient);
    }

    public Interceptor getCacheControlInterceptor(final long age) {
        return new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .header("Cache-Control", "max-age=" + age)
                        .build();
            }
        };
    }
}
