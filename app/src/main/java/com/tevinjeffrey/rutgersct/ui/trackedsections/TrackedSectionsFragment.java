package com.tevinjeffrey.rutgersct.ui.trackedsections;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import android.widget.TextView;

import com.google.gson.JsonParseException;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.ActionSwipeListener;
import com.nispok.snackbar.listeners.EventListener;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.adapters.TrackedSectionsFragmentAdapter;
import com.tevinjeffrey.rutgersct.animator.CircleSharedElementCallback;
import com.tevinjeffrey.rutgersct.customviews.CircleView;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.ui.base.MVPFragment;
import com.tevinjeffrey.rutgersct.ui.chooser.ChooserFragment;
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoFragment;
import com.tevinjeffrey.rutgersct.utils.RecyclerSimpleScrollListener;
import com.tevinjeffrey.rutgersct.utils.Utils;
import com.tevinjeffrey.rutgersct.utils.exceptions.RutgersServerIOException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icicle;
import rx.functions.Action1;

@SuppressWarnings({"ClassWithTooManyMethods"})
public class TrackedSectionsFragment extends MVPFragment implements TrackedSectionsView, SwipeRefreshLayout.OnRefreshListener,
        TrackedSectionsFragmentAdapter.ItemClickListener {

    public static final String TAG = TrackedSectionsFragment.class.getSimpleName();

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.add_courses_fab)
    FloatingActionButton mFab;

    @Bind(R.id.tsf_list)
    RecyclerView mRecyclerView;

    @Bind(R.id.add_courses_to_track)
    ViewGroup mEmptyView;

    @Bind(R.id.error_view)
    ViewGroup mErrorView;

    @Icicle
    TrackedSectionsViewState mViewState = new TrackedSectionsViewState();

    private ArrayList<Course.Section> mListDataset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater themedInflator = inflater.cloneInContext(Utils.wrapContextTheme(getActivity(), R.style.RutgersCT));
        ViewGroup rootView = (ViewGroup) themedInflator.inflate(R.layout.fragment_tracked_sections, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Recreate presenter if necessary.
        if (mBasePresenter == null) {
            mBasePresenter = new TrackedSectionsPresenterImpl();
            getObjectGraph().inject(mBasePresenter);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewState.apply(this, savedInstanceState != null);
        //Attach view to presenter
        mBasePresenter.attachView(this);

        //Load data depending on if the view is currently refreshing
        if (mIsInitialLoad) {
            getPresenter().loadTrackedSections(true);
        } else {
            //Silently refresh tracked sections
            if (!getPresenter().isLoading()) {
                getPresenter().loadTrackedSections(false);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        dismissSnackbar();
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initToolbar() {
        setToolbarTitle(mToolbar, "");
        setToolbar(mToolbar);
    }

    @Override
    public void showLoading(final boolean pullToRefresh) {
        mViewState.isRefreshing = pullToRefresh;
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(pullToRefresh);
                }
            }
        });
    }

    public void setData(List<Course.Section> data) {
        mViewState.data = data;
        mListDataset.clear();
        mListDataset.addAll(data);
        mRecyclerView.getAdapter().notifyDataSetChanged();

    }

    @Override
    public void showError(Throwable t) {
        String message;
        Resources resources = getContext().getResources();
        if (t instanceof UnknownHostException) {
            message = resources.getString(R.string.no_internet);
        } else if (t instanceof JsonParseException || t instanceof RutgersServerIOException) {
            message = resources.getString(R.string.server_down);
        } else if (t instanceof SocketTimeoutException) {
            message = resources.getString(R.string.timed_out);
        } else {
            message = t.getMessage();

        }
        //error message finalized, now save it.
        mViewState.errorMessage = message;
        //Show the error layout if there's nothing in the adpater to show.
        // Redirects the message that would usually be in the snackbar, to error layout.
        if (!adapterHasItems()) {
            showLayout(LayoutType.ERROR);
            TextView textViewMessage = ButterKnife.findById(mErrorView, R.id.text);
            textViewMessage.setText(message);
        } else {
            showSnackBar(message);
        }
    }


    public void showLayout(LayoutType type) {
        mViewState.layoutType = type;
        switch (type) {
            case ERROR:
                showEmptyLayout(View.GONE);
                showRecyclerView(View.GONE);
                showErrorLayout(View.VISIBLE);
                //enableSwipeRefreshLayout(true);
                break;
            case EMPTY:
                showRecyclerView(View.GONE);
                showErrorLayout(View.GONE);
                showEmptyLayout(View.VISIBLE);
                //enableSwipeRefreshLayout(false);
                break;
            case LIST:
                showErrorLayout(View.GONE);
                showEmptyLayout(View.GONE);
                showRecyclerView(View.VISIBLE);
                //enableSwipeRefreshLayout(true);
                break;
            default:
                throw new RuntimeException("Unknown type: " + type);
        }
    }

    @OnClick(R.id.try_again)
    public void onTryAgainClick(View view) {
        onRefresh();
    }

    private boolean adapterHasItems() {
        return mRecyclerView.getAdapter().getItemCount() > 0;
    }

    private void dismissSnackbar() {
        //It's only being dismissed to not leak the fragment
        if (SnackbarManager.getCurrentSnackbar() != null) {
            SnackbarManager.dismiss();
        }
    }

    private void enableSwipeRefreshLayout(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
    }

    private void showErrorLayout(int visibility) {
        if (mErrorView.getVisibility() != visibility)
            mErrorView.setVisibility(visibility);
    }

    private void showEmptyLayout(int visibility) {
        if (mEmptyView.getVisibility() != visibility)
            mEmptyView.setVisibility(visibility);
    }

    private void showRecyclerView(int visibility) {
        if (mRecyclerView.getVisibility() != visibility)
            mRecyclerView.setVisibility(visibility);
    }

    public void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getParentActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        RecyclerSimpleScrollListener recyclerSimpleScrollListener = new RecyclerSimpleScrollListener();
        mRecyclerView.addOnScrollListener(recyclerSimpleScrollListener);
        recyclerSimpleScrollListener.getDirectionObservable().subscribe(new Action1<RecyclerSimpleScrollListener.Direction>() {
            @Override
            public void call(RecyclerSimpleScrollListener.Direction direction) {
                switch (direction) {
                    case UP:
                        animateFabIn();
                        //mFab.show();
                        break;
                    case DOWN:
                        animateFabOut();
                        //mFab.hide();
                        break;
                    case NEUTRAL:
                        //mFab.jumpDrawablesToCurrentState();
                        break;
                }
            }

            private void animateFabIn() {
                ViewCompat.animate(mFab).alpha(1).setStartDelay(50).start();
                ViewCompat.animate(mFab).translationY(mViewState.snackBarShowing?
                        -SnackbarManager.getCurrentSnackbar().getHeight():0).setStartDelay(50).start();
            }

            private void animateFabOut() {
                ViewCompat.animate(mFab).alphaBy(0).setStartDelay(50).start();
                ViewCompat.animate(mFab).translationYBy(250).setStartDelay(50).start();
            }
        });

        if (mListDataset == null) {
            mListDataset = new ArrayList<>(10);
        }

        if (mRecyclerView.getAdapter() == null) {
            mRecyclerView.setAdapter(new TrackedSectionsFragmentAdapter(mListDataset, this));
        }
    }

    public void initSwipeLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green);
    }

    @OnClick(R.id.add_courses_fab)
    public void onFabClick(View view) {
        startChooserFragment();
    }


    @Override
    public void onRefresh() {
        getPresenter().loadTrackedSections(true);
    }

    @Override
    public void onItemClicked(Course.Section section, View view, int positon) {
        startSectionInfoFragment(SectionInfoFragment.newInstance(section), view);
    }

    private void showSnackBar(CharSequence message) {
        SnackbarManager.show(
                Snackbar.with(getParentActivity())
                        .type(SnackbarType.MULTI_LINE)
                        .text(message)
                        .actionLabel("RETRY")// text to display
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                                onRefresh();
                                mViewState.snackBarShowing = false;
                            }
                        })
                        .swipeListener(new ActionSwipeListener() {
                            @Override
                            public void onSwipeToDismiss() {
                                mViewState.snackBarShowing = false;
                            }
                        })
                        .actionColor(getResources().getColor(android.R.color.white))
                        .color(getResources().getColor(R.color.accent))// action button label color
                        .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                        .eventListener(new EventListener() {
                            @Override
                            public void onShow(Snackbar snackbar) {
                                if (snackbar != null) {
                                    mViewState.snackBarShowing = true;
                                    if (mFab != null) {
                                        ViewCompat.animate(mFab).translationYBy(-snackbar.getHeight()).setInterpolator(new OvershootInterpolator());
                                    }
                                }

                            }

                            @Override
                            public void onShowByReplace(Snackbar snackbar) {
                            }

                            @Override
                            public void onShown(Snackbar snackbar) {

                            }

                            @Override
                            public void onDismiss(Snackbar snackbar) {
                                if (mFab != null) {
                                    ViewCompat.animate(mFab).translationYBy(snackbar.getHeight()).setInterpolator(new OvershootInterpolator());
                                }
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

    private void startChooserFragment() {
        ChooserFragment chooserFragment = ChooserFragment.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setTransitionName(getString(R.string.transition_name_tool_background));
            ft.addSharedElement(mToolbar, getString(R.string.transition_name_tool_background));
            setExitTransition(new Fade(Fade.OUT).setDuration(getResources().getInteger(R.integer.exit_anim)));
            chooserFragment.setAllowEnterTransitionOverlap(false);
            chooserFragment.setAllowReturnTransitionOverlap(false);
        } else {
            ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        }
        startFragment(TrackedSectionsFragment.this, chooserFragment, ft);
    }

    private void startSectionInfoFragment(SectionInfoFragment sectionInfoFragment, View view) {
        FragmentTransaction ft =
                this.getFragmentManager().beginTransaction();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CircleView circleView = ButterKnife.findById(view, R.id.section_number_background);
            mFab.setTransitionName(getString(R.string.transition_name_fab));
            ft.addSharedElement(mFab, getString(R.string.transition_name_fab));

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

            setReenterTransition(new Fade(Fade.IN).addTarget(RecyclerView.class));
            setExitTransition(new Fade(Fade.OUT).addTarget(RecyclerView.class));

            sectionInfoFragment.setAllowReturnTransitionOverlap(false);
            sectionInfoFragment.setAllowEnterTransitionOverlap(false);


            Transition sharedElementsEnter = TransitionInflater
                    .from(getParentActivity())
                    .inflateTransition(R.transition.tsf_shared_element_enter);
            Transition sharedElementsReturn = TransitionInflater
                    .from(getParentActivity())
                    .inflateTransition(R.transition.tsf_shared_element_return);


            sectionInfoFragment.setSharedElementEnterTransition(sharedElementsEnter);
            sectionInfoFragment.setSharedElementReturnTransition(sharedElementsReturn);

            CircleSharedElementCallback sharedelementCallback = new CircleSharedElementCallback();
            sectionInfoFragment.setEnterSharedElementCallback(sharedelementCallback);
            sharedElementsEnter.addListener(sharedelementCallback.getTransitionCallback());

        } else {
            ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        }
        startFragment(this, sectionInfoFragment, ft);
    }

    private void launchWebReg() {
        String url = "http://webreg.rutgers.edu";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void lauchMarket() {
        final Uri uri = Uri.parse("market://details?id=" + getParentActivity().getApplicationContext().getPackageName());
        final Intent rateAppIntent = new Intent(Intent.ACTION_VIEW, uri);

        if (getParentActivity().getPackageManager().queryIntentActivities(rateAppIntent, 0).size() > 0) {
            startActivity(rateAppIntent);
        }
    }

    private TrackedSectionsPresenter getPresenter() {
        return (TrackedSectionsPresenter) mBasePresenter;
    }

    @Override
    public String toString() {
        return TAG;
    }

}
