package com.tevinjeffrey.rutgerssoc.ui;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.splunk.mint.Mint;
import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.adapters.SectionListAdapter;
import com.tevinjeffrey.rutgerssoc.animator.EaseOutQuint;
import com.tevinjeffrey.rutgerssoc.model.Course;
import com.tevinjeffrey.rutgerssoc.model.Request;
import com.tevinjeffrey.rutgerssoc.model.TrackedSections;
import com.tevinjeffrey.rutgerssoc.utils.CourseUtils;
import com.tevinjeffrey.rutgerssoc.utils.UrlUtils;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CancellationException;

import butterknife.ButterKnife;
import butterknife.InjectView;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setEnterTransition(new AutoTransition().excludeTarget(ImageView.class, true));
            setExitTransition(new Fade(Fade.OUT).excludeTarget(ImageView.class, true));
            setReenterTransition(new AutoTransition().excludeTarget(ImageView.class, true));
            setReturnTransition(new Fade(Fade.IN).excludeTarget(ImageView.class, true));
            setAllowReturnTransitionOverlap(false);
            setAllowEnterTransitionOverlap(false);
            setSharedElementEnterTransition(new ChangeBounds().setInterpolator(new EaseOutQuint()));
            setSharedElementReturnTransition(new ChangeBounds().setInterpolator(new EaseOutQuint()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getParentActivity().setPrimaryWindow();
        setRetainInstance(true);

        rootView = inflater.inflate(R.layout.fragment_tracked_section, container, false);

        ButterKnife.inject(this, rootView);
        setToolbar();

        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.accent);

/*       progress = ProgressDialog.show(getParentActivity(), "",
                "Checking classes", true);*/

        setRefreshListener();
        refresh();
        setFabListener();


        return rootView;
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
        @SuppressLint("CommitTransaction") FragmentTransaction ft = getFragmentManager().beginTransaction();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            chooserFragment.setEnterTransition(new Slide(Gravity.RIGHT).excludeTarget(ImageView.class, true));
            chooserFragment.setExitTransition(new Fade(Fade.OUT).excludeTarget(ImageView.class, true));
            chooserFragment.setReenterTransition(new Slide(Gravity.LEFT).excludeTarget(ImageView.class, true));
            chooserFragment.setReturnTransition(new Explode().excludeTarget(ImageView.class, true));
            setAllowReturnTransitionOverlap(false);
            setAllowEnterTransitionOverlap(false);
            chooserFragment.setSharedElementEnterTransition(new ChangeBounds().setInterpolator(new EaseOutQuint()));
            chooserFragment.setSharedElementReturnTransition(new ChangeBounds().setInterpolator(new EaseOutQuint()));


            ft.addSharedElement(mToolbar, "toolbar_background");
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
        Log.d("TAG", "getTrackedSections Called");
        removeAllViews();

        List<TrackedSections> allTrackedSections = TrackedSections.listAll(TrackedSections.class);
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
                                  /*          for(final Iterator<Course.Sections> sectionsInCourse = c.getSections().iterator();  sectionsInCourse.hasNext();) {
                                                if(!s.getIndex().equals(r.getIndex())) {
                                                    sectionsInCourse.remove();
                                                }
                                            }*/

                                            Log.d("TAG", "Adding section to layout | " + CourseUtils.getTitle(c));
                                            new SectionListAdapter(TrackedSectionsFragment.this, c, rootView, r, MainActivity.TRACKED_SECTION).init();

                                            if (isLastSection) {
                                                dismissProgress();
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (e instanceof UnknownHostException) {
                                    Toast.makeText(getParentActivity(), "No Internet connection", Toast.LENGTH_LONG).show();
                                } else if (e instanceof CancellationException) {
                                    Mint.transactionCancel("NetworkOp", "Cancelled");
                                } else {
                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("Request", r.toString());
                                    map.put("Error", (e != null ? e.getMessage() : "An error occurred"));
                                    Mint.logExceptionMap(map, e);
                                    Toast.makeText(getParentActivity(), "Error: " + (e != null ? e.getMessage() : null), Toast.LENGTH_LONG).show();
                                }
                            }
                            dismissProgress();
                        }
                    });
        }
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
//       if(progress.isShowing()) {
//            progress.dismiss();
//        }
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
        Ion.getDefault(getParentActivity().getApplicationContext()).cancelAll();
        ButterKnife.reset(this);
    }
}
