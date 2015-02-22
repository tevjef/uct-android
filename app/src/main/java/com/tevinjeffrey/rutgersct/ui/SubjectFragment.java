package com.tevinjeffrey.rutgersct.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.adapters.SubjectAdapter;
import com.tevinjeffrey.rutgersct.animator.EaseOutQuint;
import com.tevinjeffrey.rutgersct.model.Request;
import com.tevinjeffrey.rutgersct.model.Subject;
import com.tevinjeffrey.rutgersct.utils.CourseUtils;
import com.tevinjeffrey.rutgersct.utils.UrlUtils;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeoutException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class SubjectFragment extends MainFragment {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.courses)
    ListView mCourses;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private Request request;
    private ArrayList<Subject> subjects;

    public SubjectFragment() {
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
        setToolbar();

        getSubjects(mCourses);

        setRefreshListener();
        refresh();

        mCourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setSubjectInRequest(parent, position);
                createFragment(createArgs(request));
            }
        });
        return rootView;
    }

    private void getSubjects(final ListView listView) {
        String url = UrlUtils.getSubjectUrl(UrlUtils.buildParamUrl(request));
        Ion.with(this)
                .load(url)
                .as(new TypeToken<List<Subject>>() {
                })
                .setCallback(new FutureCallback<List<Subject>>() {
                    @Override
                    public void onCompleted(Exception e, List<Subject> subjectList) {
                        if (e == null && subjectList.size() > 0) {

                            subjects = (ArrayList<Subject>) subjectList;
                            final SubjectAdapter subjectAdapter = new SubjectAdapter(getActivity(),
                                    subjectList);
                            listView.setAdapter(subjectAdapter);
                        } else {
                            if (e instanceof UnknownHostException) {
                                showSnackBar(getResources().getString(R.string.no_internet));
                            } else if (e instanceof IllegalStateException && !(e instanceof CancellationException)) {
                                cancelRequests();
                                showSnackBar(getResources().getString(R.string.server_down));
                            } else if (e instanceof TimeoutException) {
                                cancelRequests();
                                showSnackBar(getResources().getString(R.string.timed_out));
                            } else if (!(e instanceof CancellationException)) {
                                Timber.e(e, "Crash while attempting to complete request in %s to %s"
                                        , SubjectFragment.this.toString(), request.toString());
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
                        getSubjects(mCourses);
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
                        ObjectAnimator.ofFloat(mCourses, "translationY", 50),
                        ObjectAnimator.ofFloat(mCourses, "alpha", 1, 0)
                );
                set.setInterpolator(new EaseOutQuint());
                set.setDuration(500).start();

                getSubjects(mCourses);
            }
        });
    }

    private void dismissProgress() {
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(mCourses, "translationY", 50, 0),
                    ObjectAnimator.ofFloat(mCourses, "alpha", 0, 1)

            );
            set.setInterpolator(new EaseOutQuint());
            set.setDuration(500).start();
        }
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

        setToolbarTitle(mToolbar);
    }

    private void setToolbarTitle(Toolbar toolbar) {
        toolbar.setTitle(request.getSemester());

        ArrayList<String> al = new ArrayList<>();
        for (String s : request.getLocations()) {
            al.add(UrlUtils.getAbbreviatedLocationName(s));
        }
        toolbar.setSubtitle(Request.toStringList(al) + " - "
                + Request.toStringList(request.getLevels()));
    }

    private void setSubjectInRequest(AdapterView<?> parent, int position) {
        request.setSubject(CourseUtils.formatNumber(((Subject) parent.getAdapter()
                .getItem(position))
                .getCode()));
    }

    private void createFragment(Bundle b) {
        CourseFragment courseFragment = new CourseFragment();
        courseFragment.setArguments(b);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, courseFragment).addToBackStack(this.toString())
                .commit();
    }

    private Bundle createArgs(Parcelable parcelable) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(MainActivity.REQUEST, parcelable);
        bundle.putParcelableArrayList(MainActivity.SUBJECTS_LIST, subjects);
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