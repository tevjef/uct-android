package com.tevinjeffrey.rutgersct.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tevinjeffrey.rutgersct.MyApplication;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.adapters.CourseInfoAdapter;
import com.tevinjeffrey.rutgersct.model.Course;
import com.tevinjeffrey.rutgersct.model.Request;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CourseInfoFragment extends BaseFragment {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    private Request request;

    public CourseInfoFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            request = savedInstanceState.getParcelable(MyApplication.REQUEST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.setAccentWindow(getParentActivity());
        setRetainInstance(true);

        final View rootView = inflater.inflate(R.layout.course_info, container, false);
        ButterKnife.inject(this, rootView);
        setUp(rootView);


        return rootView;
    }

    private void setUp(View rootView) {
        setToolbar();
        Bundle bundle = getArguments();
        Course selectedCourse = bundle.getParcelable(MyApplication.SELECTED_COURSE);
        request = bundle.getParcelable(MyApplication.REQUEST);
        inflateViews(selectedCourse, rootView);
    }

    private void setToolbar() {
        getParentActivity().setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentActivity().onBackPressed();
            }
        });
        getParentActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getParentActivity().getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void inflateViews(Course course, View rootView) {
        new CourseInfoAdapter(this, course, rootView, request).init();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MyApplication.REQUEST, request);
        outState.putParcelableArrayList(MyApplication.COURSE_LIST, getArguments().getParcelableArrayList(MyApplication.COURSE_LIST));
        outState.putParcelableArrayList(MyApplication.SUBJECTS_LIST, getArguments().getParcelableArrayList(MyApplication.SUBJECTS_LIST));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_info, menu);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}