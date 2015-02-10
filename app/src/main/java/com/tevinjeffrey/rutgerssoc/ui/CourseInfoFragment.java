package com.tevinjeffrey.rutgerssoc.ui;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.adapters.CourseInfoAdapter;
import com.tevinjeffrey.rutgerssoc.model.Course;
import com.tevinjeffrey.rutgerssoc.model.Request;

import butterknife.ButterKnife;

public class CourseInfoFragment extends Fragment {

    private Request request;

    public CourseInfoFragment() {

    }

    private MainActivity getParentActivity() {
        return (MainActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            request = savedInstanceState.getParcelable(MainActivity.REQUEST);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setEnterTransition(new Fade(Fade.IN).excludeTarget(ImageView.class,true));
            setExitTransition(new Fade(Fade.OUT).excludeTarget(ImageView.class, true));
            setReenterTransition(new Fade(Fade.IN).excludeTarget(ImageView.class, true));
            setReturnTransition(new Fade(Fade.IN).excludeTarget(ImageView.class, true));
            setAllowReturnTransitionOverlap(false);
            setAllowEnterTransitionOverlap(true);
/*            setSharedElementEnterTransition(new ChangeBounds());
            setSharedElementReturnTransition(new ChangeBounds());*/
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getParentActivity().setAccentWindow();
        setRetainInstance(true);

        final View rootView = inflater.inflate(R.layout.course_info, container, false);

        setToolbar(rootView);

        Bundle bundle = getArguments();

        Course selectedCourse = bundle.getParcelable(MainActivity.SELECTED_COURSE);
        request = bundle.getParcelable(MainActivity.REQUEST);

        inflateViews(selectedCourse, rootView);

        return rootView;
    }

    private void setToolbar(View rootView) {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
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
        new CourseInfoAdapter(getParentActivity(), course, rootView, request).init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MainActivity.REQUEST, request);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_info, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_track:
                TrackedSectionsFragment trackedSectionsFragment = new TrackedSectionsFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, trackedSectionsFragment)
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}