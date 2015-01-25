package com.tevinjeffrey.rutgerssoc.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.koushikdutta.ion.Ion;
import com.tevinjeffrey.rutgerssoc.model.Course;
import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.model.Subject;

import java.util.ArrayList;
import java.util.Map;


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

    public String getSemesterId() {
        return semesterId;
    }

    public String getCampus() {
        return campus;
    }

    public String getLevel() {
        return level;
    }

    public String semesterId;
    public String campus;
    public String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new ChooserFragment()).addToBackStack(null)
                    .commit();
        }
        Ion.getDefault(getApplicationContext()).configure().setLogging("MyLogs", Log.DEBUG);
        setPrimaryWindow();
    }

    public void setAccentWindow() {
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.accent_dark));
        window.setNavigationBarColor(getResources().getColor(R.color.accent_dark));
    }
    public void setCyanWindow() {
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.cyan_dark));
        window.setNavigationBarColor(getResources().getColor(R.color.cyan_dark));
    }
    public void setGreenWindow() {
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.green_dark));
        window.setNavigationBarColor(getResources().getColor(R.color.green_dark));
    }
    public void setPrimaryWindow() {
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        window.setNavigationBarColor(getResources().getColor(R.color.primary_dark));
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
