package com.tevinjeffrey.rutgersct.ui;

import android.app.FragmentTransaction;
import android.app.SharedElementCallback;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
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
import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.adapters.CourseInfoFragmentAdapter;
import com.tevinjeffrey.rutgersct.customviews.CircleView;
import com.tevinjeffrey.rutgersct.rutgersapi.RutgersApiImpl;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import icepick.Icicle;

public class CourseInfoFragment extends BaseFragment implements CourseInfoFragmentAdapter.ItemClickListener {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.course_info_list)
    RecyclerView mRecyclerView;

    @InjectView(R.id.course_title_text)
    TextView mCourseTitleText;

    @InjectView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;

    @InjectView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @InjectView(R.id.subtitle)
    TextView mCreditsText;
    @InjectView(R.id.shortenedCourseInfo)
    TextView mShortenedCourseInfo;
    @InjectView(R.id.openSections_text)
    TextView mOpenSectionsText;
    @InjectView(R.id.totalSections_text)
    TextView mTotalSectionsText;

    @Icicle
    Request mRequest;

    private List<View> headerViews = new ArrayList<>();

    @Icicle
    Course mSelectedCourse;

    private WeakReference<View> tempView;

    public CourseInfoFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSelectedCourse = getArguments().getParcelable(RutgersCTApp.SELECTED_COURSE);
            mRequest = getArguments().getParcelable(RutgersCTApp.REQUEST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.setAccentWindow(getParentActivity());
        setRetainInstance(true);

        final View rootView = inflater.inflate(R.layout.fragment_course_info, container, false);

        ButterKnife.inject(this, rootView);

        initViews();

        return rootView;
    }

    @Override
    public void itemClicked(Course course, Course.Sections section, View view, int positon) {
        setIndexInRequestObject(section.getIndex());
        Bundle bundle = new Bundle();
        ArrayList<Course> c = new ArrayList<>();
        c.add(course);
        bundle.putParcelableArrayList(RutgersCTApp.COURSE_LIST, c);
        bundle.putParcelable(RutgersCTApp.REQUEST, mRequest);

        startSectionInfoFragment(bundle, view);
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


            //toolbar.setTransitionName(getString(R.string.transition_name_tool_background));
            //ft.addSharedElement(toolbar, getString(R.string.transition_name_tool_background));

            Transition sharedElementsEnter = TransitionInflater
                    .from(getParentActivity()).inflateTransition(R.transition.cif_shared_element_enter);

            Transition sharedElementsReturn = TransitionInflater
                    .from(getParentActivity()).inflateTransition(R.transition.cif_shared_element_return);

            sectionInfoFragment.setSharedElementEnterTransition(sharedElementsEnter);
            sectionInfoFragment.setSharedElementReturnTransition(sharedElementsReturn);

            sectionInfoFragment.setEnterSharedElementCallback(new CircleSharedElementCallback());
            sharedElementsEnter.addListener(new SharedElementsEnterTransitionCallback());


            //if the course title is visible to the user within the window, add the the course title to the shared elements

            /*Rect window = new Rect();
            getView().getHitRect(window);
            if(mCourseTitleText.getLocalVisibleRect(window))
            ft.addSharedElement(mCourseTitleText, mCourseTitleText.getTransitionName());*/

        } else {
            ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        }
        sectionInfoFragment.setArguments(b);
        ft.replace(R.id.container, sectionInfoFragment).addToBackStack(this.toString())
                .commit();
    }

    private void initViews() {
        setToolbar(toolbar);

        initRecyclerView();

        setCourseTitle();
        setCredits();
        setShortenedCourseInfo();
        setOpenSections();
        setTotalSections();
    }

    public void setToolbar(Toolbar toolbar) {
        super.setToolbar(toolbar);
        ActionBar actionBar = getParentActivity().getSupportActionBar();
        if(actionBar != null) {
            getParentActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getParentActivity().getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void initRecyclerView() {
        View courseMetadata = createCourseMetaDataView();
        headerViews.add(courseMetadata);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getParentActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.Adapter mAdapter = new CourseInfoFragmentAdapter(headerViews, mSelectedCourse, CourseInfoFragment.this);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RutgersCTApp.REQUEST, mRequest);
        outState.putParcelableArrayList(RutgersCTApp.COURSE_LIST, getArguments().getParcelableArrayList(RutgersCTApp.COURSE_LIST));
        outState.putParcelableArrayList(RutgersCTApp.SUBJECTS_LIST, getArguments().getParcelableArrayList(RutgersCTApp.SUBJECTS_LIST));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_info, menu);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    void setCourseTitle() {
        mCourseTitleText.setText(mSelectedCourse.getTrueTitle());
    }

    void setShortenedCourseInfo() {
        //String offeringUnitCode = mSelectedCourse.getOfferingUnitCode();
        String subject = mSelectedCourse.getSubject();
        String courseNumber = mSelectedCourse.getCourseNumber();
        String shortenedCourseInfo =  RutgersApiImpl.getSubjectFromJson(subject) + " › " + courseNumber;

        mShortenedCourseInfo.setText(shortenedCourseInfo);
    }

    void setCredits() {
        mCreditsText.setText(String.valueOf(mSelectedCourse.getCredits()));
    }

    void setOpenSections() {
        mOpenSectionsText.setText(String.valueOf(mSelectedCourse.getOpenSections()));
    }

    void setTotalSections() {
        mTotalSectionsText.setText(String.valueOf(mSelectedCourse.getSectionsTotal()));
    }

    View createCourseMetaDataView() {
        ViewGroup root = (ViewGroup) getParentActivity().getLayoutInflater().inflate(R.layout.course_info_metadata, null);
        setPreReqNotes(root, mSelectedCourse);
        setCourseNotes(root, mSelectedCourse);
        setSubjectNotes(root, mSelectedCourse);
        return root;
    }

    void setPreReqNotes(View root, Course course) {
        TextView prereqText = ButterKnife.findById(root, R.id.course_prereq_text);
        RelativeLayout prereqContainer = ButterKnife.findById(root, R.id.course_prereq_layout);

        if (course.getPreReqNotes() == null) {
            prereqContainer.setVisibility(View.GONE);
        } else {
            prereqText.setText(Html.fromHtml(course.getPreReqNotes()));
            prereqText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    void setCourseNotes(View root, Course course) {
        TextView courseNotesText = ButterKnife.findById(root, R.id.course_notes_text);
        RelativeLayout courseNotesContainer = ButterKnife.findById(root, R.id.course_notes_layout);

        if (course.getCourseNotes() == null) {
            courseNotesContainer.setVisibility(View.GONE);
        } else {
            courseNotesText.setText(Html.fromHtml(course.getCourseNotes()));
            courseNotesText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    void setSubjectNotes(View root, Course course) {
        TextView subjectNotesText = ButterKnife.findById(root, R.id.course_subject_notes_text);
        RelativeLayout subjectNotesContainer = ButterKnife.findById(root, R.id.course_subject_notes_layout);

        if (course.getSubjectNotes() == null) {
            subjectNotesContainer.setVisibility(View.GONE);
        } else {
            subjectNotesText.setText(Html.fromHtml(course.getSubjectNotes()));
            subjectNotesText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private class SharedElementsEnterTransitionCallback implements Transition.TransitionListener {

        @Override
        public void onTransitionStart(Transition transition) {

        }

        @Override
        public void onTransitionEnd(Transition transition) {
            if (transition != null) {
                tempView.get().setAlpha(0);
            }
        }

        @Override
        public void onTransitionCancel(Transition transition) {

        }

        @Override
        public void onTransitionPause(Transition transition) {

        }

        @Override
        public void onTransitionResume(Transition transition) {

        }
    }

    public class CircleSharedElementCallback extends SharedElementCallback {

        CircleView mCircleViewSnapshot;

        @Override //capture
        public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
            super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
            //get desired view by tranisition name...guarenteed to be unique ;)

            for (View v : sharedElements) {
                if (v.getTransitionName().equals(RutgersCTApp.getInstance().getString(R.string.transition_name_circle_view)) && v instanceof CircleView) {
                    mCircleViewSnapshot = (CircleView) v;
                    mCircleViewSnapshot.setVisibility(View.INVISIBLE);
                }
            }

        }

        @Override //set
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            //It's possible the the framework was unable to map the view in the appering activity/fragment.

            //It's possible for the fragmment to not be attach to the activity. Calls to getResources will crash.
            if (mCircleViewSnapshot != null) {
                final View mappedFrameLayout = sharedElements.get(mCircleViewSnapshot.getTransitionName());

                CircleView hiddenCircleView = ButterKnife.findById(mappedFrameLayout, R.id.hidden_circle_view);

                hiddenCircleView.setVisibility(View.VISIBLE);
                hiddenCircleView.setBackgroundColor(mCircleViewSnapshot.getBackgroundColor());
                hiddenCircleView.setTitleText(mCircleViewSnapshot.getTitleText());

                tempView = new WeakReference<>(mappedFrameLayout);
            }
        }

    }

}