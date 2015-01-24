package com.tevinjeffrey.rutgerssoc.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.Request;
import com.tevinjeffrey.rutgerssoc.Trackable;
import com.tevinjeffrey.rutgerssoc.adapters.CourseInfoAdapter;
import com.tevinjeffrey.rutgerssoc.adapters.SectionInfoAdapter;
import com.tevinjeffrey.rutgerssoc.model.Course;

/**
 * Created by Tevin on 1/14/2015.
 */
public class SectionInfoFragment extends Fragment {

    private Trackable trackable;

    public SectionInfoFragment() {

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
        getParentActivity().setPrimaryWindow();

        final View rootView = inflater.inflate(R.layout.section_info, container, false);

        Bundle bundle = getArguments();

        trackable = bundle.getParcelable("request");
        inflateViews(rootView);

        return rootView;
    }
    private void inflateViews(View rootView) {
        new SectionInfoAdapter(getParentActivity(), trackable, rootView, getParentActivity().getCourses()).init();
    }
}