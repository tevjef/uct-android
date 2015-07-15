package com.tevinjeffrey.rutgersct.ui;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.MainThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.google.gson.JsonParseException;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.EventListener;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.adapters.TrackedSectionsFragmentAdapter;
import com.tevinjeffrey.rutgersct.animator.CircleSharedElementCallback;
import com.tevinjeffrey.rutgersct.animator.EaseOutQuint;
import com.tevinjeffrey.rutgersct.customviews.CircleView;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.database.TrackedSection;
import com.tevinjeffrey.rutgersct.rutgersapi.RutgersApi;
import com.tevinjeffrey.rutgersct.rutgersapi.RutgersApiImpl;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.rutgersapi.utils.UrlUtils;
import com.tevinjeffrey.rutgersct.utils.PreferenceUtils;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeoutException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import icepick.Icicle;
import rx.Observable;
import rx.Subscriber;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

public class TrackedSectionsFragment extends BaseFragment implements DatabaseHandler.DatabaseListener,
        SwipeRefreshLayout.OnRefreshListener, TrackedSectionsFragmentAdapter.ItemClickListener {

    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.add_courses_fab)
    FloatingActionButton mFab;

    @InjectView(R.id.tsf_list)
    RecyclerView mRecyclerView;

    @InjectView(R.id.add_courses_to_track)
    ViewGroup mEmptyView;

    @Icicle
    ArrayList<Course> mListDataset;

    @Icicle
    boolean hasDataFlag = false;

    Observable<Course> courseObservable;

    private RecyclerView.Adapter mListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.setPrimaryWindow(getParentActivity());

        setRetainInstance(true);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tracked_sections, container, false);

        ButterKnife.inject(this, rootView);

        initViews();

        refresh();

        return rootView;
    }

    public void initViews() {
        initRecyclerView();

        setToolbarTitle(mToolbar, getString(R.string.tracked_sections));
        setToolbar(mToolbar);

        setupSwipeLayout();
        setFabListener();
        warnServerIssues();
    }

    public void setToolbar(Toolbar toolbar) {
        super.setToolbar(toolbar);
        ActionBar actionBar = getParentActivity().getSupportActionBar();
        if (actionBar != null)
            getParentActivity().getSupportActionBar().setIcon(R.drawable.ic_track_changes_white);
    }

    private void shouldShowEmptyView() {
        if (mEmptyView != null) {
            if (isDatasetEmpty() || !hasDataFlag) {
                mEmptyView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            } else {
                mEmptyView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    private boolean isDatasetEmpty() {
        return mListDataset.size() == 0;
    }

    private void addToDataset(Collection<Course> data) {
        mListDataset.clear();
        mListDataset.addAll(data);
        mListAdapter.notifyDataSetChanged();
    }

    public void initRecyclerView() {
        if (mListDataset == null) {
            mListDataset = new ArrayList<>();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getParentActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.supportsPredictiveItemAnimations();
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.scrollToPosition(0);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mListAdapter = new TrackedSectionsFragmentAdapter(mListDataset, TrackedSectionsFragment.this);
        mRecyclerView.setAdapter(mListAdapter);

        shouldShowEmptyView();
    }

    private void setupSwipeLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green);
    }

    private void warnServerIssues() {
        Calendar c = Calendar.getInstance();
        if (c.get(Calendar.HOUR_OF_DAY) == 3 && !PreferenceUtils.getLearnedServerIssues()) {
            showSnackBar(getResources().getString(R.string.expect_server_issues));
        }
    }

    private void setFabListener() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChooserFragment();
            }
        });
    }

    private void createChooserFragment() {
        ChooserFragment chooserFragment = new ChooserFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setTransitionName(getString(R.string.transition_name_tool_background));
            ft.addSharedElement(mToolbar, getString(R.string.transition_name_tool_background));

            chooserFragment.setAllowEnterTransitionOverlap(false);
            chooserFragment.setAllowReturnTransitionOverlap(false);

            setExitTransition(new Fade(Fade.OUT).setDuration(50).excludeTarget(ImageView.class, true));


            chooserFragment.setEnterTransition(new Fade(Fade.IN).excludeTarget(ImageView.class, true));
            chooserFragment.setReturnTransition(new Fade(Fade.OUT).excludeTarget(ImageView.class, true).setDuration(50));


            chooserFragment.setSharedElementEnterTransition(new ChangeBounds().setInterpolator(new EaseOutQuint()));
            chooserFragment.setSharedElementReturnTransition(new ChangeBounds().setInterpolator(new EaseOutQuint()));

        } else {
            ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        }
        ft.replace(R.id.container, chooserFragment).addToBackStack(this.toString())
                .commit();
    }

    public void onRefresh() {
        if (isAdded() && !isRemoving()) {
            refresh();
        }
    }

    private void refresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        getTrackedSections();
                    }
                }
            });
            if (mSwipeRefreshLayout.isRefreshing()) {
                dismissProgress();
                cancelRequests();
            }
        }
    }

    private void getTrackedSections() {
        final RutgersApi api =
                new RutgersApiImpl(RutgersCTApp.getClient(), RutgersApi.ACTIVITY_TAG);

        List<TrackedSection> sectionsList = TrackedSection.listAll(TrackedSection.class);

        hasDataFlag = sectionsList.size() > 0;

        courseObservable = AppObservable.bindFragment(this, api.getTrackedSections(sectionsList));

        mCompositeSubscription.add(courseObservable
                .toSortedList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Course>>() {
                    @Override
                    public void onCompleted() {
                        shouldShowEmptyView();
                        dismissProgress();
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (t instanceof UnknownHostException) {
                            showSnackBar(getResources().getString(R.string.no_internet));
                        } else if (t instanceof JsonParseException || t instanceof IllegalStateException) {
                            showSnackBar(getResources().getString(R.string.server_down));
                        } else if (t instanceof TimeoutException) {
                            showSnackBar(getResources().getString(R.string.timed_out));
                        } else {
                            Timber.e(t, "Crash while attempting to complete mRequest in %s to %s"
                                    , TrackedSectionsFragment.this.toString(), t.toString());
                        }
                        shouldShowEmptyView();
                        dismissProgress();
                    }

                    @Override
                    public void onNext(List<Course> courseList) {
                        hasDataFlag = true;
                        addToDataset(courseList);
                    }
                }));

    }


    @Override
    public void itemClicked(Course course, View view, int positon) {

        CircleView circleView = ButterKnife.findById(view, R.id.section_number_background);

        final SectionInfoFragment sectionInfoFragment = new SectionInfoFragment();

        FragmentTransaction ft =
                this.getFragmentManager().beginTransaction();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mFab != null) {
                mFab.setTransitionName(getString(R.string.transition_name_fab));
                ft.addSharedElement(mFab, getString(R.string.transition_name_fab));
            }

            circleView.setTransitionName(getString(R.string.transition_name_circle_view));
            ft.addSharedElement(circleView, getString(R.string.transition_name_circle_view));

            Transition tsfSectionEnter = TransitionInflater
                    .from(getParentActivity())
                    .inflateTransition(R.transition.tsf_section_enter);

            Transition tsfSectionReturn = TransitionInflater
                    .from(getParentActivity())
                    .inflateTransition(R.transition.tsf_section_return);

            sectionInfoFragment.setEnterTransition(tsfSectionEnter);
            sectionInfoFragment.setReturnTransition(tsfSectionReturn);

            //This fragment
            setReenterTransition(new Fade(Fade.IN).addTarget(RecyclerView.class));
            setExitTransition(new Fade(Fade.OUT).addTarget(RecyclerView.class));

            sectionInfoFragment.setAllowReturnTransitionOverlap(false);
            sectionInfoFragment.setAllowEnterTransitionOverlap(false);




            Transition sharedElementsEnter = TransitionInflater.from(getParentActivity()).inflateTransition(R.transition.tsf_shared_element_enter);
            Transition sharedElementsReturn = TransitionInflater.from(getParentActivity()).inflateTransition(R.transition.tsf_shared_element_return);


            sectionInfoFragment.setSharedElementEnterTransition(sharedElementsEnter);
            sectionInfoFragment.setSharedElementReturnTransition(sharedElementsReturn);

            CircleSharedElementCallback sharedelementCallback = new CircleSharedElementCallback();
            sectionInfoFragment.setEnterSharedElementCallback(sharedelementCallback);
            sharedElementsEnter.addListener(sharedelementCallback.getTransitionCallback());



        } else {
            ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        }


        String indexNumber = course.getSections().get(0).getIndex();
        TrackedSection ts = TrackedSection.find(TrackedSection.class, "index_number = ?", indexNumber).get(0);
        Request request =  UrlUtils.getRequestFromTrackedSections(ts);

        sectionInfoFragment.setArguments(createArgs(request, course));
        ft.replace(R.id.container, sectionInfoFragment).addToBackStack(this.toString())
                .commit();
    }

    private Bundle createArgs(Parcelable parcelable, Course course) {
        Bundle bundle = new Bundle();
        ArrayList<Course> c = new ArrayList<>();
        c.add(course);
        bundle.putParcelableArrayList(RutgersCTApp.COURSE_LIST, c);
        bundle.putParcelable(RutgersCTApp.REQUEST, parcelable);
        return bundle;
    }

    @MainThread
    private void dismissProgress() {
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);

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
                                PreferenceUtils.setLearnedServerIssues(true);
                            }
                        }) // Snackbar's EventListener
                , getParentActivity()); // activity where it is displayed
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
                onRefresh();
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

        if (getParentActivity().getPackageManager().queryIntentActivities(rateAppIntent, 0).size() > 0) {
            startActivity(rateAppIntent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Listen to database changes
        DatabaseHandler.getInstance().setDatabaseListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        DatabaseHandler.getInstance().removeListener();
    }

    @Override
    public void onAdd(Request addedSection) {
        refresh();
    }

    @Override
    public void onRemove(Request removedSection) {
        refresh();
    }

}
