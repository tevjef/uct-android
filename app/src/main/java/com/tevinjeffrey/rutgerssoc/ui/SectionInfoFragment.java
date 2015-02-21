package com.tevinjeffrey.rutgerssoc.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.adapters.SectionInfoAdapter;
import com.tevinjeffrey.rutgerssoc.animator.EaseOutQuint;
import com.tevinjeffrey.rutgerssoc.model.Course;
import com.tevinjeffrey.rutgerssoc.model.Request;

import java.util.List;


public class SectionInfoFragment extends MainFragment {

    private Request request;

    public SectionInfoFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setEnterTransition(new AutoTransition().excludeTarget(ImageView.class, true));
            setExitTransition(new Fade(Fade.OUT).excludeTarget(ImageView.class, true));
            setReenterTransition(new AutoTransition().excludeTarget(ImageView.class, true));
            setReturnTransition(new Fade(Fade.IN).excludeTarget(ImageView.class, true));
            setAllowReturnTransitionOverlap(true);
            setAllowEnterTransitionOverlap(true);
            setSharedElementEnterTransition(new ChangeBounds().setInterpolator(new EaseOutQuint()));
            setSharedElementReturnTransition(new ChangeBounds().setInterpolator(new EaseOutQuint()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getParentActivity().setPrimaryWindow();
        setRetainInstance(true);

        final View rootView = inflater.inflate(R.layout.section_info, container, false);

        setToolbar(rootView);

        Bundle bundle = getArguments();
        request = bundle.getParcelable(MainActivity.REQUEST);
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

        List<Course> c = getArguments().getParcelableArrayList(MainActivity.COURSE_LIST);
        new SectionInfoAdapter(getParentActivity(), request, rootView, c).init();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_info, menu);
    }
}