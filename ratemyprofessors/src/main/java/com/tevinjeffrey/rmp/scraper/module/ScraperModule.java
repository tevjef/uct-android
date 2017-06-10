package com.tevinjeffrey.rmp.scraper.module;

import com.tevinjeffrey.rmp.scraper.RMPScraper;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;

@Module
public class ScraperModule {

  private static final long CONNECT_TIMEOUT_MILLIS = 15000;
  private static final long READ_TIMEOUT_MILLIS = 20000;

  @Provides
  public RMPScraper providesRMP() {
    OkHttpClient okClient = new OkHttpClient.Builder()
        .readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
        .connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS).build();
    return new RMPScraper(okClient);
  }
}
