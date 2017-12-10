package com.tevinjeffrey.rmp.client.module;

import com.squareup.moshi.Moshi;
import com.tevinjeffrey.rmp.client.ClientService;
import com.tevinjeffrey.rmp.client.RMPClient;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public class ClientModule {

  private static final long CONNECT_TIMEOUT_MILLIS = 6000;
  private static final long READ_TIMEOUT_MILLIS = 7000;

  @Provides
  public RMPClient providesRMPClient(Moshi moshi, OkHttpClient.Builder clientBuilder) {

    OkHttpClient client = clientBuilder
        .readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
        .connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS).build();

    return new RMPClient(new Retrofit.Builder()
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .baseUrl("http://rutgersapp.tevindev.me/api/")
        .build()
        .create(ClientService.class));
  }
}
