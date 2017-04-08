package com.tevinjeffrey.rutgersct.ui.sectioninfo;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.squareup.otto.Bus;
import com.tevinjeffrey.rmp.common.Professor;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Meeting;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Metadata;
import com.tevinjeffrey.rutgersct.data.uctapi.search.SearchFlow;
import com.tevinjeffrey.rutgersct.data.uctapi.search.SearchManager;
import com.tevinjeffrey.rutgersct.ui.base.MVPFragment;
import com.tevinjeffrey.rutgersct.ui.utils.RatingLayoutInflater;
import com.tevinjeffrey.rutgersct.utils.Utils;
import icepick.State;
import javax.inject.Inject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SectionInfoFragment extends MVPFragment implements SectionInfoView {

  private static final String TAG = SectionInfoFragment.class.getSimpleName();

  @Bind(R.id.prof_ratings_container)
  ViewGroup ratingsContainer;

  @Bind(R.id.prof_ratings_root)
  ViewGroup ratingsRoot;

  @Bind(R.id.semester_text)
  TextView mSemesterText;

  @Bind(R.id.course_title_text)
  TextView mCourseTitleText;

  @Bind(R.id.sectionNumber_text)
  TextView mSectionNumberText;

  @Bind(R.id.indexNumber_text)
  TextView mIndexNumberText;

  @Bind(R.id.subtitle)
  TextView mCreditsText;

  @Bind(R.id.tCredits)
  ViewGroup mCreditsViewGroup;

  @Bind(R.id.instructors_text)
  TextView mInstructorsText;

  @Bind(R.id.toolbar)
  Toolbar mToolbar;

  @Bind(R.id.section_times_container)
  LinearLayout mSectionTimeContainer;

  @Bind(R.id.add_courses_fab)
  FloatingActionButton mFab;

  @Bind(R.id.collapsing_toolbar)
  CollapsingToolbarLayout mCollapsingToolbar;

  @Bind(R.id.section_metadata)
  LinearLayout sectionMetadataContainer;

  @State
  SearchFlow searchFlow;

  @State
  SectionInfoViewState mViewState = new SectionInfoViewState();

  @Inject
  Bus mBus;

  @Inject
  SearchManager searchManager;

  public SectionInfoFragment() {
  }

  public static SectionInfoFragment newInstance() {
    final SectionInfoFragment newInstance = new SectionInfoFragment();
    return newInstance;
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
      LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    final Context contextThemeWrapper;
    // create ContextThemeWrapper from the original Activity Context with the custom theme
    if (searchFlow.getSection().status.equals("Open")) {
      contextThemeWrapper = Utils.wrapContextTheme(getActivity(), R.style.RutgersCT_Green);

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        getActivity().setTaskDescription(
            new ActivityManager.TaskDescription(
                null, null,
                ContextCompat.getColor(container.getContext(), R.color.green)
            )
        );
      }
    } else {
      contextThemeWrapper = Utils.wrapContextTheme(getActivity(), R.style.RutgersCT_Red);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        getActivity().setTaskDescription(
            new ActivityManager.TaskDescription(
                null, null,
                ContextCompat.getColor(container.getContext(), R.color.red)
            )
        );
      }
    }

    // clone the inflater using the ContextThemeWrapper
    LayoutInflater themedInflator = inflater.cloneInContext(contextThemeWrapper);

    final View rootView = themedInflator.inflate(R.layout.fragment_section_info, container, false);

    ButterKnife.bind(this, rootView);

    return rootView;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //Recreate presenter if necessary.
    if (mBasePresenter == null) {
      mBasePresenter = new SectionInfoPresenterImpl(searchFlow);
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
      mViewState.shouldAnimateFabIn = false;
    }

    //Requires a database access and the results should not be saved.
    getPresenter().setFabState(false);
    getPresenter().loadRMP();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_fragment_info, menu);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override
  public void addErrorProfessor(String name) {
    addRMPView(new RatingLayoutInflater(getParentActivity(), null)
        .getErrorLayout(name, searchFlow.getSection()));
  }

  @Override
  public void addRMPProfessor(Professor professor) {
    RatingLayoutInflater ratingLayoutInflater =
        new RatingLayoutInflater(getParentActivity(), professor);
    addRMPView(ratingLayoutInflater.getProfessorLayout());
  }

  @Override
  public void hideRatingsLoading() {
    ViewGroup progress = ButterKnife.findById(ratingsRoot, R.id.rmp_progressview);
    progress.setVisibility(GONE);
  }

  @Override
  public void initToolbar() {
    setToolbar(mToolbar);
    getParentActivity().getSupportActionBar().setDisplayShowTitleEnabled(false);
  }

  public void initViews() {
    setSectionNumber();
    setSectionIndex();
    setSectionCredits();
    setCourseTitle();
    showSectionMetadata();
    setTimes();
    setInstructors();
    setSemester();
  }

  @Override
  public void injectTargets() {
    RutgersCTApp.getObjectGraph(getParentActivity()).inject(this);
  }

  public void showFab(boolean animate) {
    if (animate) {
      AnimatorSet set = new AnimatorSet();
      set.playTogether(
          ObjectAnimator.ofFloat(mFab, "scaleX", 0, 1),
          ObjectAnimator.ofFloat(mFab, "scaleY", 0, 1),
          ObjectAnimator.ofFloat(mFab, "alpha", 0, 1)

      );
      set.setInterpolator(new DecelerateInterpolator());
      set.setStartDelay(400);
      set.setDuration(250).start();
    } else {
      ViewCompat.setScaleX(mFab, 1);
      ViewCompat.setScaleY(mFab, 1);
      ViewCompat.setAlpha(mFab, 1);
    }
  }

  @Override
  public void showRatingsLayout() {
    ratingsContainer.setVisibility(VISIBLE);
  }

  @Override
  public void showSectionTracked(boolean sectionIsAdded, boolean shouldAnimateView) {
    final int COLOR = ContextCompat.getColor(getParentActivity(), R.color.accent);
    final int COLOR_DARK = ContextCompat.getColor(getParentActivity(), R.color.accent_dark);
    final int ROTATION_NORMAL = 0;
    final int ROTATION_ADDED = 225;
    final int DURATION = 500;

    if (shouldAnimateView) {
      if (sectionIsAdded != mViewState.isSectionAdded) {
        ViewCompat.animate(mFab).setDuration(DURATION).setInterpolator(new DecelerateInterpolator())
            .rotation(sectionIsAdded ? ROTATION_ADDED : ROTATION_NORMAL);
        //I would much prefer to animate from the current coolor to the next but the fab has
        // no method to get the current color and I'm not desparate enough to manage it myself.
        // As for now, the fab will only animate on user click. Not from a db update.
        ValueAnimator colorAnim = ObjectAnimator.ofInt(this, "backgroundColor",
            sectionIsAdded ? COLOR : COLOR_DARK,
            sectionIsAdded ? COLOR_DARK : COLOR
        );
        colorAnim.setDuration(500);
        colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animation) {
            if (mFab != null) {
              mFab.setBackgroundTintList(ColorStateList.valueOf((Integer) animation.getAnimatedValue()));
            }
          }
        });
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.start();
      }
    } else {
      //Using ViewCompat to set the tint list is bugged on pre lollipop.
      mFab.setBackgroundTintList(ColorStateList.valueOf(sectionIsAdded ? COLOR_DARK : COLOR));
      ViewCompat.setRotation(mFab, sectionIsAdded ? ROTATION_ADDED : ROTATION_NORMAL);
    }
    mViewState.isSectionAdded = sectionIsAdded;
  }

  @Override
  public String toString() {
    return TAG;
  }

  @OnClick(R.id.add_courses_fab)
  public void fabClick(View view) {
    getPresenter().toggleFab();
  }

  private void addRMPView(View view) {
    view.setAlpha(0);
    ViewCompat.animate(view).setStartDelay(200).alpha(1).start();

    //Onclick intercepts vertical scroll
        /*viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object url =  v.getTag();
                if (url != null) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse((String) url));
                    context.startActivity(i);
                }
            }
        });*/
    ratingsContainer.addView(view);
  }

  private SectionInfoPresenter getPresenter() {
    return (SectionInfoPresenter) mBasePresenter;
  }

  private void setCourseTitle() {
    mToolbar.setTitle("");
    mCourseTitleText.setText(searchFlow.getCourse().name);
  }

  private void setInstructors() {
    if (searchFlow.getSection().instructors.size() != 0) {
      mInstructorsText.setText(com.tevinjeffrey.rutgersct.data.uctapi.model.extensions.Utils.InstructorUtils
          .toString(searchFlow.getSection().instructors));
    } else {
      mInstructorsText.setVisibility(View.GONE);
    }
  }

  private void setSectionCredits() {
    String credits = searchFlow.getSection().credits;

    if (TextUtils.isEmpty(credits) || credits.equals("-1")) {
      mCreditsViewGroup.setVisibility(View.GONE);
    }
    mCreditsText.setText(credits);
  }

  private void setSectionIndex() {
    mIndexNumberText.setText(searchFlow.getSection().call_number);
  }

  private void setSectionNumber() {
    mSectionNumberText.setText(searchFlow.getSection().number);
  }

  private void setSemester() {
    mSemesterText.setText(com.tevinjeffrey.rutgersct.data.uctapi.model.extensions.Utils.SemesterUtils
        .readableString(searchFlow.semester));
  }

  private void setTimes() {
    LayoutInflater inflater = LayoutInflater.from(getParentActivity());

    //sort times so that Monday > Tuesday and Lecture > Recitation
    for (Meeting time : searchFlow.getSection().meetings) {

      View timeLayout = inflater.inflate(R.layout.section_item_time, mSectionTimeContainer, false);

      TextView dayText = ButterKnife.findById(timeLayout, R.id.section_item_time_info_day_text);
      TextView timeText =
          ButterKnife.findById(timeLayout, R.id.section_item_time_info_meeting_time_text);
      TextView meetingTimeText =
          ButterKnife.findById(timeLayout, R.id.section_item_time_info_meeting_type);

      if (TextUtils.isEmpty(time.day)) {
        dayText.setText(time.class_type);
      } else {
        dayText.setText(time.day);
      }

      if (!TextUtils.isEmpty(time.start_time)) {
        timeText.setText(time.start_time + " - " + time.end_time);
      }

      if (!TextUtils.isEmpty(time.room)) {
        String locationText = time.room;

        if (!TextUtils.isEmpty(time.class_type)) {
          locationText = locationText + "  " + time.class_type;
        }

        meetingTimeText.setText(locationText);
      }

      mSectionTimeContainer.addView(timeLayout);
    }
  }

  private void showSectionMetadata() {
    for (Metadata data : searchFlow.getSection().metadata) {
      ViewGroup metadata =
          (ViewGroup) LayoutInflater.from(getParentActivity()).inflate(R.layout.metadata, null);
      TextView title = ButterKnife.findById(metadata, R.id.metadata_title);
      TextView description = ButterKnife.findById(metadata, R.id.metadata_text);
      description.setMovementMethod(new LinkMovementMethod());
      title.setText(data.title);
      description.setText(data.content);
      sectionMetadataContainer.addView(metadata);
    }
  }
}