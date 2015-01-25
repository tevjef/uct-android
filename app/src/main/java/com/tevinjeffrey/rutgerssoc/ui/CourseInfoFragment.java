package com.tevinjeffrey.rutgerssoc.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.Request;
import com.tevinjeffrey.rutgerssoc.adapters.CourseInfoAdapter;
import com.tevinjeffrey.rutgerssoc.model.Course;

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
        getParentActivity().setAccentWindow();
        setRetainInstance(true);

        final View rootView = inflater.inflate(R.layout.course_info, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar_header_info);
        getParentActivity().setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentActivity().onBackPressed();
            }
        });
        getParentActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getParentActivity().getSupportActionBar().setDisplayShowHomeEnabled(true);


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

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            getParentActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}