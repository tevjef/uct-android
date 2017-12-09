package com.tevinjeffrey.rutgersct.ui.subject;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar.BaseCallback;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.data.model.Subject;
import com.tevinjeffrey.rutgersct.data.search.SearchFlow;
import com.tevinjeffrey.rutgersct.data.search.SearchManager;
import com.tevinjeffrey.rutgersct.ui.base.MVPFragment;
import com.tevinjeffrey.rutgersct.ui.course.CourseFragment;
import com.tevinjeffrey.rutgersct.ui.utils.ItemClickListener;
import com.tevinjeffrey.rutgersct.utils.Utils;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

import static com.tevinjeffrey.rutgersct.data.model.extensions.Utils.SemesterUtils.readableString;

public class SubjectFragment extends MVPFragment
    implements SubjectView, SwipeRefreshLayout.OnRefreshListener, ItemClickListener<Subject, View> {

  private static final String TAG = SubjectFragment.class.getSimpleName();

  @BindView(R.id.toolbar)
  Toolbar mToolbar;
  @BindView(R.id.list)
  RecyclerView mRecyclerView;
  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout mSwipeRefreshLayout;
  @BindView(R.id.error_view)
  ViewGroup mErrorView;

  ArrayList<Subject> mListDataset;
  SearchFlow searchFlow;
  SubjectViewState mViewState = new SubjectViewState();

  @Inject
  SearchManager searchManager;
  @Inject
  SubjectSubcomponent subcomponent;

  private Unbinder unbinder;
  private Snackbar snackbar;

  public SubjectFragment() {
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
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    LayoutInflater themedInflator = inflater.cloneInContext(Utils.wrapContextTheme(
        getActivity(),
        R.style.RutgersCT
    ));
    final View rootView = themedInflator.inflate(R.layout.fragment_subjects, container, false);
    unbinder = ButterKnife.bind(this, rootView);
    return rootView;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //Recreate presenter if necessary.
    if (mBasePresenter == null) {
      mBasePresenter = new SubjectPresenterImpl(searchFlow);
      subcomponent.inject((SubjectPresenterImpl) mBasePresenter);
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
      getPresenter().loadSubjects(true);
    } else {
      //Silently load tracked sections on a config change
      if (!getPresenter().isLoading()) {
        getPresenter().loadSubjects(false);
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
        this.onRefresh();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    dismissSnackbar();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
  }

  public void initRecyclerView() {
    LinearLayoutManager layoutManager = new LinearLayoutManager(getParentActivity());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    layoutManager.setSmoothScrollbarEnabled(true);
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.addItemDecoration(new DividerItemDecoration(
        mRecyclerView.getContext(),
        DividerItemDecoration.VERTICAL
    ));
    mRecyclerView.setHasFixedSize(true);

    if (mListDataset == null) {
      mListDataset = new ArrayList<>(10);
    }

    if (mRecyclerView.getAdapter() == null) {
      mRecyclerView.setAdapter(new SubjectFragmentAdapter(mListDataset, this));
    }
  }

  public void initSwipeLayout() {
    mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
    mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green);
    mSwipeRefreshLayout.setOnRefreshListener(this);
  }

  public void initToolbar() {
    setToolbarTitle();
    setToolbar(mToolbar);
  }

  @Override
  public void injectTargets() {
    //RutgersCTApp.getObjectGraph(getParentActivity()).inject(this);
  }

  @Override
  public void onItemClicked(Subject subject, View view) {
    Timber.i("Selected subject: %s", subject);
    searchManager.getSearchFlow().subject = subject;
    startCourseFragement(new Bundle());
  }

  @Override
  public void onRefresh() {
    getPresenter().loadSubjects(true);
  }

  @Override
  public void setData(List<Subject> data) {
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
    mSwipeRefreshLayout.post(() -> {
      if (mSwipeRefreshLayout != null) {
        mSwipeRefreshLayout.setRefreshing(pullToRefresh);
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

  private void dismissSnackbar() {
    //It's only being dismissed to not leak the fragment
    if (snackbar != null) {
      snackbar.dismiss();
    }
  }

  private SubjectPresenter getPresenter() {
    return (SubjectPresenter) mBasePresenter;
  }

  private void setToolbarTitle() {
    String title = searchFlow.getUniversity().abbr + " " + readableString(searchFlow.semester);
    super.setToolbarTitle(mToolbar, title);
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
    snackbar = makeSnackBar(message);
    snackbar.setAction(R.string.retry, view -> {
      onRefresh();
      mViewState.snackBarShowing = false;
    });
    snackbar.addCallback(new BaseCallback<android.support.design.widget.Snackbar>() {
      @Override
      public void onDismissed(
          final android.support.design.widget.Snackbar transientBottomBar,
          final int event) {
        mViewState.snackBarShowing = false;
        snackbar.removeCallback(this);
      }

      @Override
      public void onShown(final android.support.design.widget.Snackbar transientBottomBar) {
        mViewState.snackBarShowing = true;
      }
    });
    snackbar.show();
  }

  private void startCourseFragement(Bundle b) {
    CourseFragment courseFragment = new CourseFragment();
    courseFragment.setArguments(b);
    FragmentTransaction ft = getFragmentManager().beginTransaction();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      Transition sfTransition =
          TransitionInflater.from(getParentActivity()).inflateTransition(R.transition.sf_exit);
      setExitTransition(sfTransition.excludeTarget(Toolbar.class, true));
      courseFragment.setAllowEnterTransitionOverlap(false);
    } else {
      ft.setCustomAnimations(
          R.animator.enter,
          R.animator.exit,
          R.animator.pop_enter,
          R.animator.pop_exit
      );
    }

    startFragment(this, courseFragment, ft);
  }
}