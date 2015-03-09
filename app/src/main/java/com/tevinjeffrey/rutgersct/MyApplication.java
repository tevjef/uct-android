package com.tevinjeffrey.rutgersct;

import android.content.Context;
import android.text.format.Time;

import com.crashlytics.android.Crashlytics;
import com.orm.SugarApp;
import com.splunk.mint.Mint;

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
    public final static String SUBJECTS_LIST = "SUBJECTS_LIST";
    public final static String COURSE_LIST = "COURSE_LIST";
    public final static String SELECTED_COURSE = "SELECTED_COURSE";
    public final static String REQUEST = "REQUEST";
    public final static String TRACKED_SECTION = "TRACKED_SECTION";
    public final static String COURSE_INFO_SECTION = "COURSE_INFO_SECTION";
    private static final String INSTALLATION = "INSTALLATION";
    public static String REMOVE_SECTION = "REMOVE_SECTION";
    private static String sID = null;

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

    @Override
    public void onCreate() {
        super.onCreate();

        //Initalize crash reporting apis
        Fabric.with(this, new Crashlytics());


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
            Crashlytics.logException(t);
            Mint.logExceptionMessage("INFO: ", String.format(message, args),
                    new Exception(t));
        }
    }

}
