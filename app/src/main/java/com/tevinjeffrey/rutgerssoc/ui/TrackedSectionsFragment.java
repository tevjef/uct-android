package com.tevinjeffrey.rutgerssoc.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.model.Request;
import com.tevinjeffrey.rutgerssoc.model.TrackedSections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TrackedSectionsFragment extends Fragment {
    @InjectView(R.id.prof_text)
    TextView mProfText;
    @InjectView(R.id.sectionNumber)
    TextView mSectionNumber;
    @InjectView(R.id.sectionNumberBackground)
    RelativeLayout mSectionNumberBackground;
    @InjectView(R.id.sectionTimeContainer)
    LinearLayout mSectionTimeContainer;
    @InjectView(R.id.sectionTitle_text)
    TextView mSectionTitleText;
    @InjectView(R.id.section_details)
    RelativeLayout mSectionDetails;
    @InjectView(R.id.sectionRoot)
    RelativeLayout mSectionRoot;

    private MainActivity getParentActivity() {
        return (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getParentActivity().setPrimaryWindow();
        setRetainInstance(true);

        final View rootView = inflater.inflate(R.layout.full_section_layout, container, false);
        ButterKnife.inject(this, rootView);

        List<Request> requests = new ArrayList<>();

        for(Iterator<TrackedSections> allTrackedSections =
                    TrackedSections.findAll(TrackedSections.class); allTrackedSections.hasNext();) {
            TrackedSections ts = allTrackedSections.next();
            requests.add(new Request(ts.getSubject(), ts.getSemester(), ts.getLocations(), ts.getLevels(), ts.getIndexNumber()));
        }


        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
