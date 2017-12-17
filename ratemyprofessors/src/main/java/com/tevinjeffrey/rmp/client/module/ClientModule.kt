package com.tevinjeffrey.rmp.client.module

import com.squareup.moshi.Moshi
import com.tevinjeffrey.rmp.client.ClientService
import com.tevinjeffrey.rmp.client.RMPClient

import java.util.concurrent.TimeUnit

import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class ClientModule {

  @Provides
  fun providesRMPClient(moshi: Moshi, clientBuilder: OkHttpClient.Builder): RMPClient {

    val client = clientBuilder
        .readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
        .connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS).build()

    return RMPClient(Retrofit.Builder()
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .baseUrl("http://rutgersapp.tevindev.me/api/")
        .build()
        .create(ClientService::class.java))
  }

  companion object {

    private val CONNECT_TIMEOUT_MILLIS: Long = 6000
    private val READ_TIMEOUT_MILLIS: Long = 7000
  }
}
