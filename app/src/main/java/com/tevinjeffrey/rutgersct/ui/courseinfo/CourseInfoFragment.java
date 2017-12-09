package com.tevinjeffrey.rutgersct.ui.courseinfo;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.data.model.Course;
import com.tevinjeffrey.rutgersct.data.model.Metadata;
import com.tevinjeffrey.rutgersct.data.model.Section;
import com.tevinjeffrey.rutgersct.data.model.Subject;
import com.tevinjeffrey.rutgersct.data.search.SearchFlow;
import com.tevinjeffrey.rutgersct.data.search.SearchManager;
import com.tevinjeffrey.rutgersct.ui.base.MVPFragment;
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoFragment;
import com.tevinjeffrey.rutgersct.ui.settings.SettingsActivity;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsFragment;
import com.tevinjeffrey.rutgersct.ui.utils.CircleSharedElementCallback;
import com.tevinjeffrey.rutgersct.ui.utils.CircleView;
import com.tevinjeffrey.rutgersct.ui.utils.ItemClickListener;
import com.tevinjeffrey.rutgersct.utils.Utils;
import icepick.State;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

@SuppressWarnings("ClassWithTooManyMethods")
public class CourseInfoFragment extends MVPFragment
    implements CourseInfoView, ItemClickListener<Section, View> {

  private static final String TAG = CourseInfoFragment.class.getSimpleName();

  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.course_info_list) RecyclerView mRecyclerView;
  @BindView(R.id.course_title_text) TextView mCourseTitleText;
  @BindView(R.id.app_bar_layout) AppBarLayout mAppBarLayout;
  @BindView(R.id.coordinator_layout) CoordinatorLayout mCoordinatorLayout;
  @BindView(R.id.subtitle) TextView mCreditsText;
  @BindView(R.id.shortenedCourseInfo) TextView mShortenedCourseInfo;
  @BindView(R.id.openSections_text) TextView mOpenSectionsText;
  @BindView(R.id.totalSections_text) TextView mTotalSectionsText;

  SearchFlow searchFlow;
  CourseInfoViewState mViewState = new CourseInfoViewState();

  @Inject SearchManager searchManager;

  private List<View> mHeaderViews = new ArrayList<>();
  private Course mSelectedCourse;
  private Unbinder unbinder;

  public CourseInfoFragment() { }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);

    if (searchManager.getSearchFlow() != null) {
      searchFlow = searchManager.getSearchFlow();
    } else {
      searchManager.setSearchFlow(searchFlow);
    }

    mSelectedCourse = searchFlow.getCourse();
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    LayoutInflater themedInflator = inflater.cloneInContext(Utils.wrapContextTheme(
        getActivity(),
        R.style.RutgersCT_Accent
    ));
    final View rootView = themedInflator.inflate(R.layout.fragment_course_info, container, false);
    unbinder = ButterKnife.bind(this, rootView);
    return rootView;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mViewState.apply(this, savedInstanceState != null);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_course_info, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_add_all:
        //noinspection ClassReferencesSubclass
        getParentActivity().mBackstackCount = 0;
        getFragmentManager().popBackStackImmediate(
            TrackedSectionsFragment.TAG,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        );
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override public void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
  }

  public void initHeaderView() {
    View courseMetadata = createCourseMetaDataView();
    mHeaderViews.add(courseMetadata);
  }

  public void initRecyclerView() {
    LinearLayoutManager layoutManager = new LinearLayoutManager(getParentActivity());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setHasFixedSize(true);
    if (mRecyclerView.getAdapter() == null) {
      mRecyclerView.setAdapter(new CourseInfoFragmentAdapter(mHeaderViews,
          mSelectedCourse.sections, this
      ));
    }
  }

  @Override
  public void initToolbar() {
    setToolbar(mToolbar);
    getParentActivity().getSupportActionBar().setDisplayShowTitleEnabled(false);
  }

  public void initViews() {
    setCourseTitle();
    setShortenedCourseInfo();
    setOpenSections();
    setTotalSections();
  }

  @Override
  public void injectTargets() {
    //RutgersCTApp.getObjectGraph(getParentActivity()).inject(this);
  }

  @Override
  public void onItemClicked(Section section, View view) {
    Timber.i("Selected section: %s", section);
    searchManager.getSearchFlow().section = section;
    Bundle bundle = new Bundle();
    startSectionInfoFragment(bundle, view);
  }

  @Override
  public String toString() {
    return TAG;
  }

  private View createCourseMetaDataView() {
    ViewGroup root = (ViewGroup) getParentActivity()
        .getLayoutInflater()
        .inflate(R.layout.course_info_metadata, null);
    for (Metadata data : mSelectedCourse.metadata) {
      ViewGroup metadata =
          (ViewGroup) LayoutInflater.from(getParentActivity()).inflate(R.layout.metadata, null);
      TextView title = ButterKnife.findById(metadata, R.id.metadata_title);
      TextView description = ButterKnife.findById(metadata, R.id.metadata_text);
      description.setMovementMethod(new LinkMovementMethod());
      title.setText(data.title);
      description.setText(Html.fromHtml(data.content));
      root.addView(metadata);
    }
    return root;
  }

  private void setCourseTitle() {
    mCourseTitleText.setText(mSelectedCourse.name);
  }

  private void setOpenSections() {
    mOpenSectionsText.setText(String.valueOf(com.tevinjeffrey.rutgersct.data.model.extensions.Utils.CourseUtils
        .getOpenSections(mSelectedCourse)));
  }

  private void setShortenedCourseInfo() {
    //String offeringUnitCode = mSelectedCourse.getOfferingUnitCode();
    Subject subject = searchFlow.getSubject();
    Course course = searchFlow.getCourse();
    if (subject != null) {
      String shortenedCourseInfo = subject.number + ": " + subject.name + " › " + course.number;
      mShortenedCourseInfo.setText(shortenedCourseInfo);
    }
  }

  private void setTotalSections() {
    mTotalSectionsText.setText(String.valueOf(mSelectedCourse.sections.size()));
  }

  private void startSectionInfoFragment(Bundle b, View clickedView) {
    SectionInfoFragment sectionInfoFragment = new SectionInfoFragment();

    FragmentTransaction ft = getFragmentManager().beginTransaction();

    CircleView circleView = ButterKnife.findById(clickedView, R.id.section_number_background);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

      circleView.setTransitionName(getString(R.string.transition_name_circle_view));
      ft.addSharedElement(circleView, getString(R.string.transition_name_circle_view));

      mAppBarLayout.setTransitionName(null);

      Transition cifSectionEnter = TransitionInflater
          .from(getParentActivity())
          .inflateTransition(R.transition.cif_section_enter);

      Transition cifSectionReturn = TransitionInflater
          .from(getParentActivity())
          .inflateTransition(R.transition.cif_section_return);

      sectionInfoFragment.setEnterTransition(cifSectionEnter);
      sectionInfoFragment.setReturnTransition(cifSectionReturn);

      setReenterTransition(new Fade(Fade.IN).setDuration(200));

      Transition cifExit = TransitionInflater
          .from(getParentActivity())
          .inflateTransition(R.transition.cif_exit);

      setExitTransition(cifExit);

      sectionInfoFragment.setAllowReturnTransitionOverlap(false);
      sectionInfoFragment.setAllowEnterTransitionOverlap(false);

      Transition sharedElementsEnter = TransitionInflater
          .from(getParentActivity()).inflateTransition(R.transition.cif_shared_element_enter);

      Transition sharedElementsReturn = TransitionInflater
          .from(getParentActivity()).inflateTransition(R.transition.cif_shared_element_return);

      sectionInfoFragment.setSharedElementEnterTransition(sharedElementsEnter);
      sectionInfoFragment.setSharedElementReturnTransition(sharedElementsReturn);

      CircleSharedElementCallback sharedelementCallback = new CircleSharedElementCallback();
      sectionInfoFragment.setEnterSharedElementCallback(sharedelementCallback);
      sharedElementsEnter.addListener(sharedelementCallback.getTransitionCallback());
    } else {
      ft.setCustomAnimations(
          R.animator.enter,
          R.animator.exit,
          R.animator.pop_enter,
          R.animator.pop_exit
      );
    }

    sectionInfoFragment.setArguments(b);
    startFragment(this, sectionInfoFragment, ft);
  }
}