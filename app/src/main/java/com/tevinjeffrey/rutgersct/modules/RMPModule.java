package com.tevinjeffrey.rutgersct.modules;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.tevinjeffrey.rmp.RMP;
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoPresenterImpl;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = {
        SectionInfoPresenterImpl.class,
},      complete = false,
        library = true )

public class RMPModule {

    @Provides
    @Singleton
    public RMP providesRMP(OkHttpClient client) {
        OkHttpClient okClient = client.clone();
        okClient.networkInterceptors().add(getCacheControlInterceptor(TimeUnit.DAYS.toMillis(7)));
        return new RMP(client);
    }

    public static Interceptor getCacheControlInterceptor(final long age) {
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
