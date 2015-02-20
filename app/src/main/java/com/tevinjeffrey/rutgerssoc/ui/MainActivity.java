package com.tevinjeffrey.rutgerssoc.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.Fade;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;
import com.splunk.mint.Mint;
import com.tevinjeffrey.rutgerssoc.AlarmWakefulReceiver;
import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.animator.EaseOutQuint;

import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;


public class MainActivity extends ActionBarActivity {

    public final static String SUBJECTS_LIST = "SUBJECTS_LIST";
    public final static String COURSE_LIST = "COURSE_LIST";
    public final static String SELECTED_COURSE = "SELECTED_COURSE";

    public final static String REQUEST = "REQUEST";
    public final static String TRACKED_SECTION = "TRACKED_SECTION";
    public final static String COURSE_INFO_SECTION = "COURSE_INFO_SECTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            TrackedSectionsFragment tsf = new TrackedSectionsFragment();

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tsf.setEnterTransition(new AutoTransition().excludeTarget(ImageView.class, true));
                tsf.setExitTransition(new Fade(Fade.OUT).excludeTarget(ImageView.class, true));
                tsf.setReenterTransition(new AutoTransition().excludeTarget(ImageView.class, true));
                tsf.setReturnTransition(new Fade(Fade.IN).excludeTarget(ImageView.class, true));
                tsf.setAllowReturnTransitionOverlap(false);
                tsf.setAllowEnterTransitionOverlap(false);
                tsf.setSharedElementEnterTransition(new ChangeBounds().setInterpolator(new EaseOutQuint()));
                tsf.setSharedElementReturnTransition(new ChangeBounds().setInterpolator(new EaseOutQuint()));
            }
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, tsf)
                    .commit();
        }
        setPrimaryWindow();

        setAlarm();
    }

    private void setAlarm() {
        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent wakefulBroadcastReceiverIntent = new Intent(this,
                AlarmWakefulReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 1234, wakefulBroadcastReceiverIntent,
                0);

        alarmMgr.cancel(alarmIntent);
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000,
                getInterval(), alarmIntent);
    }

    long getInterval() {
        int index = PreferenceManager.getDefaultSharedPreferences(this)
                .getInt(getApplicationContext().getResources().getString(R.string.sync_interval), 1);
        if(index == 0) {
            return 5 * 60 * 1000;
        } else if(index == 1) {
            return 15 * 60 * 1000;
        } else if(index == 2) {
            return 60 * 60 * 1000;
        } else if(index == 3) {
            return 3 * 60 * 60 * 1000;
        } else {
            return 6 * 60 * 60 * 1000;
        }
    }

    public void setAccentWindow() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.accent_dark));
            window.setNavigationBarColor(getResources().getColor(R.color.accent_dark));

        }
    }

    public void setCyanWindow() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.cyan_dark));
            window.setNavigationBarColor(getResources().getColor(R.color.cyan_dark));
        }
    }

    public void setGreenWindow() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.green_dark));
            window.setNavigationBarColor(getResources().getColor(R.color.green_dark));
        }
    }

    public void setPrimaryWindow() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
            window.setNavigationBarColor(getResources().getColor(R.color.primary_dark));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
