package com.tevinjeffrey.rutgersct.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.adapters.SectionInfoAdapter;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import icepick.Icicle;


public class SectionInfoFragment extends BaseFragment {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @Icicle
    ArrayList<Course> courseList;

    @Icicle
    Request request;

    public SectionInfoFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseList = getArguments().getParcelableArrayList(RutgersCTApp.COURSE_LIST);
            request = getArguments().getParcelable(RutgersCTApp.REQUEST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.setPrimaryWindow(getParentActivity());

        setRetainInstance(true);

        final View rootView = inflater.inflate(R.layout.fragment_section_info, container, false);

        ButterKnife.inject(this, rootView);

        initViews(rootView);

        return rootView;
    }

    private void initViews(View rootView) {
        setToolbar(mToolbar);

        new SectionInfoAdapter(getParentActivity(), rootView, request, courseList).init();
    }

    public void setToolbar(Toolbar toolbar) {
        super.setToolbar(toolbar);
        ActionBar actionBar = getParentActivity().getSupportActionBar();
        if(actionBar != null) {
            getParentActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getParentActivity().getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
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