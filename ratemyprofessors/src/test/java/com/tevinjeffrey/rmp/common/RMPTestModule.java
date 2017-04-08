package com.tevinjeffrey.rmp.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.tevinjeffrey.rmp.client.RMPClient;
import com.tevinjeffrey.rmp.client.module.ClientModule;
import com.tevinjeffrey.rmp.scraper.RMPScraper;
import com.tevinjeffrey.rmp.scraper.module.ScraperModule;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(
    injects = {
        RMPTest.class
    },
    includes = { ClientModule.class, ScraperModule.class },
    complete = false,
    library = true)

public class RMPTestModule {

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

  @Singleton
  @Provides
  public RMP providesRMP(RMPClient rmpClient, RMPScraper rmpScraper) {
    return new RMP(rmpClient, rmpScraper);
  }
}