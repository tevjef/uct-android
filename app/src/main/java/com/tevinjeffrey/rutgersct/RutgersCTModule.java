package com.tevinjeffrey.rutgersct;

import android.content.Context;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;
import com.tevinjeffrey.rmp.common.RMPModule;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.database.DatabaseHandlerImpl;
import com.tevinjeffrey.rutgersct.modules.RetroRutgersModule;
import com.tevinjeffrey.rutgersct.modules.RetroUCTModule;
import com.tevinjeffrey.rutgersct.receivers.BootReceiver;
import com.tevinjeffrey.rutgersct.receivers.DatabaseReceiver;
import com.tevinjeffrey.rutgersct.services.Alarm;
import com.tevinjeffrey.rutgersct.services.RequestService;
import com.tevinjeffrey.rutgersct.ui.MainActivity;
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.settings.SettingsActivity.SettingsFragment;
import com.tevinjeffrey.rutgersct.utils.AndroidMainThread;
import com.tevinjeffrey.rutgersct.utils.AndroidSchedulerTransformer;
import com.tevinjeffrey.rutgersct.utils.BackgroundThread;
import com.tevinjeffrey.rutgersct.utils.PreferenceUtils;
import com.tevinjeffrey.rutgersct.utils.SchedulerTransformer;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module(
        injects = {
        Alarm.class,
        RequestService.class,
        SettingsFragment.class,
        MainActivity.class,
        BootReceiver.class,
        DatabaseReceiver.class,
        SectionInfoPresenterImpl.class,
},
        includes = {RetroRutgersModule.class, RetroUCTModule.class,
                RMPModule.class})

public class RutgersCTModule {

    private static final long CONNECT_TIMEOUT_MILLIS = 15000;
    private static final long READ_TIMEOUT_MILLIS = 20000;

    private final Context applicationContext;

    public RutgersCTModule(Context context) {
        this.applicationContext = context;
    }

    @Provides
    @Singleton
    @AndroidMainThread
    public Scheduler provideAndroidMainThread() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Singleton
    @BackgroundThread
    public Scheduler provideBackgroundThread() {
        return Schedulers.io();
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
    public Gson providesGson() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create();
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
        client.setConnectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.setReadTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.networkInterceptors().add(new StethoInterceptor());

        File httpCacheDir = new File(context.getCacheDir(), context.getString(R.string.application_name));
        long httpCacheSize = 50 * 1024 * 1024; // 50 MiB
        Cache cache = new Cache(httpCacheDir, httpCacheSize);
        client.setCache(cache);
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
