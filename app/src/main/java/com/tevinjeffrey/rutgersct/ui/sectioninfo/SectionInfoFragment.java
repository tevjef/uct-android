package com.tevinjeffrey.rutgersct.ui.sectioninfo;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.tevinjeffrey.rmp.common.Professor;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.data.rutgersapi.RetroRutgers;
import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.data.rutgersapi.utils.SectionUtils;
import com.tevinjeffrey.rutgersct.data.uctapi.search.SearchManager;
import com.tevinjeffrey.rutgersct.data.uctapi.search.UCTSubscription;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.ui.base.MVPFragment;
import com.tevinjeffrey.rutgersct.ui.courseinfo.CourseInfoView;
import com.tevinjeffrey.rutgersct.ui.utils.RatingLayoutInflater;
import com.tevinjeffrey.rutgersct.utils.Utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.State;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class SectionInfoFragment extends MVPFragment implements SectionInfoView {

    private static final String TAG = SectionInfoFragment.class.getSimpleName();

    @Bind(R.id.prof_ratings_container)
    ViewGroup ratingsContainer;

    @Bind(R.id.prof_ratings_root)
    ViewGroup ratingsRoot;

    @Bind(R.id.semesterText)
    TextView mSemesterText;

    @Bind(R.id.course_title_text)
    TextView mCourseTitleText;

    @Bind(R.id.sectionNumber_text)
    TextView mSectionNumberText;

    @Bind(R.id.indexNumber_text)
    TextView mIndexNumberText;

    @Bind(R.id.subtitle)
    TextView mCreditsText;

    @Bind(R.id.instructors_text)
    TextView mInstructorsText;

    @Bind(R.id.instructors_container)
    ViewGroup mInstructorsContainer;

    @Bind(R.id.exam_code_text)
    TextView mExamCodeText;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.section_notes_text)
    TextView mSectionNotesText;

    @Bind(R.id.section_notes_layout)
    RelativeLayout mSectionNotesLayout;


    @Bind(R.id.section_comments_text)
    TextView mSectionCommentsText;


    @Bind(R.id.section_comments_layout)
    RelativeLayout mSectionCommentsLayout;


    @Bind(R.id.section_crosslist_text)
    TextView mSectionCrossListText;


    @Bind(R.id.section_crosslist_layout)
    RelativeLayout mSectionCrossListLayout;


    @Bind(R.id.section_subtitle_text)
    TextView mSectionSubtitleText;


    @Bind(R.id.section_subtitle_layout)
    RelativeLayout mSectionSubtitleLayout;


    @Bind(R.id.section_permission_text)
    TextView mSectionPermissionText;


    @Bind(R.id.section_permission_layout)
    RelativeLayout mSectionPermissionLayout;


    @Bind(R.id.section_open_to_text)
    TextView mSectionOpenToText;


    @Bind(R.id.section_open_to_layout)
    RelativeLayout mSectionOpenLayout;


    @Bind(R.id.section_times_container)
    LinearLayout mSectionTimeContainer;


    @Bind(R.id.add_courses_fab)
    FloatingActionButton mFab;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @State
    Course.Section selectedSection;

    @State
    SectionInfoViewState mViewState = new SectionInfoViewState();

    @Inject
    RetroRutgers mRetroRutgers;

    @Inject
    DatabaseHandler mDatabaseHandler;

    @Inject
    Bus mBus;

    @Inject
    SearchManager searchManager;

    public SectionInfoFragment() {
    }

    @Override
    public void injectTargets() {
        RutgersCTApp.getObjectGraph(getParentActivity()).inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            selectedSection = getArguments().getParcelable(CourseInfoView.SELECTED_SECTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final Context contextThemeWrapper;
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        if (selectedSection.isOpenStatus()) {
            contextThemeWrapper = Utils.wrapContextTheme(getActivity(), R.style.RutgersCT_Green);
        } else {
            contextThemeWrapper = Utils.wrapContextTheme(getActivity(), R.style.RutgersCT_Red);
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
            mBasePresenter = new SectionInfoPresenterImpl(searchManager.getSearchFlow());
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

    @OnClick(R.id.add_courses_fab)
    public void fabClick(View view) {
        getPresenter().toggleFab();
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
                            if (mFab != null)
                                mFab.setBackgroundTintList(ColorStateList.valueOf((Integer) animation.getAnimatedValue()));

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
    public void showRatingsLayout() {
        ratingsContainer.setVisibility(VISIBLE);
    }

    @Override
    public void hideRatingsLoading() {
        ViewGroup progress = ButterKnife.findById(ratingsRoot, R.id.rmp_progressview);
        progress.setVisibility(GONE);
    }

    @Override
    public void addErrorProfessor(String name) {
        addRMPView(new RatingLayoutInflater(getParentActivity(), null)
                .getErrorLayout(name, selectedSection));
    }

    @Override
    public void addRMPProfessor(Professor professor) {
        RatingLayoutInflater ratingLayoutInflater =
                new RatingLayoutInflater(getParentActivity(), professor);
        addRMPView(ratingLayoutInflater.getProfessorLayout());
    }

    @Override
    public void initToolbar() {
        setToolbar(mToolbar);
        getParentActivity().getSupportActionBar().setDisplayShowTitleEnabled(false);
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

    public void initViews() {
        setSectionNumber();
        setSectionIndex();
        setSectionCredits();
        setCourseTitle();
        setSectionNotes();
        setSectionComments();
        setSectionOpenTo();
        setSectionPermission();
        setSectionCrossList();
        setSectionSubtitle();
        setExamCode();
        setTimes();
        setInstructors();
        setSemester();

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

    private void setSemester() {
        mSemesterText.setText(selectedSection.getRequest().getSemester().toString());
    }

    private void setSectionNumber() {
        mSectionNumberText.setText(selectedSection.getNumber());
    }

    private void setSectionIndex() {
        mIndexNumberText.setText(selectedSection.getIndex());
    }

    private void setSectionCredits() {
        mCreditsText.setText(String.valueOf(selectedSection.getCourse().getCredits()));
    }

    private void setCourseTitle() {
        mToolbar.setTitle("");
        mCourseTitleText.setText(selectedSection.getCourse().getTrueTitle());
    }

    private void setSectionNotes() {
        if (!selectedSection.hasNotes()) {
            mSectionNotesLayout.setVisibility(View.GONE);
        } else {
            mSectionNotesLayout.setVisibility(View.VISIBLE);
            mSectionNotesText.setText(Html.fromHtml(selectedSection.getSectionNotes()));
            mSectionNotesText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private void setSectionComments() {
        if (!selectedSection.hasComments()) {
            mSectionCommentsLayout.setVisibility(View.GONE);
        } else {
            mSectionCommentsLayout.setVisibility(View.VISIBLE);
            mSectionCommentsText.setText(Html.fromHtml(StringUtils.join(selectedSection.getComments(), ", ")));
            mSectionCommentsText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private void setSectionOpenTo() {
        if (!selectedSection.hasMajors()) {
            mSectionOpenLayout.setVisibility(View.GONE);
        } else {
            boolean isMajorHeaderSet = false;
            boolean isUnitHeaderSet = false;
            StringBuilder sb = new StringBuilder();
            for (Course.Section.Majors i : selectedSection.getMajors()) {
                if (i.isMajorCode()) {
                    if (!isMajorHeaderSet) {
                        isMajorHeaderSet = true;
                        sb.append("MAJORS: ");
                    }
                    sb.append(i.getCode());
                    sb.append(", ");
                } else if (i.isUnitCode()) {
                    if (!isUnitHeaderSet) {
                        isUnitHeaderSet = true;
                        sb.append("SCHOOLS: ");
                    }
                    sb.append(i.getCode());
                    sb.append(", ");
                }
            }
            mSectionOpenLayout.setVisibility(View.VISIBLE);
            mSectionOpenToText.setText(Html.fromHtml(sb.toString()));
        }
    }

    private void setSectionPermission() {
        if (!selectedSection.hasSpecialPermission()) {
            mSectionPermissionLayout.setVisibility(View.GONE);
        } else {
            mSectionPermissionLayout.setVisibility(View.VISIBLE);
            mSectionPermissionText.setText(Html.fromHtml(selectedSection.getSpecialPermissionAddCodeDescription()));
            mSectionPermissionText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private void setSectionCrossList() {
        if (!selectedSection.hasCrossListed()) {
            mSectionCrossListLayout.setVisibility(View.GONE);
        } else {
            StringBuilder sb = new StringBuilder();
            for (Course.Section.CrossListedSections i : selectedSection.getCrossListedSections()) {
                sb.append(i.getFullCrossListedSection());
                sb.append(", ");
            }
            mSectionCrossListLayout.setVisibility(View.VISIBLE);
            mSectionCrossListText.setText(Html.fromHtml(sb.toString()));
            mSectionCrossListText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private void setSectionSubtitle() {
        if (!selectedSection.hasSubtitle()) {
            mSectionSubtitleLayout.setVisibility(View.GONE);
        } else {
            mSectionSubtitleLayout.setVisibility(View.VISIBLE);
            mSectionSubtitleText.setText(Html.fromHtml(selectedSection.getSubtitle()));
            mSectionSubtitleText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private void setExamCode() {
        mExamCodeText.setText(String.valueOf(selectedSection.getExamCode()));
    }

    private void setTimes() {
        LayoutInflater inflater = LayoutInflater.from(getParentActivity());

        //sort times so that Monday > Tuesday and Lecture > Recitation
        Collections.sort(selectedSection.getMeetingTimes());
        for (Course.Section.MeetingTimes time : selectedSection.getMeetingTimes()) {

            View timeLayout = inflater.inflate(R.layout.section_item_time, mSectionTimeContainer, false);

            TextView dayText = ButterKnife.findById(timeLayout, R.id.section_item_time_info_day_text);
            TextView timeText = ButterKnife.findById(timeLayout, R.id.section_item_time_info_meeting_time_text);
            TextView locationText = ButterKnife.findById(timeLayout, R.id.section_item_time_info_location_text);
            TextView meetingTimeText = ButterKnife.findById(timeLayout, R.id.section_item_time_info_meeting_type);

            dayText.setText(SectionUtils.getMeetingDayName(time));
            timeText.setText(SectionUtils.getMeetingHours(time));
            locationText.setText(SectionUtils.getClassLocation(time));
            meetingTimeText.setText(time.getMeetingModeDesc());

            mSectionTimeContainer.addView(timeLayout);
        }
    }

    private void setInstructors() {
        if (selectedSection.getInstructors().size() != 0) {
            mInstructorsText.setText(selectedSection.getToStringInstructors(" | "));
        } else {
            mInstructorsContainer.setVisibility(View.GONE);
        }
    }

    private SectionInfoPresenter getPresenter() {
        return (SectionInfoPresenter) mBasePresenter;
    }

    public static SectionInfoFragment newInstance(UCTSubscription selectedSection) {
        final SectionInfoFragment newInstance = new SectionInfoFragment();

        final Bundle arguments = new Bundle();
        arguments.putParcelable(CourseInfoView.SELECTED_SECTION, selectedSection);

        newInstance.setArguments(arguments);
        return newInstance;
    }

    @Override
    public String toString() {
        return TAG;
    }
}