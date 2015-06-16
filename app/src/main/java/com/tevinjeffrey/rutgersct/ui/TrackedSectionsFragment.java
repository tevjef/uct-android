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

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.EventListener;
import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.adapters.SectionListAdapter;
import com.tevinjeffrey.rutgersct.animator.EaseOutQuint;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.database.Updater;
import com.tevinjeffrey.rutgersct.model.Course;
import com.tevinjeffrey.rutgersct.model.Request;
import com.tevinjeffrey.rutgersct.model.TrackedSection;
import com.tevinjeffrey.rutgersct.utils.SemesterUtils;
import com.tevinjeffrey.rutgersct.utils.UrlUtils;

import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeoutException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

public class TrackedSectionsFragment extends BaseFragment {

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

        DatabaseHandler.setDatabaseListener(new MyDatabaseListener());

        setToolbar();
        setupSwipeLayout();
        refresh();
        setFabListener();

        warnServerIssues();
        return rootView;
    }

    private void setupSwipeLayout() {
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green);

        setRefreshListener();
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

        Observable<Course> courseObservable = Updater.getTrackedSections();
        courseObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Course>() {
                    @Override
                    public void onCompleted() {
                        dismissProgress();
                        setEmptyLayout();
                    }

                    @Override
                    public void onError(Throwable t) {
                        dismissProgress();
                        showSnackBar(t.getClass().getSimpleName());
                        if (t instanceof UnknownHostException) {
                            showSnackBar(getResources().getString(R.string.no_internet));
                        } else if (t instanceof IllegalStateException && !(t instanceof CancellationException)) {
                            cancelRequests();
                            showSnackBar(getResources().getString(R.string.server_down));
                        } else if (t instanceof TimeoutException) {
                            cancelRequests();
                            showSnackBar(getResources().getString(R.string.timed_out));
                        } else if (!(t instanceof CancellationException)) {
                            Timber.e(t, "Crash while attempting to complete request in %s to %s"
                                    , TrackedSectionsFragment.this.toString(), t.toString());
                        }
                    }

                    @Override
                    public void onNext(Course course) {
                        String indexNumber = course.getSections().get(0).getIndex();
                        TrackedSection ts = TrackedSection.find(TrackedSection.class, "index_number = ?", indexNumber).get(0);

                        Request request = UrlUtils.getRequestFromTrackedSections(ts);

                        new SectionListAdapter(TrackedSectionsFragment.this,
                                course,
                                rootView,
                                request,
                                RutgersCTApp.TRACKED_SECTION).init();
                    }
                });
    }

    private void setEmptyLayout() {
        if (TrackedSection.count(TrackedSection.class, null, null) == 0) {
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
            case R.id.action_rate:
                lauchMarket();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void lauchMarket() {
        final Uri uri = Uri.parse("market://details?id=" + getParentActivity().getApplicationContext().getPackageName());
        final Intent rateAppIntent = new Intent(Intent.ACTION_VIEW, uri);

        if (getParentActivity().getPackageManager().queryIntentActivities(rateAppIntent, 0).size() > 0)
        {
            startActivity(rateAppIntent);
        }
    }


    private class MyDatabaseListener implements DatabaseHandler.DatabaseListener {
        @Override
        public void onAdd(Request addedSection) {
            refresh();
        }

        @Override
        public void onRemove(Request removedSection) {
            refresh();
        }
    }
}
