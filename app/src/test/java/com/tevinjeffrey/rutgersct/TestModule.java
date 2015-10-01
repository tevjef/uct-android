package com.tevinjeffrey.rutgersct;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.database.MockDatabaseHandler;
import com.tevinjeffrey.rutgersct.rutgersapi.RetroRutgers;
import com.tevinjeffrey.rutgersct.rutgersapi.RetroRutgersService;
import com.tevinjeffrey.rutgersct.rutgersapi.RetroRutgersTest;
import com.tevinjeffrey.rutgersct.ui.chooser.ChooserPresenter;
import com.tevinjeffrey.rutgersct.ui.chooser.ChooserPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.course.CoursePresenterImpl;
import com.tevinjeffrey.rutgersct.ui.course.CoursePresenterImplTest;
import com.tevinjeffrey.rutgersct.ui.courseinfo.CourseInfoFragment;
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoPresenterImplTest;
import com.tevinjeffrey.rutgersct.ui.subject.SubjectPresenterImplTest;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsPresenterImplTest;
import com.tevinjeffrey.rutgersct.ui.subject.SubjectPresenterImpl;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsPresenterImpl;
import com.tevinjeffrey.rutgersct.utils.AndroidMainThread;
import com.tevinjeffrey.rutgersct.utils.BackgroundThread;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.mock;

@Module(injects = {
        TrackedSectionsPresenterImplTest.class,
        TrackedSectionsPresenterImpl.class,
        SubjectPresenterImplTest.class,
        SubjectPresenterImpl.class,
        SectionInfoPresenterImpl.class,
        SectionInfoPresenterImplTest.class,
        CoursePresenterImplTest.class,
        CoursePresenterImpl.class,
        ChooserPresenterImpl.class,
        CoursePresenterImplTest.class,
        RetroRutgersTest.class
}
        , library = true
        , complete = false)
public class TestModule {
    @Provides
    @Singleton
    public Bus provideBus() {
        return mock(Bus.class);
    }

    @Provides
    @Singleton
    public DatabaseHandler provideDatabaseHandler() {
        return new MockDatabaseHandler();
    }

    @Provides
    @Singleton
    public RetroRutgers providesRetroRutgers(RetroRutgersService service, @BackgroundThread Scheduler thread) {
        return new RetroRutgers(service, thread);
    }

    @Provides
    @Singleton
    public RetroRutgersService providesRutgersRestAdapter(OkHttpClient client, Gson gson) {
        OkHttpClient okClient = client.clone();
        return new RestAdapter.Builder()
                .setEndpoint("http://sis.rutgers.edu/soc/")
                .setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS)
                .setClient(new OkClient(okClient))
                .setConverter(new GsonConverter(gson))
                .build().create(RetroRutgersService.class);
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
    public OkHttpClient providesOkHttpClient() {
        return new OkHttpClient();
    }

    @Provides
    @Singleton
    @AndroidMainThread
    public Scheduler provideAndroidMainThread() {
        return Schedulers.immediate();
    }

    @Provides
    @Singleton
    @BackgroundThread
    public Scheduler provideBackgroundThread() {
        return Schedulers.immediate();
    }
}
