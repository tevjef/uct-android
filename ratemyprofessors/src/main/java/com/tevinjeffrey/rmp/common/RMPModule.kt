package com.tevinjeffrey.rmp.common

import com.tevinjeffrey.rmp.client.RMPClient
import com.tevinjeffrey.rmp.client.module.ClientModule
import com.tevinjeffrey.rmp.scraper.RMPScraper
import com.tevinjeffrey.rmp.scraper.module.ScraperModule

import dagger.Module
import dagger.Provides

@Module(includes = [ClientModule::class, ScraperModule::class])
class RMPModule {

  @Provides
  fun providesRMP(rmpClient: RMPClient, rmpScraper: RMPScraper): RMP {
    return RMP(rmpClient, rmpScraper)
  }
}
