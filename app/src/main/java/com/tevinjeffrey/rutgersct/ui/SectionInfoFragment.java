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

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SectionInfoFragment extends MainFragment {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

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
        ButterKnife.inject(this, rootView);

        setToolbar();
        if (getFragmentManager().getBackStackEntryCount() > 2) {
            ButterKnife.findById(rootView, R.id.fab).setAlpha(0);
            ButterKnife.findById(rootView, R.id.fab).setVisibility(View.GONE);
        }

        inflateViews(rootView);
        return rootView;
    }

    private void setToolbar() {
        mToolbar.setTitleTextAppearance(getParentActivity(), R.style.toolbar_title);
        mToolbar.setSubtitleTextAppearance(getParentActivity(), R.style.toolbar_subtitle);
        getParentActivity().setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}