package com.tevinjeffrey.rutgersct.dagger;

import com.google.android.gms.tasks.RuntimeExecutionException;
import com.tevinjeffrey.rutgersct.data.UCTService;
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
  public UCTService providesUCTRestAdapter(OkHttpClient client) {
    RxJavaPlugins.setErrorHandler(throwable -> {
      if (throwable instanceof OnErrorNotImplementedException || throwable instanceof ProtocolViolationException) {
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
