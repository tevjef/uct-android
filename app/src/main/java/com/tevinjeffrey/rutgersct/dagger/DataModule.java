package com.tevinjeffrey.rutgersct.dagger;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.google.android.gms.tasks.RuntimeExecutionException;
import com.tevinjeffrey.rutgersct.data.UCTService;
import com.tevinjeffrey.rutgersct.data.database.PreferenceDao;
import com.tevinjeffrey.rutgersct.data.database.UCTDatabase;
import com.tevinjeffrey.rutgersct.data.database.UCTSubscriptionDao;

import dagger.Module;
import dagger.Provides;
import io.reactivex.exceptions.OnErrorNotImplementedException;
import io.reactivex.exceptions.ProtocolViolationException;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.wire.WireConverterFactory;
import timber.log.Timber;

@Module
public class DataModule {

  @Provides
  @PerApp
  public PreferenceDao providePreferenceDao(final Context context) {
    UCTDatabase db = Room.databaseBuilder(context.getApplicationContext(),
        UCTDatabase.class, "uct")
        .allowMainThreadQueries()
        .build();
    return db.preferenceDao();
  }

  @Provides
  @PerApp
  public UCTSubscriptionDao provideSubscriptionDao(final UCTDatabase database) {
    return database.subscriptionDao();
  }

  @Provides
  @PerApp
  public UCTDatabase provideUCTDatabase(final Context context) {
    UCTDatabase db = Room.databaseBuilder(context.getApplicationContext(),
        UCTDatabase.class, "uct"
    )
        .allowMainThreadQueries()
        .build();

    return db;
  }

  @Provides
  @PerApp
  public UCTService providesUCTRestAdapter(OkHttpClient client) {
    RxJavaPlugins.setErrorHandler(throwable -> {
      if (throwable instanceof OnErrorNotImplementedException
          || throwable instanceof ProtocolViolationException) {
        throw new RuntimeExecutionException(throwable);
      }
      Timber.e(throwable, "Error handler reported");
    });

    return new Retrofit.Builder()
        .client(client)
        .addConverterFactory(WireConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .baseUrl("https://api.coursetrakr.io/")
        .build()
        .create(UCTService.class);
  }
}
