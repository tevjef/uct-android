package com.tevinjeffrey.rutgerssoc.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.Request;
import com.tevinjeffrey.rutgerssoc.adapters.CourseAdapter;
import com.tevinjeffrey.rutgerssoc.adapters.CourseInfoAdapter;
import com.tevinjeffrey.rutgerssoc.model.Course;
import com.tevinjeffrey.rutgerssoc.model.Subject;
import com.tevinjeffrey.rutgerssoc.utils.CourseUtils;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CourseInfoFragment extends Fragment {

    private Request request;

    public CourseInfoFragment() {

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
        final View rootView = inflater.inflate(R.layout.course_info, container, false);

        Bundle bundle = getArguments();

        int courseIndex = bundle.getInt("courseIndex");
        request = bundle.getParcelable("request");

        Course selectedCourse = getSelectedCourse(courseIndex);
        inflateViews(selectedCourse, rootView);

        return rootView;
    }

    private Course getSelectedCourse(int position) {
        return getParentActivity().getCourses().get(position);
    }

    private void inflateViews(Course course, View rootView) {
        new CourseInfoAdapter(getParentActivity(), course, rootView, request).init();
    }

    private void createFragment(Bundle b) {
        SectionInfoFragment sectionInfoFragment = new SectionInfoFragment();
        sectionInfoFragment.setArguments(b);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, sectionInfoFragment).addToBackStack(null)
                .commit();
    }

    private Bundle createArgs(Parcelable parcelable) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("request", parcelable);
        return bundle;
    }
}