package com.tevinjeffrey.rmp.common;


import com.tevinjeffrey.rmp.client.RMPClient;
import com.tevinjeffrey.rmp.client.module.ClientModule;
import com.tevinjeffrey.rmp.scraper.RMPScraper;
import com.tevinjeffrey.rmp.scraper.module.ScraperModule;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = {ClientModule.class, ScraperModule.class},
        complete = false,
        library = true )

public class RMPModule {

    @Singleton
    @Provides
    public RMP providesRMP(RMPClient rmpClient, RMPScraper rmpScraper) {
        return new RMP(rmpClient, rmpScraper);
    }
}
