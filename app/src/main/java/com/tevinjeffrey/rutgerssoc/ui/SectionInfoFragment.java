package com.tevinjeffrey.rutgerssoc.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.adapters.SectionInfoAdapter;
import com.tevinjeffrey.rutgerssoc.model.Request;


public class SectionInfoFragment extends Fragment {

    private Request request;

    public SectionInfoFragment() {

    }

    private MainActivity getParentActivity() {
        return (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getParentActivity().setPrimaryWindow();

        setRetainInstance(true);

        final View rootView = inflater.inflate(R.layout.section_info, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar_header_info);
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

        Bundle bundle = getArguments();
        request = bundle.getParcelable("request");
        inflateViews(rootView);


        return rootView;
    }

    private void inflateViews(View rootView) {
        new SectionInfoAdapter(getParentActivity(), request, rootView, getParentActivity().getCourses()).init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}