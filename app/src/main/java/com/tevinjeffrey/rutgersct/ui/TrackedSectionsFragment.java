package com.tevinjeffrey.rutgersct.ui;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.EventListener;
import com.splunk.mint.Mint;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.adapters.SectionListAdapter;
import com.tevinjeffrey.rutgersct.animator.EaseOutQuint;
import com.tevinjeffrey.rutgersct.model.Course;
import com.tevinjeffrey.rutgersct.model.Request;
import com.tevinjeffrey.rutgersct.model.TrackedSections;
import com.tevinjeffrey.rutgersct.utils.UrlUtils;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeoutException;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

public class TrackedSectionsFragment extends MainFragment {

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.scrollView)
    ObservableScrollView mScrollView;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.fab)
    FloatingActionButton mFab;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.sectionsContainer)
    LinearLayout mSectionsContainer;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.addCoursesTrack)
    RelativeLayout addCoursesToTrack;

    View rootView;

    String TAG = this.toString();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getParentActivity().setPrimaryWindow();
        setRetainInstance(true);

        rootView = inflater.inflate(R.layout.fragment_tracked_section, container, false);

        ButterKnife.inject(this, rootView);
        setToolbar();

        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green);

/*       progress = ProgressDialog.show(getParentActivity(), "",
                "Checking classes", true);*/

        setRefreshListener();
        refresh();
        setFabListener();

        warnServerIssues();
        return rootView;
    }

    private void warnServerIssues() {
        Calendar c= Calendar.getInstance();
            Log.i(TAG, String.valueOf(c.get(Calendar.HOUR_OF_DAY)));

        if(c.get(Calendar.HOUR_OF_DAY) == 3 || c.get(Calendar.HOUR_OF_DAY) == 4) {
            showSnackBar("Expect intermittent server issues between the hours of 3:00am and 4:00am");
        }
    }

    private void setFabListener() {
        mFab.attachToScrollView(mScrollView);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragment();
            }
        });
    }

    private void createFragment() {
        ChooserFragment chooserFragment = new ChooserFragment();

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ft.addSharedElement(mToolbar, mToolbar.getTransitionName());
            ft.addSharedElement(mFab, "snackbar");
        }
                ft.replace(R.id.container, chooserFragment).addToBackStack(this.toString())
                .commit();
    }

    private void setRefreshListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                AnimatorSet set = new AnimatorSet();
                set.playTogether(
                        ObjectAnimator.ofFloat(mSectionsContainer, "translationY", 50),
                        ObjectAnimator.ofFloat(mSectionsContainer, "alpha", 1, 0)

                );
                set.setInterpolator(new EaseOutQuint());
                set.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (TrackedSectionsFragment.this.isAdded()) {
                            getTrackedSections();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                set.setDuration(500).start();
            }
        });
    }

    private void refresh() {
        if (mSwipeRefreshLayout != null && !mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        getTrackedSections();
                    }
                }
            });
        } else if(mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
            dismissProgress();
            Ion.getDefault(getParentActivity().getApplicationContext()).cancelAll(this);
            //refresh();
        }
    }

    private void getTrackedSections() {
        removeAllViews();
        final List<TrackedSections> allTrackedSections = TrackedSections.listAll(TrackedSections.class);
        for (final Iterator<TrackedSections> trackedSectionsIterator = allTrackedSections.iterator(); trackedSectionsIterator.hasNext(); ) {
            TrackedSections ts = trackedSectionsIterator.next();
            final Request r = new Request(ts.getSubject(), ts.getSemester(), ts.getLocations(), ts.getLevels(), ts.getIndexNumber());
            String url = UrlUtils.getCourseUrl(UrlUtils.buildParamUrl(r));
            Ion.with(this)
                    .load(url)
                    .as(new TypeToken<List<Course>>() {
                    })
                    .setCallback(new FutureCallback<List<Course>>() {

                        final boolean isLastSection = !trackedSectionsIterator.hasNext();

                        @Override
                        public void onCompleted(Exception e, List<Course> courses) {
                            if (e == null && courses.size() > 0) {
                                for (final Course c : courses) {
                                    for (final Course.Sections s : c.getSections()) {
                                        if (s.getIndex().equals(r.getIndex())) {
                                            List<Course.Sections> currentSection = new ArrayList<>();
                                            currentSection.add(s);
                                            c.setSections(currentSection);

                                            new SectionListAdapter(TrackedSectionsFragment.this, c, rootView, r, MainActivity.TRACKED_SECTION).init();

                                            if (isLastSection) {
                                                dismissProgress();
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (e instanceof UnknownHostException) {
                                    showSnackBar("No internet connection.");
                                } else if (e instanceof CancellationException) {
                                    //
                                } else if (e instanceof IllegalStateException){
                                    Ion.getDefault(getParentActivity().getApplicationContext()).cancelAll();
                                    showSnackBar("The server is currently down. Try again later.");
                                } else if (e instanceof TimeoutException) {
                                    Ion.getDefault(getParentActivity().getApplicationContext()).cancelAll();
                                    showSnackBar("Connection timed out. Check internet connection.");
                                }else {
                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("Request", r.toString());
                                    map.put("Error", (e != null ? e.getMessage() : "An error occurred"));
                                    Mint.logExceptionMap(map, e);
                                    if(e != null) Toast.makeText(getParentActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                            dismissProgress();
                            setEmptyLayout(allTrackedSections);
                        }
                    });
        }
        setEmptyLayout(allTrackedSections);
    }

    private void setEmptyLayout(List<TrackedSections> allTrackedSections) {
        if (allTrackedSections.size() == 0 || ((ViewGroup)ButterKnife.findById(rootView, R.id.sectionsContainer)).getChildCount() == 0) {
            dismissProgress();
            addCoursesToTrack.setVisibility(View.VISIBLE);
        } else {
            addCoursesToTrack.setVisibility(View.GONE);
            SnackbarManager.dismiss();
        }
    }

    private void removeAllViews() {
        if (mSectionsContainer != null) {
            mSectionsContainer.removeAllViews();
            mSectionsContainer.setAlpha(0);

        }
    }

    private void dismissProgress() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);

            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(mSectionsContainer, "translationY", 50, 0),
                    ObjectAnimator.ofFloat(mSectionsContainer, "alpha", 0, 1)

            );
            set.setInterpolator(new EaseOutQuint());
            set.setDuration(500).start();
        }
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
                        .eventListener(new EventListener() {
                            @Override
                            public void onShow(Snackbar snackbar) {
                                animate(mFab).translationYBy(-snackbar.getHeight()).setInterpolator(new OvershootInterpolator()).start();
                            }

                            @Override
                            public void onShowByReplace(Snackbar snackbar) {
                                Log.i(TAG, String.format("Snackbar will show by replace. Width: %d Height: %d Offset: %d",
                                        snackbar.getWidth(), snackbar.getHeight(),
                                        snackbar.getOffset()));
                            }

                            @Override
                            public void onShown(Snackbar snackbar) {
                                Log.i(TAG, String.format("Snackbar shown. Width: %d Height: %d Offset: %d",
                                        snackbar.getWidth(), snackbar.getHeight(),
                                        snackbar.getOffset()));
                            }

                            @Override
                            public void onDismiss(Snackbar snackbar) {
                                animate(mFab).translationYBy(snackbar.getHeight()).setInterpolator(new OvershootInterpolator()).start();
                            }

                            @Override
                            public void onDismissByReplace(Snackbar snackbar) {
                                Log.i(TAG, String.format(
                                        "Snackbar will dismiss by replace. Width: %d Height: %d Offset: %d",
                                        snackbar.getWidth(), snackbar.getHeight(),
                                        snackbar.getOffset()));
                            }

                            @Override
                            public void onDismissed(Snackbar snackbar) {
                                Log.i(TAG, String.format("Snackbar dismissed. Width: %d Height: %d Offset: %d",
                                        snackbar.getWidth(), snackbar.getHeight(),
                                        snackbar.getOffset()));
                            }
                        }) // Snackbar's EventListener
                , getParentActivity()); // activity where it is displayed
    }

    private void setToolbar() {
        setToolbarTitle(mToolbar);
        getParentActivity().setSupportActionBar(mToolbar);

    }

    private void setToolbarTitle(Toolbar toolbar) {
        toolbar.setTitle("Tracked Sections");
    }

    void launchWebReg() {
        String url = "http://webreg.rutgers.edu";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_tracked_sections, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refresh();
                return true;
            case R.id.action_webreg:
                launchWebReg();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SnackbarManager.dismiss();
        Ion.getDefault(getParentActivity().getApplicationContext()).cancelAll();
        ButterKnife.reset(this);
    }
}
