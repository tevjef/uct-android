package com.tevinjeffrey.rutgerssoc.ui;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.koushikdutta.ion.Ion;
import com.tevinjeffrey.rutgerssoc.AlarmWakefulReceiver;
import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.model.Course;
import com.tevinjeffrey.rutgerssoc.model.Subject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<Subject> subjects) {
        this.subjects = subjects;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    ArrayList<Subject> subjects;
    ArrayList<Course> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new ChooserFragment()).addToBackStack(null)
                    .commit();
        }
        Ion.getDefault(getApplicationContext()).configure().setLogging("MyLogs", Log.VERBOSE);
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
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);

    }

    public void setAccentWindow() {
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.accent_dark));
        window.setNavigationBarColor(getResources().getColor(R.color.accent_dark));
    }

    public void setCyanWindow() {
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.cyan_dark));
            window.setNavigationBarColor(getResources().getColor(R.color.cyan_dark));
        }
    }
    public void setGreenWindow() {
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.green_dark));
        window.setNavigationBarColor(getResources().getColor(R.color.green_dark));
    }

    public void setPrimaryWindow() {
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
