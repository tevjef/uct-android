package com.tevinjeffrey.rutgersct.dagger

import android.arch.persistence.room.Room
import android.content.Context

import com.google.android.gms.tasks.RuntimeExecutionException
import com.tevinjeffrey.rutgersct.data.UCTService
import com.tevinjeffrey.rutgersct.data.database.PreferenceDao
import com.tevinjeffrey.rutgersct.data.database.UCTDatabase
import com.tevinjeffrey.rutgersct.data.database.UCTSubscriptionDao

import dagger.Module
import dagger.Provides
import io.reactivex.exceptions.OnErrorNotImplementedException
import io.reactivex.exceptions.ProtocolViolationException
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.wire.WireConverterFactory
import timber.log.Timber

@Module
class DataModule {

  @Provides
  @PerApp
  fun providePreferenceDao(context: Context): PreferenceDao {
    val db = Room.databaseBuilder(context.applicationContext,
        UCTDatabase::class.java, "uct")
        .allowMainThreadQueries()
        .build()
    return db.preferenceDao()
  }

  @Provides
  @PerApp
  fun provideSubscriptionDao(database: UCTDatabase): UCTSubscriptionDao {
    return database.subscriptionDao()
  }

  @Provides
  @PerApp
  fun provideUCTDatabase(context: Context): UCTDatabase {
    return Room.databaseBuilder(context.applicationContext,
        UCTDatabase::class.java, "uct")
        .allowMainThreadQueries()
        .build()
  }

  @Provides
  @PerApp
  fun providesUCTRestAdapter(client: OkHttpClient): UCTService {
    RxJavaPlugins.setErrorHandler { throwable ->
      if (throwable is OnErrorNotImplementedException || throwable is ProtocolViolationException) {
        throw RuntimeExecutionException(throwable)
      }
      Timber.e(throwable, "Error handler reported")
    }

    return Retrofit.Builder()
        .client(client)
        .addConverterFactory(WireConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .baseUrl("https://api.coursetrakr.io/")
        .build()
        .create(UCTService::class.java)
  }
}
