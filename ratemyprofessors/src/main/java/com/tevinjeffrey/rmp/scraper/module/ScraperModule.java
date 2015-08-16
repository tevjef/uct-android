package com.tevinjeffrey.rmp.scraper.module;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.tevinjeffrey.rmp.scraper.RMPScraper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module(injects = {
},      complete = false,
        library = true )

public class ScraperModule {

    private static final long CONNECT_TIMEOUT_MILLIS = 15000;
    private static final long READ_TIMEOUT_MILLIS = 20000;

    @Provides
    @Singleton
    public RMPScraper providesRMP(OkHttpClient client) {
        OkHttpClient okClient = client.clone();
        okClient.setConnectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        okClient.setReadTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        //okClient.networkInterceptors().add(getCacheControlInterceptor(TimeUnit.DAYS.toMillis(7)));
        return new RMPScraper(okClient);
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
