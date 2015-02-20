package com.tevinjeffrey.rutgerssoc.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.EventListener;
import com.splunk.mint.Mint;
import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.adapters.SubjectAdapter;
import com.tevinjeffrey.rutgerssoc.animator.EaseOutQuint;
import com.tevinjeffrey.rutgerssoc.model.Request;
import com.tevinjeffrey.rutgerssoc.model.Subject;
import com.tevinjeffrey.rutgerssoc.utils.CourseUtils;
import com.tevinjeffrey.rutgerssoc.utils.UrlUtils;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeoutException;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

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
        setToolbar(rootView);

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
        Log.d("URL", url);
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
                                showSnackBar("No internet connection.");
                            } else if (e instanceof CancellationException) {
                                Mint.transactionCancel("NetworkOp", "Cancelled");
                            } else if (e instanceof IllegalStateException){
                                Ion.getDefault(getParentActivity().getApplicationContext()).cancelAll();
                                showSnackBar("The server is currently down. Try again later.");
                            } else if (e instanceof TimeoutException) {
                                Ion.getDefault(getParentActivity().getApplicationContext()).cancelAll();
                                showSnackBar("Connection timed out. Check internet connection.");
                            }else {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("Request", request.toString());
                                map.put("Error", (e != null ? e.getMessage() : "An error occurred"));
                                Mint.logExceptionMap(map, e);
                                if(e != null) Toast.makeText(getParentActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Ion.getDefault(getParentActivity().getApplicationContext()).cancelAll(this);
        ButterKnife.reset(this);
    }
}