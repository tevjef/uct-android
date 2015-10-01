package com.tevinjeffrey.rutgersct.modules;

import com.google.gson.Gson;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.tevinjeffrey.rutgersct.rutgersapi.RetroRutgers;
import com.tevinjeffrey.rutgersct.rutgersapi.RetroRutgersService;
import com.tevinjeffrey.rutgersct.ui.chooser.ChooserPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.course.CoursePresenterImpl;
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.subject.SubjectPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsPresenterImpl;
import com.tevinjeffrey.rutgersct.rutgersapi.exceptions.RutgersServerIOException;
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
import retrofit.converter.GsonConverter;
import rx.Scheduler;

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
    public RetroRutgers providesRetroRutgers(RetroRutgersService retroRutgersService, @BackgroundThread Scheduler backgroundThread) {
        return new RetroRutgers(retroRutgersService, backgroundThread);
    }

    @Provides
    @Singleton
    public RetroRutgersService providesRutgersRestAdapter(OkHttpClient client, Gson gson) {
        OkHttpClient okClient = client.clone();
        okClient.networkInterceptors().add(getCacheControlInterceptor(TimeUnit.SECONDS.toMillis(5)));

        return new RestAdapter.Builder()
                .setEndpoint("http://sis.rutgers.edu/soc/")
                .setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS)
                .setErrorHandler(new MyErrorHandler())
                .setClient(new OkClient(okClient))
                .setConverter(new GsonConverter(gson))
                .build().create(RetroRutgersService.class);
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

    class MyErrorHandler implements ErrorHandler {
        @Override
        public Throwable handleError(RetrofitError cause) {
            if (cause.getCause() instanceof ConversionException)
                return new RutgersServerIOException();
            if (cause.getCause() instanceof UnknownHostException)
                return cause.getCause();
            return cause;
        }
    }

}
