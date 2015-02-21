package com.tevinjeffrey.rutgersct.ui;

import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.splunk.mint.Mint;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.adapters.CourseAdapter;
import com.tevinjeffrey.rutgersct.animator.EaseOutQuint;
import com.tevinjeffrey.rutgersct.model.Course;
import com.tevinjeffrey.rutgersct.model.Request;
import com.tevinjeffrey.rutgersct.model.Subject;
import com.tevinjeffrey.rutgersct.utils.CourseUtils;
import com.tevinjeffrey.rutgersct.utils.UrlUtils;

import org.apache.commons.lang3.text.WordUtils;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeoutException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CourseFragment extends MainFragment {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.courses)
    ListView mCoursesListView;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private Request request;
    private ArrayList<Course> courses;

    public CourseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            request = savedInstanceState.getParcelable(MainActivity.REQUEST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getParentActivity().setPrimaryWindow();
        setRetainInstance(true);

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, rootView);

        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green);

        request = getArguments().getParcelable(MainActivity.REQUEST);
        setToolbar(rootView);

        getCourses(mCoursesListView);
        setRefreshListener();
        refresh();

        mCoursesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createFragment(createArgs(request, courses.get(position)));
            }
        });
        return rootView;
    }

    private void getCourses(final ListView listView) {
        String url = UrlUtils.getCourseUrl(UrlUtils.buildParamUrl(request));
        Ion.with(this)
                .load(url)
                .as(new TypeToken<List<Course>>() {
                })
                .setCallback(new FutureCallback<List<Course>>() {
                    @Override
                    public void onCompleted(Exception e, List<Course> courseList) {
                        if (e == null && courseList.size() > 0) {
                            courses = (ArrayList<Course>) courseList;
                            final CourseAdapter subjectAdapter = new CourseAdapter(getActivity(), courseList);
                            listView.setAdapter(subjectAdapter);
                        } else {
                            if (e instanceof UnknownHostException) {
                                showSnackBar(getResources().getString(R.string.no_internet));
                            } else if (e instanceof IllegalStateException && !(e instanceof CancellationException)){
                                cancelRequests();
                                showSnackBar(getResources().getString(R.string.server_down));
                            } else if (e instanceof TimeoutException) {
                                cancelRequests();
                                showSnackBar(getResources().getString(R.string.timed_out));
                            } else {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("Request", request.toString());
                                map.put("Error", (e != null ? e.getMessage() : "An error occurred"));
                                Mint.logExceptionMap(map, e);
                            }
                        }
                        dismissProgress();
                    }
                });
    }

    void showSnackBar(String message) {
        SnackbarManager.show(
                Snackbar.with(getParentActivity())
                        .type(SnackbarType.MULTI_LINE)
                        .text(message)
                        .actionLabel("DISMISS")// text to display
                        .actionColor(getResources().getColor(R.color.white))
                        .color(getResources().getColor(R.color.accent))// action button label color
                        .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                , getParentActivity()); // activity where it is displayed
    }

    private void refresh() {
        if (mSwipeRefreshLayout != null && !mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        getCourses(mCoursesListView);
                    }

                }
            });
        }
    }

    private void setRefreshListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AnimatorSet set = new AnimatorSet();
                set.playTogether(
                        ObjectAnimator.ofFloat(mCoursesListView, "translationY", 50),
                        ObjectAnimator.ofFloat(mCoursesListView, "alpha", 1, 0)
                );
                set.setInterpolator(new EaseOutQuint());
                set.setDuration(500).start();

                getCourses(mCoursesListView);

            }
        });
    }

    private void dismissProgress() {
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(mCoursesListView, "translationY", 50, 0),
                    ObjectAnimator.ofFloat(mCoursesListView, "alpha", 0, 1)

            );
            set.setInterpolator(new EaseOutQuint());
            set.setDuration(500).start();
        }
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

        setToolbarTitle(toolbar);
    }

    private void setToolbarTitle(Toolbar toolbar) {
        ArrayList<Subject> al = getArguments().getParcelableArrayList(MainActivity.SUBJECTS_LIST);
        for (Subject s : al) {
            if (CourseUtils.formatNumber(s.getCode()).equals(request.getSubject())) {
                toolbar.setTitle(WordUtils.capitalize(s.getDescription().toLowerCase()));
            }
        }
    }

    private void createFragment(Bundle b) {
        CourseInfoFragment courseInfoFragment = new CourseInfoFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            courseInfoFragment.setEnterTransition(new Fade(Fade.IN).excludeTarget(ImageView.class, true));
            courseInfoFragment.setExitTransition(new Fade(Fade.OUT).excludeTarget(ImageView.class, true));
            courseInfoFragment.setReenterTransition(new AutoTransition().excludeTarget(ImageView.class, true));
            courseInfoFragment.setReturnTransition(new Fade(Fade.IN).excludeTarget(ImageView.class, true));
            courseInfoFragment.setAllowReturnTransitionOverlap(false);
            courseInfoFragment.setAllowEnterTransitionOverlap(false);
            courseInfoFragment.setSharedElementEnterTransition(new ChangeBounds().setInterpolator(new EaseOutQuint()));
            courseInfoFragment.setSharedElementReturnTransition(new ChangeBounds().setInterpolator(new EaseOutQuint()));
            ft.addSharedElement(mToolbar, "toolbar_background");
        }
        courseInfoFragment.setArguments(b);
                ft.replace(R.id.container, courseInfoFragment).addToBackStack(this.toString())
                .commit();
    }

    private Bundle createArgs(Parcelable parcelable, Course selectedCourse) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(MainActivity.REQUEST, parcelable);
        bundle.putParcelableArrayList(MainActivity.SUBJECTS_LIST, getArguments().getParcelableArrayList(MainActivity.SUBJECTS_LIST));
        bundle.putParcelableArrayList(MainActivity.COURSE_LIST, courses);
        bundle.putParcelable(MainActivity.SELECTED_COURSE, selectedCourse);
        return bundle;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MainActivity.REQUEST, request);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}