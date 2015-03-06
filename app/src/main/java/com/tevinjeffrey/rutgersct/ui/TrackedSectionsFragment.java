package com.tevinjeffrey.rutgersct.ui;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.crashlytics.android.Crashlytics;
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
import com.tevinjeffrey.rutgersct.MyApplication;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.adapters.SectionListAdapter;
import com.tevinjeffrey.rutgersct.animator.EaseOutQuint;
import com.tevinjeffrey.rutgersct.model.Course;
import com.tevinjeffrey.rutgersct.model.Request;
import com.tevinjeffrey.rutgersct.model.TrackedSections;
import com.tevinjeffrey.rutgersct.utils.SemesterUtils;
import com.tevinjeffrey.rutgersct.utils.UrlUtils;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

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

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.setPrimaryWindow(getParentActivity());
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
        Calendar c = Calendar.getInstance();
        if (c.get(Calendar.HOUR_OF_DAY) == 3) {
            showSnackBar(getResources().getString(R.string.expect_server_issues));
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            chooserFragment.setEnterTransition(new Fade(Fade.IN).excludeTarget(ImageView.class, true));
            chooserFragment.setExitTransition(new Fade(Fade.OUT).excludeTarget(ImageView.class, true));
            chooserFragment.setReenterTransition(new Fade(Fade.IN).excludeTarget(ImageView.class, true));
            chooserFragment.setReturnTransition(new Fade(Fade.OUT).excludeTarget(ImageView.class, true));
            chooserFragment.setAllowReturnTransitionOverlap(true);
            chooserFragment.setAllowEnterTransitionOverlap(true);
            chooserFragment.setSharedElementEnterTransition(new ChangeBounds().setInterpolator(new EaseOutQuint()));
            chooserFragment.setSharedElementReturnTransition(new ChangeBounds().setInterpolator(new EaseOutQuint()));
            ft.addSharedElement(mToolbar, mToolbar.getTransitionName());
            ft.addSharedElement(mFab, "snackbar");
        } else {
            ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
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
        } else if (mSwipeRefreshLayout != null) {
            dismissProgress();
            cancelRequests();
        }
    }

    private void getTrackedSections() {
        removeAllViews();
        final List<TrackedSections> allTrackedSections = TrackedSections.listAll(TrackedSections.class);
        Mint.addExtraData(MyApplication.ITEMS_IN_DATABASE, String.valueOf(allTrackedSections.size()));
        Crashlytics.setInt(MyApplication.ITEMS_IN_DATABASE, allTrackedSections.size());
        Timber.d("getting %s items from dataase", allTrackedSections.size());

        final List<Course> updatedTrackedSections = new ArrayList<>();
        final AtomicInteger numOfRequestedCourses = new AtomicInteger();
        for (TrackedSections ts : allTrackedSections) {
            final Request r = new Request(ts.getSubject(), new SemesterUtils.Semester(ts.getSemester()), ts.getLocations(), ts.getLevels(), ts.getIndexNumber());
            getCourses(allTrackedSections, updatedTrackedSections, numOfRequestedCourses, r);
        }
        setEmptyLayout(allTrackedSections);
    }

    private void getCourses(final List<TrackedSections> allTrackedSections, final List<Course> updatedTrackedSections, final AtomicInteger numOfRequestedCourses, final Request r) {
        String url = UrlUtils.getCourseUrl(UrlUtils.buildParamUrl(r));
        Ion.with(this)
                .load(url)
                .as(new TypeToken<List<Course>>() {
                })
                .setCallback(new ListFutureCallback(numOfRequestedCourses, r, updatedTrackedSections, allTrackedSections));
    }


    private void setEmptyLayout(Collection<TrackedSections> allTrackedSections) {
        if (allTrackedSections.size() == 0) {
            dismissProgress();
            addCoursesToTrack.setVisibility(View.VISIBLE);
        } else {
            addCoursesToTrack.setVisibility(View.GONE);
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

    void showSnackBar(CharSequence message) {
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
                            }

                            @Override
                            public void onShown(Snackbar snackbar) {
                            }

                            @Override
                            public void onDismiss(Snackbar snackbar) {
                                animate(mFab).translationYBy(snackbar.getHeight()).setInterpolator(new OvershootInterpolator()).start();
                            }

                            @Override
                            public void onDismissByReplace(Snackbar snackbar) {
                            }

                            @Override
                            public void onDismissed(Snackbar snackbar) {
                            }
                        }) // Snackbar's EventListener
                , getParentActivity()); // activity where it is displayed
    }

    private void setToolbar() {
        setToolbarTitle(mToolbar);
        mToolbar.setTitleTextAppearance(getParentActivity(), R.style.toolbar_title);
        mToolbar.setSubtitleTextAppearance(getParentActivity(), R.style.toolbar_subtitle);
        getParentActivity().setSupportActionBar(mToolbar);
        getParentActivity().getSupportActionBar().setIcon(R.drawable.ic_track_changes_white);
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

    private class ListFutureCallback implements FutureCallback<List<Course>> {
        private final AtomicInteger numOfRequestedCourses;
        private final Request r;
        private final List<Course> updatedTrackedSections;
        private final List<TrackedSections> allTrackedSections;

        public ListFutureCallback(AtomicInteger numOfRequestedCourses, Request r, List<Course> updatedTrackedSections, List<TrackedSections> allTrackedSections) {
            this.numOfRequestedCourses = numOfRequestedCourses;
            this.r = r;
            this.updatedTrackedSections = updatedTrackedSections;
            this.allTrackedSections = allTrackedSections;
        }

        @Override
        public void onCompleted(Exception e, List<Course> courses) {
            numOfRequestedCourses.incrementAndGet();

            if (e == null && courses.size() > 0) {
                for (final Course c : courses) {
                    for (final Course.Sections s : c.getSections()) {
                        if (s.getIndex().equals(r.getIndex())) {
                            List<Course.Sections> currentSection = new ArrayList<>();
                            currentSection.add(s);
                            c.setSections(currentSection);
                            updatedTrackedSections.add(c);

                            if (allTrackedSections.size() == numOfRequestedCourses.get()) {
                                dismissProgress();
                                insertCoursesInRootView(updatedTrackedSections, r);
                            }
                        }
                    }
                }
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
                            , TrackedSectionsFragment.this.toString(), r.toString());
                }
            }
            dismissProgress();
            setEmptyLayout(allTrackedSections);
        }

        private void insertCoursesInRootView(List<Course> updatedTrackedSections, Request r) {
            Collections.sort(updatedTrackedSections);
            for(Course c : updatedTrackedSections)
                new SectionListAdapter(TrackedSectionsFragment.this, c, rootView, r, MainActivity.TRACKED_SECTION).init();
        }

        @Override
        public String toString() {
            return "ListFutureCallback{" +
                    "numOfRequestedCourses=" + numOfRequestedCourses +
                    ", r=" + r +
                    ", updatedTrackedSections=" + updatedTrackedSections +
                    ", allTrackedSections=" + allTrackedSections +
                    '}';
        }
    }

}
