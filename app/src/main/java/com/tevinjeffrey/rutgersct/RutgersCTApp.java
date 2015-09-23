package com.tevinjeffrey.rutgersct;

import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.orm.SugarApp;
import com.splunk.mint.Mint;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

import dagger.ObjectGraph;
import io.fabric.sdk.android.Fabric;
import jonathanfinerty.once.Once;
import timber.log.Timber;

public class RutgersCTApp extends SugarApp {
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

        Once.initialise(this);

        initStetho();

        objectGraph = ObjectGraph.create(new RutgersCTModule(getApplicationContext()));

        //Initalize crash reporting apis
        Fabric.with(this, new Crashlytics());
        if (BuildConfig.DEBUG) {
            //When debugging logs will go through the Android logger
            Timber.plant(new Timber.DebugTree());
        } else {

            //Mint.enableDebug();
            //Mint.initAndStartSession(this, "2974ff7f");
            //Gets a unique id for for every installation
            String s = getsID(getApplicationContext());

            //Set unique user id
            //Mint.setUserIdentifier(s);
            Crashlytics.setUserIdentifier(s);
            //Diverts logs through crash reporting APIs
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
}
