package com.tevinjeffrey.rutgersct;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;
import com.tevinjeffrey.rmp.common.RMPModule;
import com.tevinjeffrey.rutgersct.data.uctapi.search.SearchManager;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.database.DatabaseHandlerImpl;
import com.tevinjeffrey.rutgersct.modules.RetroRutgersModule;
import com.tevinjeffrey.rutgersct.modules.RetroUCTModule;
import com.tevinjeffrey.rutgersct.receivers.DatabaseReceiver;
import com.tevinjeffrey.rutgersct.ui.MainActivity;
import com.tevinjeffrey.rutgersct.ui.chooser.ChooserFragment;
import com.tevinjeffrey.rutgersct.ui.course.CourseFragment;
import com.tevinjeffrey.rutgersct.ui.courseinfo.CourseInfoFragment;
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoFragment;
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.settings.SettingsActivity.SettingsFragment;
import com.tevinjeffrey.rutgersct.ui.subject.SubjectFragment;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsFragment;
import com.tevinjeffrey.rutgersct.utils.AndroidMainThread;
import com.tevinjeffrey.rutgersct.utils.BackgroundThread;
import com.tevinjeffrey.rutgersct.utils.PreferenceUtils;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module(
        injects = {
        SettingsFragment.class,
        MainActivity.class,
        DatabaseReceiver.class,
                TrackedSectionsFragment.class,
                ChooserFragment.class,
                SubjectFragment.class,
                CourseFragment.class,
                CourseInfoFragment.class,
                SectionInfoFragment.class,
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
    public SearchManager providesSearchManager() {
        return new SearchManager();
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
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        if (BuildConfig.DEBUG) {
            return getUnsafeOkHttpClient();
        }
        return client.build();
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            builder.addInterceptor(interceptor);

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
