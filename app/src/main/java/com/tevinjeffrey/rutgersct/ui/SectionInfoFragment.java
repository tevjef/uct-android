package com.tevinjeffrey.rutgersct.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.adapters.SectionInfoAdapter;
import com.tevinjeffrey.rutgersct.model.Course;
import com.tevinjeffrey.rutgersct.model.Request;

import java.util.ArrayList;


public class SectionInfoFragment extends MainFragment {

    private ArrayList<Course> c;
    private Request request;

    public SectionInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.setPrimaryWindow(getParentActivity());
        setRetainInstance(true);

        c = getArguments().getParcelableArrayList(MainActivity.COURSE_LIST);
        request = getArguments().getParcelable(MainActivity.REQUEST);
        final View rootView = inflater.inflate(R.layout.section_info, container, false);
        setToolbar(rootView);
        inflateViews(rootView);
        return rootView;
    }

    private void setToolbar(View rootView) {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        getParentActivity().setSupportActionBar(toolbar);
        //IMPORTANT-must be after setting the actionbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentActivity().onBackPressed();
            }
        });
        getParentActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getParentActivity().getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void inflateViews(View rootView) {
        new SectionInfoAdapter(getParentActivity(), request, rootView, c).init();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_info, menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MainActivity.REQUEST, getArguments().getParcelable(MainActivity.REQUEST));
        outState.putParcelableArrayList(MainActivity.COURSE_LIST, getArguments().getParcelableArrayList(MainActivity.COURSE_LIST));
    }
}