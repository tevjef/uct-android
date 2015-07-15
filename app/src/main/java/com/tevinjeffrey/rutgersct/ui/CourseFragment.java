package com.tevinjeffrey.rutgersct.ui;

import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.adapters.CourseFragmentAdapter;
import com.tevinjeffrey.rutgersct.animator.EaseOutQuint;
import com.tevinjeffrey.rutgersct.rutgersapi.RutgersApi;
import com.tevinjeffrey.rutgersct.rutgersapi.RutgersApiImpl;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Subject;

import org.apache.commons.lang3.text.WordUtils;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CancellationException;
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

public class CourseFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, CourseFragmentAdapter.ItemClickListener{

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.list_view)
    RecyclerView mRecyclerView;

    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.empty_view)
    ViewGroup mEmptyView;

    @Icicle
    Request mRequest;

    @Icicle
    ArrayList<Course> mListDataset;

    @Icicle
    boolean hasDataFlag = true;

    private RecyclerView.Adapter mListAdapter;

    public CourseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequest = getArguments().getParcelable(RutgersCTApp.REQUEST);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.setPrimaryWindow(getParentActivity());
        setRetainInstance(true);

        final View rootView = inflater.inflate(R.layout.subjects, container, false);

        ButterKnife.inject(this, rootView);

        initViews();

        refresh();

        return rootView;
    }

    public void initViews() {
        setToolbarTitle();
        setToolbar(mToolbar);
        initRecyclerView();
        setupSwipeRefreshLayout();
    }

    public void setupSwipeRefreshLayout() {
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void itemClicked(Course course, View view, int positon) {
        startCourseInfoFragment(createArgs(mRequest, course));
    }

    private void shouldShowEmptyView() {
        if (mEmptyView != null) {
            if (isDatasetEmpty() && !hasDataFlag) {
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
        mListAdapter = new CourseFragmentAdapter(mListDataset,
                this);
        mRecyclerView.setAdapter(mListAdapter);

        shouldShowEmptyView();
    }

    private void getCourses() {
        RutgersApi api = new RutgersApiImpl(RutgersCTApp.getClient());

        Observable<Course> mCourseObservable = AppObservable.bindFragment(this, api.getCourses(mRequest));

        mCompositeSubscription.add(mCourseObservable
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Course>>() {
                    @Override
                    public void onCompleted() {
                        shouldShowEmptyView();
                        dismissProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof UnknownHostException) {
                            showSnackBar(getResources().getString(R.string.no_internet));
                        } else if (e instanceof IllegalStateException && !(e instanceof CancellationException)) {
                            cancelRequests();
                            showSnackBar(getResources().getString(R.string.server_down));
                        } else if (e instanceof TimeoutException) {
                            cancelRequests();
                            showSnackBar(getResources().getString(R.string.timed_out));
                        } else if (!(e instanceof CancellationException)) {
                            Timber.e(e, "Crash while attempting to complete mRequest in %s to %s"
                                    , CourseFragment.this.toString(), mRequest.toString());
                        }
                        //hasDataFlag = false;
                        shouldShowEmptyView();
                        dismissProgress();
                    }

                    @Override
                    public void onNext(List<Course> courseList) {
                        if(courseList.size() > 0) {
                            hasDataFlag = true;
                            addToDataset(courseList);
                        } else {
                            hasDataFlag = false;
                            shouldShowEmptyView();
                        }
                    }
                }));
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
                        getCourses();
                    }

                }
            });
        } else if (mSwipeRefreshLayout != null) {
            dismissProgress();
            cancelRequests();
        }
    }

    @Override
    public void onRefresh() {
        if (isAdded() && !isRemoving()) {
            refresh();
        }
    }

    private void dismissProgress() {
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public void setToolbar(Toolbar toolbar) {
        super.setToolbar(toolbar);
        ActionBar actionBar = getParentActivity().getSupportActionBar();
        if(actionBar != null) {
            getParentActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getParentActivity().getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setToolbarTitle() {
        Iterable<Subject> al = getArguments().getParcelableArrayList(RutgersCTApp.SUBJECTS_LIST);
        for (Subject s : al) {
            if (s.getCode().equals(mRequest.getSubject())) {
                super.setToolbarTitle(mToolbar, s.getCode() + " | "
                        + WordUtils.capitalize(s.getDescription().toLowerCase()));
            }
        }
    }

    private void startCourseInfoFragment(Bundle b) {
        CourseInfoFragment courseInfoFragment = new CourseInfoFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ChangeBounds changeBoundsTransition = new ChangeBounds();
            changeBoundsTransition.setInterpolator(new EaseOutQuint());

            courseInfoFragment.setEnterTransition(new Fade(Fade.IN).setStartDelay(250));
            courseInfoFragment.setReturnTransition(new Fade(Fade.OUT).setDuration(50));

            courseInfoFragment.setAllowReturnTransitionOverlap(false);
            courseInfoFragment.setAllowEnterTransitionOverlap(false);

            courseInfoFragment.setSharedElementEnterTransition(changeBoundsTransition);
            courseInfoFragment.setSharedElementReturnTransition(changeBoundsTransition);

            ft.addSharedElement(mToolbar, getString(R.string.transition_name_tool_background));
        } else {
            ft.setCustomAnimations(R.anim.enter, R.anim.exit, 0, R.anim.pop_exit);
        }

        courseInfoFragment.setArguments(b);
        ft.replace(R.id.container, courseInfoFragment).addToBackStack(this.toString())
                .commit();
    }

    private Bundle createArgs(Parcelable parcelable, Course selectedCourse) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(RutgersCTApp.REQUEST, parcelable);
        bundle.putParcelableArrayList(RutgersCTApp.SUBJECTS_LIST, getArguments().getParcelableArrayList(RutgersCTApp.SUBJECTS_LIST));
        bundle.putParcelableArrayList(RutgersCTApp.COURSE_LIST, mListDataset);
        bundle.putParcelable(RutgersCTApp.SELECTED_COURSE, selectedCourse);
        return bundle;
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