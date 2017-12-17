package com.tevinjeffrey.rmp.scraper.module

import com.tevinjeffrey.rmp.scraper.RMPScraper

import java.util.concurrent.TimeUnit

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

@Module
class ScraperModule {

  @Provides
  fun providesRMP(): RMPScraper {
    val okClient = OkHttpClient.Builder()
        .readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
        .connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS).build()
    return RMPScraper(okClient)
  }

  companion object {

    private val CONNECT_TIMEOUT_MILLIS: Long = 15000
    private val READ_TIMEOUT_MILLIS: Long = 20000
  }
}
