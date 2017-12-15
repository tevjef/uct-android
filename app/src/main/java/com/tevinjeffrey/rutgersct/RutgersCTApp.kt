package com.tevinjeffrey.rutgersct

import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import android.text.TextUtils
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.google.firebase.crash.FirebaseCrash
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.orhanobut.hawk.Hawk
import com.orhanobut.hawk.Parser
import com.squareup.wire.AndroidMessage
import com.tevinjeffrey.rutgersct.dagger.DaggerRutgersAppComponent
import com.tevinjeffrey.rutgersct.dagger.RutgersAppComponent
import com.tevinjeffrey.rutgersct.dagger.RutgersAppModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasBroadcastReceiverInjector
import dagger.android.HasServiceInjector
import io.fabric.sdk.android.Fabric
import jonathanfinerty.once.Once
import timber.log.Timber
import java.lang.reflect.Type
import javax.inject.Inject

class RutgersCTApp : MultiDexApplication(), HasActivityInjector, HasBroadcastReceiverInjector, HasServiceInjector {

  @Inject lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>
  @Inject lateinit var broadcastReceiverDispatchingAndroidInjector: DispatchingAndroidInjector<BroadcastReceiver>
  @Inject lateinit var serviceDispatchingAndroidInjector: DispatchingAndroidInjector<Service>

  private lateinit var component: RutgersAppComponent

  override fun onCreate() {
    super.onCreate()

    Hawk.init(this)
        .setParser(GsonParser(Gson()))
        .build()
    Once.initialise(this)

    component = DaggerRutgersAppComponent
        .builder()
        .application(this)
        .appModule(RutgersAppModule((this)))
        .build()
    component.inject(this)

    //Initalize crash reporting apis
    Fabric.with(this, Crashlytics())
    if (BuildConfig.DEBUG) {
      //When debugging logs will go through the Android logger
      Stetho.initializeWithDefaults(this)
      Timber.plant(Timber.DebugTree())
    } else {
      //Diverts logs through crash reporting APIs
      Timber.plant(CrashReportingTree())
    }
  }

  override fun activityInjector(): AndroidInjector<Activity>? {
    return dispatchingActivityInjector
  }

  override fun attachBaseContext(base: Context) {
    super.attachBaseContext(base)
    MultiDex.install(this)
  }

  override fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver>? {
    return broadcastReceiverDispatchingAndroidInjector
  }

  override fun serviceInjector(): AndroidInjector<Service>? {
    return serviceDispatchingAndroidInjector
  }

  // A tree which logs important information for crash reporting.
  private class CrashReportingTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
      if (priority == Log.VERBOSE || priority == Log.DEBUG) {
        return
      }
      if (t != null) {
        if (priority == Log.ERROR) {
          FirebaseCrash.report(t)
          Crashlytics.logException(t)
        }
      }
      Crashlytics.log(priority, tag, message)
      FirebaseCrash.logcat(priority, tag, message)
    }
  }

  inner class GsonParser(private val gson: Gson) : Parser {

    @Throws(JsonSyntaxException::class)
    override fun <T> fromJson(content: String, type: Type): T? {
      val fromJson = if (TextUtils.isEmpty(content)) null else this.gson.fromJson<T>(content, type)
      if (fromJson is AndroidMessage<*, *>) {
        Timber.d("Is instance %s", fromJson)
      }
      return if (TextUtils.isEmpty(content)) null else this.gson.fromJson<T>(content, type)
    }

    override fun toJson(body: Any): String {
      return this.gson.toJson(body)
    }
  }
}
