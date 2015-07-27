package com.tevinjeffrey.rutgersct;

import android.content.Context;
import android.text.format.Time;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.orm.SugarApp;
import com.splunk.mint.Mint;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.otto.Bus;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.database.DatabaseHandlerImpl;
import com.tevinjeffrey.rutgersct.rutgersapi.RetroRutgers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

import dagger.ObjectGraph;
import timber.log.Timber;

public class RutgersCTApp extends SugarApp {
    public static final String REFRESH_INTERVAL = "Sync Interval";
    public static final String ITEMS_IN_DATABASE = "Items in database";
    public static final String RESPONSE = "Response from server";
    public final static String SELECTED_SUBJECT = "SELECTED_SUBJECT";
    public final static String SELECTED_SECTION = "SELECTED_SECTION";
    public final static String SELECTED_COURSE = "SELECTED_COURSE";
    public final static String REQUEST = "REQUEST";
    private static final String INSTALLATION = "INSTALLATION";

    private static String sID = null;

    ObjectGraph objectGraph;


    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        RutgersCTApp rutgersCTApp = (RutgersCTApp) context.getApplicationContext();
        return rutgersCTApp.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //refWatcher = LeakCanary.install(this);

        initStetho();

        objectGraph = ObjectGraph.create(new RutgersCTModule(getApplicationContext()));

        //Initalize crash reporting apis
        //Fabric.with(this, new Crashlytics());
        if (BuildConfig.DEBUG) {
            //When debugging logs will go through the Android logger
            Timber.plant(new Timber.DebugTree());
        } else {

            Mint.enableDebug();
            Mint.initAndStartSession(this, "2974ff7f");
            //Gets a unique id for for every installation
            String s = getsID(getApplicationContext());

            //Set unique user id
            Mint.setUserIdentifier(s);
            Crashlytics.setUserIdentifier(s);

            //Diverts logs through crash roeporting APIs
            Timber.plant(new CrashReportingTree());
        }
    }

    public ObjectGraph getObjectGraph() {
        return objectGraph;
    }

    private void initStetho() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }

    private synchronized static String getsID(Context context) {
        if (sID == null) {
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try {
                if (!installation.exists())
                    writeInstallationFile(installation);
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

    public static String getTimeNow() {
        Time t = new Time();
        t.setToNow();
        return t.toString();
    }

    // A tree which logs important information for crash reporting.
    private static class CrashReportingTree extends Timber.HollowTree {
        @Override
        public void i(String message, Object... args) {
            Mint.leaveBreadcrumb(String.format(message, args));
            Crashlytics.log(String.format(message, args));
        }

        @Override
        public void i(Throwable t, String message, Object... args) {
            i(message, args); // Just add to the log.
        }

        @Override
        public void e(String message, Object... args) {
            i("ERROR: " + message, args); // Just add to the log.
        }

        @Override
        public void e(Throwable t, String message, Object... args) {
            e(message, args);
            //Crashlytics.logException(t);
            Mint.logExceptionMessage("INFO: ", String.format(message, args),
                    new Exception(t));
        }
    }

}
