package com.tevinjeffrey.rutgerssoc.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.tevinjeffrey.rutgerssoc.model.Course;
import com.tevinjeffrey.rutgerssoc.adapters.CourseAdapter;
import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.model.Subject;
import com.tevinjeffrey.rutgerssoc.utils.CourseUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tevin on 1/14/2015.
 */
public class CourseFragment extends Fragment {

    public CourseFragment() {
    }

    private MainActivity getParentActivity() {
        return (MainActivity)getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Bundle bundle = getArguments();

        int subjectNumber = bundle.getInt("subject");

        final ListView listView = (ListView) rootView.findViewById(R.id.courses);


        String url = "http://sis.rutgers.edu/soc/courses.json?subject=" + CourseUtils.formatNumber(subjectNumber) + "&semester=12015&campus=NK&level=U";

        Ion.with(this)
                .load(url)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {

                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        Log.e("Response", result.toString());

                        Gson gson = new Gson();

                        Type listType = new TypeToken<List<Course>>() {
                        }.getType();

                        getParentActivity().setCourses((ArrayList<Course>) gson.fromJson(result.toString(), listType));

                        Log.d("Response", result.toString());


                        final CourseAdapter subjectAdapter = new CourseAdapter(getActivity(), getParentActivity().getCourses());
                        listView.setAdapter(subjectAdapter);


                    }
                });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Fragment courseInfoFragment = new CourseInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("courseIndex", position);
                courseInfoFragment.setArguments(bundle);

                getFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(R.id.container, courseInfoFragment)
                        .commit();
            }
        });


        return rootView;
    }
}