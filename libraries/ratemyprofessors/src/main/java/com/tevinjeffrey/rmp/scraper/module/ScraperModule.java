package com.tevinjeffrey.rmp.scraper.module;

import com.tevinjeffrey.rmp.scraper.RMPScraper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;


@Module(injects = {
},      complete = false,
        library = true )

public class ScraperModule {

    private static final long CONNECT_TIMEOUT_MILLIS = 15000;
    private static final long READ_TIMEOUT_MILLIS = 20000;

    @Provides
    @Singleton
    public RMPScraper providesRMP() {
        OkHttpClient okClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS).build();
        return new RMPScraper(okClient);
    }
}
