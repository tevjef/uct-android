package com.tevinjeffrey.rutgersct.ui.course;

import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
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
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.gson.JsonParseException;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.ActionSwipeListener;
import com.nispok.snackbar.listeners.EventListener;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.data.rutgersapi.exceptions.RutgersServerIOException;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Course;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Subject;
import com.tevinjeffrey.rutgersct.data.uctapi.search.SearchFlow;
import com.tevinjeffrey.rutgersct.data.uctapi.search.SearchManager;
import com.tevinjeffrey.rutgersct.ui.base.MVPFragment;
import com.tevinjeffrey.rutgersct.ui.courseinfo.CourseInfoFragment;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsView;
import com.tevinjeffrey.rutgersct.ui.utils.ItemClickListener;
import com.tevinjeffrey.rutgersct.utils.Utils;
import icepick.State;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

public class CourseFragment extends MVPFragment
    implements CourseView, SwipeRefreshLayout.OnRefreshListener,
    ItemClickListener<Course, View> {

  private final String TAG = this.getClass().getSimpleName();

  @Bind(R.id.toolbar)
  Toolbar mToolbar;

  @Bind(R.id.list_view)
  RecyclerView mRecyclerView;

  @Bind(R.id.swipeRefreshLayout)
  SwipeRefreshLayout mSwipeRefreshLayout;

  @Bind(R.id.error_view)
  ViewGroup mErrorView;

  @State
  ArrayList<Course> mListDataset;

  @State
  CourseViewState mViewState = new CourseViewState();

  @State
  SearchFlow searchFlow;

  @Inject
  SearchManager searchManager;

  private Subject selectedSubject;

  public CourseFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);

    if (searchManager.getSearchFlow() != null) {
      searchFlow = searchManager.getSearchFlow();
    } else {
      searchManager.setSearchFlow(searchFlow);
    }

    selectedSubject = searchFlow.getSubject();
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    LayoutInflater themedInflator =
        inflater.cloneInContext(Utils.wrapContextTheme(getActivity(), R.style.RutgersCT));
    final View rootView = themedInflator.inflate(R.layout.fragment_courses, container, false);
    ButterKnife.bind(this, rootView);
    return rootView;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //Recreate presenter if necessary.
    if (mBasePresenter == null) {
      mBasePresenter = new CoursePresenterImpl(searchFlow);
      RutgersCTApp.getObjectGraph(getParentActivity()).inject(mBasePresenter);
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
      getPresenter().loadCourses(true);
    } else {
      //Silently refresh tracked sections
      if (!getPresenter().isLoading()) {
        getPresenter().loadCourses(false);
      }
    }
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
        onRefresh();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
    dismissSnackbar();
  }

  public void initRecyclerView() {
    LinearLayoutManager layoutManager = new LinearLayoutManager(getParentActivity());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    layoutManager.setSmoothScrollbarEnabled(true);
    mRecyclerView.addItemDecoration(new DividerItemDecoration(
        mRecyclerView.getContext(),
        DividerItemDecoration.VERTICAL
    ));
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setHasFixedSize(true);

    if (mListDataset == null) {
      mListDataset = new ArrayList<>(10);
    }

    if (mRecyclerView.getAdapter() == null) {
      mRecyclerView.setAdapter(new CourseFragmentAdapter(mListDataset, this));
    }
  }

  public void initSwipeLayout() {
    mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
    mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green);
    mSwipeRefreshLayout.setOnRefreshListener(this);
  }

  @Override
  public void initToolbar() {
    setToolbarTitle();
    setToolbar(mToolbar);
  }

  @Override
  public void injectTargets() {
    RutgersCTApp.getObjectGraph(getParentActivity()).inject(this);
  }

  @Override
  public void onItemClicked(Course course, View view) {
    Timber.i("Selected course: %s", course);
    searchManager.getSearchFlow().course = course;
    startCourseInfoFragment(createArgs(course));
  }

  @Override
  public void onRefresh() {
    getPresenter().loadCourses(true);
  }

  @Override
  public void setData(List<Course> data) {
    mViewState.data = data;
    mListDataset.clear();
    mListDataset.addAll(data);
    if (mRecyclerView.getAdapter() != null) {
      mRecyclerView.getAdapter().notifyDataSetChanged();
    }
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

  public void showLayout(TrackedSectionsView.LayoutType type) {
    mViewState.layoutType = type;
    switch (type) {
      case ERROR:
        showRecyclerView(View.GONE);
        showErrorLayout(View.VISIBLE);
        //enableSwipeRefreshLayout(true);
        break;
      case LIST:
        showErrorLayout(View.GONE);
        showRecyclerView(View.VISIBLE);
        //enableSwipeRefreshLayout(true);
        break;
      default:
        throw new RuntimeException("Unknown type: " + type);
    }
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

  @OnClick(R.id.try_again)
  public void onTryAgainClick(View view) {
    onRefresh();
  }

  private boolean adapterHasItems() {
    return mRecyclerView.getAdapter().getItemCount() > 0;
  }

  private Bundle createArgs(Course selectedCourse) {
    Bundle bundle = new Bundle();
    bundle.putParcelable(SELECTED_COURSE, selectedCourse);
    return bundle;
  }

  private void dismissSnackbar() {
    //It's only being dismissed to not leak the fragment
    if (SnackbarManager.getCurrentSnackbar() != null) {
      SnackbarManager.dismiss();
    }
  }

  private CoursePresenter getPresenter() {
    return (CoursePresenter) mBasePresenter;
  }

  private void setToolbarTitle() {
    super.setToolbarTitle(mToolbar, selectedSubject.number + ": " + selectedSubject.name);
  }

  private void showErrorLayout(int visibility) {
    if (mErrorView.getVisibility() != visibility) {
      mErrorView.setVisibility(visibility);
    }
  }

  private void showRecyclerView(int visibility) {
    if (mRecyclerView.getVisibility() != visibility) {
      mRecyclerView.setVisibility(visibility);
    }
  }

  private void showSnackBar(CharSequence message) {
    SnackbarManager.show(
        Snackbar.with(getParentActivity())
            .type(SnackbarType.MULTI_LINE)
            .text(message)
            .actionLabel(R.string.retry)// text to display
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
            .actionColor(ContextCompat.getColor(getParentActivity(), android.R.color.white))
            .color(ContextCompat.getColor(
                getParentActivity(),
                R.color.accent
            ))// action button label color
            .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
            .eventListener(new EventListener() {
              @Override
              public void onDismiss(Snackbar snackbar) {

              }

              @Override
              public void onDismissByReplace(Snackbar snackbar) {

              }

              @Override
              public void onDismissed(Snackbar snackbar) {

              }

              @Override
              public void onShow(Snackbar snackbar) {
                if (snackbar != null) {
                  mViewState.snackBarShowing = true;
                }
              }

              @Override
              public void onShowByReplace(Snackbar snackbar) {

              }

              @Override
              public void onShown(Snackbar snackbar) {

              }
            })
        , getParentActivity()); // activity where it is displayed
  }

  private void startCourseInfoFragment(Bundle b) {
    CourseInfoFragment courseInfoFragment = new CourseInfoFragment();
    FragmentTransaction ft = getFragmentManager().beginTransaction();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      ChangeBounds changeBoundsTransition = new ChangeBounds();
      changeBoundsTransition.setInterpolator(new DecelerateInterpolator());

      courseInfoFragment.setEnterTransition(new Fade(Fade.IN).setStartDelay(250));
      courseInfoFragment.setReturnTransition(new Fade(Fade.OUT).setDuration(50));

      courseInfoFragment.setAllowReturnTransitionOverlap(false);
      courseInfoFragment.setAllowEnterTransitionOverlap(false);

      courseInfoFragment.setSharedElementEnterTransition(changeBoundsTransition);
      courseInfoFragment.setSharedElementReturnTransition(changeBoundsTransition);

      ft.addSharedElement(mToolbar, getString(R.string.transition_name_tool_background));
    } else {
      ft.setCustomAnimations(
          R.animator.enter,
          R.animator.exit,
          R.animator.pop_enter,
          R.animator.pop_exit
      );
    }
    courseInfoFragment.setArguments(b);
    startFragment(this, courseInfoFragment, ft);
  }
}