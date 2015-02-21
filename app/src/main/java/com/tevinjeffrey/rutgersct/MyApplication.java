package com.tevinjeffrey.rutgersct;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.koushikdutta.ion.Ion;
import com.orm.SugarApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class MyApplication extends SugarApp {
    public static final String REFRESH_INTERVAL = "Sync Interval";
    public static final String ITEMS_IN_DATABASE = "Items in database";

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize crash reporting
        Fabric.with(this, new Crashlytics());

        //Set unique user id
        Crashlytics.setUserIdentifier(getsID(getApplicationContext()));

        if (BuildConfig.DEBUG) {
            Ion.getDefault(getApplicationContext()).configure().setLogging("Ion", Log.VERBOSE);
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    // A tree which logs important information for crash reporting.
    private static class CrashReportingTree extends Timber.HollowTree {
        @Override public void i(String message, Object... args) {
            Crashlytics.log(String.format(message, args));
        }

        @Override public void i(Throwable t, String message, Object... args) {
            i(message, args); // Just add to the log.
        }

        @Override public void e(String message, Object... args) {
            i("ERROR: " + message, args); // Just add to the log.
        }

        @Override public void e(Throwable t, String message, Object... args) {
            e(message, args);
            Crashlytics.logException(t);
        }
    }

    private static String sID = null;
    private static final String INSTALLATION = "INSTALLATION";

    public synchronized static String getsID(Context context) {
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
        Time t= new Time();
        t.setToNow();
        return t.toString();
    }
}
