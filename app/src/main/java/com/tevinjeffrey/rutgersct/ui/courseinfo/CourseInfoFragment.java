package com.tevinjeffrey.rutgersct.ui.courseinfo;

import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.ui.course.CourseView;
import com.tevinjeffrey.rutgersct.ui.utils.ItemClickListener;
import com.tevinjeffrey.rutgersct.ui.utils.CircleSharedElementCallback;
import com.tevinjeffrey.rutgersct.ui.utils.CircleView;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course.Section;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Subject;
import com.tevinjeffrey.rutgersct.ui.base.MVPFragment;
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoFragment;
import com.tevinjeffrey.rutgersct.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icicle;
import timber.log.Timber;

@SuppressWarnings("ClassWithTooManyMethods")
public class CourseInfoFragment extends MVPFragment implements CourseInfoView, ItemClickListener<Section, View> {

    private static final String TAG = CourseInfoFragment.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.course_info_list)
    RecyclerView mRecyclerView;

    @Bind(R.id.course_title_text)
    TextView mCourseTitleText;

    @Bind(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;

    @Bind(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @Bind(R.id.subtitle)
    TextView mCreditsText;

    @Bind(R.id.shortenedCourseInfo)
    TextView mShortenedCourseInfo;

    @Bind(R.id.openSections_text)
    TextView mOpenSectionsText;

    @Bind(R.id.totalSections_text)
    TextView mTotalSectionsText;

    @Icicle
    Request mRequest;

    @Icicle
    Course mSelectedCourse;

    @Icicle
    CourseInfoViewState mViewState = new CourseInfoViewState();

    private List<View> mHeaderViews = new ArrayList<>();

    public CourseInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mSelectedCourse = getArguments().getParcelable(CourseView.SELECTED_COURSE);
            if (mSelectedCourse != null) {
                mRequest = mSelectedCourse.getRequest();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater themedInflator = inflater.cloneInContext(Utils.wrapContextTheme(getActivity(),
                R.style.RutgersCT_Accent));
        final View rootView = themedInflator.inflate(R.layout.fragment_course_info, container, false);
        ButterKnife.bind(this, rootView);
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
        inflater.inflate(R.menu.menu_fragment_info, menu);
    }


    @Override
    public void onItemClicked(Section section, View view) {
        Timber.i("Selected section: %s", section);
        setIndexInRequestObject(section.getIndex());
        Bundle bundle = new Bundle();
        bundle.putParcelable(SELECTED_SECTION, section);
        startSectionInfoFragment(bundle, view);
    }

    public void initViews() {
        setCourseTitle();
        setCredits();
        setShortenedCourseInfo();
        setOpenSections();
        setTotalSections();
    }

    public void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getParentActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        if (mRecyclerView.getAdapter() == null) {
            mRecyclerView.setAdapter(new CourseInfoFragmentAdapter(mHeaderViews,
                    mSelectedCourse.getSections(), this));
        }
    }

    public void initHeaderView() {
        View courseMetadata = createCourseMetaDataView();
        mHeaderViews.add(courseMetadata);
    }

    private void setCourseTitle() {
        mCourseTitleText.setText(mSelectedCourse.getTrueTitle());
    }

    private void setShortenedCourseInfo() {
        //String offeringUnitCode = mSelectedCourse.getOfferingUnitCode();

        Subject subject = mSelectedCourse.getEnclosingSubject();
        String courseNumber = mSelectedCourse.getCourseNumber();
        if (subject != null) {
            String shortenedCourseInfo = subject.getTitle() + " › " + courseNumber;
            mShortenedCourseInfo.setText(shortenedCourseInfo);
        } else {
            Timber.i("http://crashes.to/s/0397fd79332 Selected course: %s\n" +
                    " Course number: %s\n" +
                    " Request: %s", mSelectedCourse, courseNumber, mSelectedCourse.getRequest());
        }
    }

    private void setCredits() {
        mCreditsText.setText(String.valueOf(mSelectedCourse.getCredits()));
    }

    private void setOpenSections() {
        mOpenSectionsText.setText(String.valueOf(mSelectedCourse.getOpenSections()));
    }

    private void setTotalSections() {
        mTotalSectionsText.setText(String.valueOf(mSelectedCourse.getSectionsTotal()));
    }

    private View createCourseMetaDataView() {
        ViewGroup root = (ViewGroup) getParentActivity().getLayoutInflater().inflate(R.layout.course_info_metadata, null);
        setPreReqNotes(root, mSelectedCourse);
        setCourseNotes(root, mSelectedCourse);
        setSubjectNotes(root, mSelectedCourse);
        return root;
    }

    private void setPreReqNotes(View root, Course course) {
        TextView prereqText = ButterKnife.findById(root, R.id.course_prereq_text);
        RelativeLayout prereqContainer = ButterKnife.findById(root, R.id.course_prereq_layout);

        if (course.getPreReqNotes() == null) {
            prereqContainer.setVisibility(View.GONE);
        } else {
            prereqText.setText(Html.fromHtml(course.getPreReqNotes()));
            prereqText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private void setCourseNotes(View root, Course course) {
        TextView courseNotesText = ButterKnife.findById(root, R.id.course_notes_text);
        RelativeLayout courseNotesContainer = ButterKnife.findById(root, R.id.course_notes_layout);

        if (course.getCourseNotes() == null) {
            courseNotesContainer.setVisibility(View.GONE);
        } else {
            courseNotesText.setText(Html.fromHtml(course.getCourseNotes()));
            courseNotesText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private void setSubjectNotes(View root, Course course) {
        TextView subjectNotesText = ButterKnife.findById(root, R.id.course_subject_notes_text);
        RelativeLayout subjectNotesContainer = ButterKnife.findById(root, R.id.course_subject_notes_layout);

        if (course.getSubjectNotes() == null) {
            subjectNotesContainer.setVisibility(View.GONE);
        } else {
            subjectNotesText.setText(Html.fromHtml(course.getSubjectNotes()));
            subjectNotesText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private void setIndexInRequestObject(String index) {
        mRequest.setIndex(index);
    }

    private void startSectionInfoFragment(Bundle b, View clickedView) {
        SectionInfoFragment sectionInfoFragment = new SectionInfoFragment();

        FragmentTransaction ft =
                this.getFragmentManager().beginTransaction();

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
            ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        }

        sectionInfoFragment.setArguments(b);
        startFragment(this, sectionInfoFragment, ft);
    }

    @Override
    public String toString() {
        return TAG;
    }

    @Override
    public void initToolbar() {
        setToolbar(mToolbar);
        getParentActivity().getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}