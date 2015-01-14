package com.tevinjeffrey.rutgerssoc;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    ArrayList<Subject> subjects;
    ArrayList<Course> courses;
    private Fragment subjectFragment = new SubjectFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, subjectFragment ).addToBackStack(null)
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    /**
     * A placeholder fragment containing a simple view.
     */
    public class SubjectFragment extends Fragment {

        public SubjectFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);


            String url = "http://sis.rutgers.edu/soc/subjects.json?semester=12015&campus=NK&level=U";

            Ion.with(this)
                    .load(url)
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {

                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            Log.e("Response", result.toString());

                            Gson gson = new Gson();

                            ArrayList<Subject> items;
                            if (result.toString().startsWith("[")) {
                                Type listType = new TypeToken<List<Subject>>() {
                                }.getType();

                                subjects = gson.fromJson(result.toString(), listType);

                                Log.d("Response", subjects.toString());
                            }

                            ListView listView = (ListView) rootView.findViewById(R.id.courses);

                            final SubjectAdapter subjectAdapter = new SubjectAdapter(getActivity(), subjects);
                            listView.setAdapter(subjectAdapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    CourseFragment courseFragment = new CourseFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("subject", ((Subject) parent.getAdapter().getItem(position)).getCode());
                                    courseFragment.setArguments(bundle);

                                    getFragmentManager().beginTransaction()
                                            .replace(R.id.container, courseFragment).addToBackStack(null)
                                            .commit();
                                }
                            });
                        }
                    });


            return rootView;
        }

    }

    public class CourseFragment extends Fragment {

        public CourseFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            Bundle bundle = getArguments();

            int subjectNumber = bundle.getInt("subject");


            String url = "http://sis.rutgers.edu/soc/courses.json?subject=" + formatNumber(subjectNumber) + "&semester=12015&campus=NK&level=U";

            Ion.with(this)
                    .load(url)
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {

                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            Log.e("Response", result.toString());

                            Gson gson = new Gson();

                            ArrayList<Course> items;
                            if (result.toString().startsWith("[")) {
                                Type listType = new TypeToken<List<Course>>() {
                                }.getType();

                                courses = gson.fromJson(result.toString(), listType);

                                Log.d("Response", subjects.toString());
                            }

                            ListView listView = (ListView) rootView.findViewById(R.id.courses);

                            final CourseAdapter subjectAdapter = new CourseAdapter(getActivity(), courses);
                            listView.setAdapter(subjectAdapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    SubjectFragment subjectFragment = new SubjectFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("subject", ((Subject) parent.getAdapter().getItem(position)).getCode());
                                    subjectFragment.setArguments(bundle);

                                    getFragmentManager().beginTransaction().addToBackStack("subject main")
                                            .add(R.id.container, subjectFragment)
                                            .commit();
                                }
                            });
                        }
                    });






            return rootView;
        }

    }

    private String formatNumber(int subjectCode) {
        if (subjectCode <10) {
            return "00" + subjectCode;
        } else if (subjectCode < 100) {
            return "0" + subjectCode;
        } else {
            return String.valueOf(subjectCode);
        }
    }

    @Override
    public void onBackPressed() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, subjectFragment ).addToBackStack(null)
                .commit();    }
}
