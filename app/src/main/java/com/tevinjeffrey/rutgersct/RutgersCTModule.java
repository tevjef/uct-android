package com.tevinjeffrey.rutgersct;

import android.content.Context;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.database.DatabaseHandlerImpl;
import com.tevinjeffrey.rutgersct.modules.RMPModule;
import com.tevinjeffrey.rutgersct.modules.RetroRutgersModule;
import com.tevinjeffrey.rutgersct.receivers.BootReceiver;
import com.tevinjeffrey.rutgersct.receivers.DatabaseReceiver;
import com.tevinjeffrey.rutgersct.services.Alarm;
import com.tevinjeffrey.rutgersct.services.RequestService;
import com.tevinjeffrey.rutgersct.ui.MainActivity;
import com.tevinjeffrey.rutgersct.ui.SettingsActivity.SettingsFragment;
import com.tevinjeffrey.rutgersct.utils.PreferenceUtils;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = {
        Alarm.class,
        RequestService.class,
        SettingsFragment.class,
        MainActivity.class,
        BootReceiver.class,
        DatabaseReceiver.class,

}, includes = {RetroRutgersModule.class,
                RMPModule.class})

public class RutgersCTModule {

    private final Context applicationContext;

    public RutgersCTModule(Context context) {
        this.applicationContext = context;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return applicationContext;
    }

    @Provides
    @Singleton
    public DatabaseHandler providesDatabaseHandler(Bus bus) {
        return new DatabaseHandlerImpl(bus);
    }

    @Provides
    @Singleton
    public Bus providesEventBus() {
        return new Bus();
    }

    @Provides
    @Singleton
    public PreferenceUtils providesPreferenceUtils(Context context) {
        return new PreferenceUtils(context);
    }

    @Provides
    @Singleton
    public OkHttpClient providesOkHttpClient(Context context) {
        OkHttpClient client = new OkHttpClient();

        File httpCacheDir = new File(context.getCacheDir(), context.getString(R.string.app_name));
        long httpCacheSize = 50 * 1024 * 1024; // 50 MiB
        Cache cache = new Cache(httpCacheDir, httpCacheSize);
        client.setCache(cache);
        client.networkInterceptors().add(new StethoInterceptor());

        if (BuildConfig.DEBUG) {
            try {
                cache.evictAll();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return client;
    }
}
