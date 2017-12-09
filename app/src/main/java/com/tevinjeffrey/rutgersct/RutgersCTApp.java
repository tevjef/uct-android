package com.tevinjeffrey.rutgersct;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.Parser;
import com.orm.SugarContext;
import com.squareup.wire.AndroidMessage;
import com.tevinjeffrey.rutgersct.dagger.DaggerRutgersCTComponent;
import com.tevinjeffrey.rutgersct.dagger.RutgersCTComponent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Type;
import java.util.UUID;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasBroadcastReceiverInjector;
import dagger.android.HasServiceInjector;
import io.fabric.sdk.android.Fabric;
import jonathanfinerty.once.Once;
import timber.log.Timber;

public class RutgersCTApp extends MultiDexApplication implements HasActivityInjector,
    HasBroadcastReceiverInjector, HasServiceInjector {
  private static final String INSTALLATION = "INSTALLATION";
  private static String sID = null;
  private static RutgersCTComponent component;

  @Inject DispatchingAndroidInjector<Activity> dispatchingActivityInjector;
  @Inject DispatchingAndroidInjector<BroadcastReceiver> broadcastReceiverDispatchingAndroidInjector;
  @Inject DispatchingAndroidInjector<Service> serviceDispatchingAndroidInjector;

  private synchronized static String getsID(Context context) {
    if (sID == null) {
      File installation = new File(context.getFilesDir(), INSTALLATION);
      try {
        if (!installation.exists()) {
          writeInstallationFile(installation);
        }
        sID = readInstallationFile(installation);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return sID;
  }

  private static String readInstallationFile(File installation) throws IOException {
    RandomAccessFile f = new RandomAccessFile(installation, "r");
    byte[] bytes = new byte[(int) f.length()];
    f.readFully(bytes);
    f.close();
    return new String(bytes);
  }

  private static void writeInstallationFile(File installation) throws IOException {
    FileOutputStream out = new FileOutputStream(installation);
    String id = UUID.randomUUID().toString();
    out.write(id.getBytes());
    out.close();
  }

  @Override
  public void onCreate() {
    super.onCreate();

    Hawk.init(this)
        .setParser(new GsonParser(new Gson()))
        .build();

    SugarContext.init(this);
    Once.initialise(this);

    component = DaggerRutgersCTComponent
        .builder()
        .application(this)
        .build();
    component.inject(this);

    //Initalize crash reporting apis
    Fabric.with(this, new Crashlytics());
    if (BuildConfig.DEBUG) {
      //When debugging logs will go through the Android logger
      Timber.plant(new Timber.DebugTree());
    } else {
      String s = getsID(getApplicationContext());

      Crashlytics.setUserIdentifier(s);
      //Diverts logs through crash reporting APIs
      Timber.plant(new CrashReportingTree());
    }
  }

  @Override public AndroidInjector<Activity> activityInjector() {
    return dispatchingActivityInjector;
  }

  @Override protected void attachBaseContext(final Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  @Override public AndroidInjector<BroadcastReceiver> broadcastReceiverInjector() {
    return broadcastReceiverDispatchingAndroidInjector;
  }

  @Override
  public void onTerminate() {
    super.onTerminate();
    SugarContext.terminate();
  }

  @Override public AndroidInjector<Service> serviceInjector() {
    return serviceDispatchingAndroidInjector;
  }

  // A tree which logs important information for crash reporting.
  private static class CrashReportingTree extends Timber.Tree {
    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
      if (priority == Log.VERBOSE || priority == Log.DEBUG) {
        return;
      }
      if (t != null) {
        if (priority == Log.ERROR) {
          Crashlytics.logException(t);
        }
      }
      Crashlytics.log(priority, tag, message);
    }
  }

  public final class GsonParser implements Parser {
    private final Gson gson;

    public GsonParser(Gson gson) {
      this.gson = gson;
    }

    public <T> T fromJson(String content, Type type) throws JsonSyntaxException {
      T fromJson = TextUtils.isEmpty(content) ? null : this.gson.fromJson(content, type);
      if (fromJson instanceof AndroidMessage) {
        Timber.d("Is instance %s", fromJson);
      }
      return TextUtils.isEmpty(content) ? null : this.gson.fromJson(content, type);
    }

    public String toJson(Object body) {
      return this.gson.toJson(body);
    }
  }
}
