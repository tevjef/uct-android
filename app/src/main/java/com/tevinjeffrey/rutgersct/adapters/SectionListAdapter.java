package com.tevinjeffrey.rutgersct.adapters;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.animator.EaseOutQuint;
import com.tevinjeffrey.rutgersct.model.Course;
import com.tevinjeffrey.rutgersct.model.Request;
import com.tevinjeffrey.rutgersct.ui.MainActivity;
import com.tevinjeffrey.rutgersct.ui.MainFragment;
import com.tevinjeffrey.rutgersct.ui.SectionInfoFragment;
import com.tevinjeffrey.rutgersct.utils.SectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SectionListAdapter {

    private final MainActivity mContext;
    private final String mInflationType;
    private final Request mRequest;
    private final View rootView;
    private final MainFragment mCallingFragment;
    private final Course mCourse;
    private final List<Course.Sections> sectionData;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.day_text)
    TextView mDayText;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.sectionLocation_text)
    TextView mSectionLocationText;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.time_text)
    TextView mTimeText;
    private LinearLayout mSectionsContainer;
    private Toolbar mToolbar;
    private TextView mInstructors;
    private TextView mSectionNumber;
    private TextView mCourseTitleText;
    private RelativeLayout mSectionNumberBackground;
    private LinearLayout mSectionTimeContainer;
    private LayoutInflater inflater;
    private View sectionLayout;
    private FloatingActionButton mFab;

    public SectionListAdapter(MainFragment callingFragment, Course course, View rootView, Request request, String inflationType) {
        this.sectionData = course.getSections();
        this.mCallingFragment = callingFragment;
        this.mCourse = course;
        this.mInflationType = inflationType;
        this.mRequest = request;
        this.rootView = rootView;
        this.mContext = callingFragment.getParentActivity();
    }

    public void init() {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (Course.Sections s : sectionData) {
            setData(s);
        }
    }

    void setData(Course.Sections s) {
        // remove classes with a stopPint of 0. These represent some kind of hidden class taught
        // by STAFF. Though the obvious solution is to loop through the list and on some condition,
        // remove the class, this results in a ConcurrentModificationException.
        // Update: Uncommented because it produced unexpected behaviour in the types of classes.
        // Some classes are real classes even though the stopPoint is 0
        /*List<Course.Sections> toRemove = new ArrayList<>();
        for(Course.Sections s: sections) {
            if(s.getStopPoint() == 0) {
                toRemove.add(s);
            }
        }
        sections.removeAll(toRemove);*/
        sectionLayout = inflateSectionLayout();
        initValues();

        setCourseTitle();
        setOpenStatus(s);
        setSectionNumber(s);
        setInstructors(s);
        setTimes(s);

        sectionLayout.setTag(new Request(mRequest.getSubject(), mRequest.getSemester(),
                mRequest.getLocations(), mRequest.getLevels(), s.getIndex()));


        setOnClickSectionClickListener();


        mSectionsContainer.addView(sectionLayout);
    }

    private void setCourseTitle() {
        if (mCourseTitleText != null && mInflationType.equals(MainActivity.TRACKED_SECTION)) {
            mCourseTitleText.setText(mCourse.getSubject() + ":" + mCourse.getTrueTitle());
        }
    }

    private void initValues() {
        mInstructors = ButterKnife.findById(sectionLayout, R.id.prof_text);
        mSectionNumber = ButterKnife.findById(sectionLayout, R.id.sectionNumber);
        mSectionNumberBackground = ButterKnife.findById(sectionLayout, R.id.sectionNumberBackground);
        mSectionTimeContainer = ButterKnife.findById(sectionLayout, R.id.sectionTimeContainer);

        mSectionsContainer = ButterKnife.findById(rootView, R.id.sectionsContainer);
        mToolbar = ButterKnife.findById(rootView, R.id.toolbar);
        mFab = ButterKnife.findById(rootView, R.id.fab);

        if (mInflationType.equals(MainActivity.TRACKED_SECTION)) {
            mCourseTitleText = ButterKnife.findById(sectionLayout, R.id.courseTitle_text);
        } else {
            mCourseTitleText = ButterKnife.findById(rootView, R.id.courseTitle_text);
        }
    }

    private View inflateSectionLayout() {
        switch (mInflationType) {
            case MainActivity.COURSE_INFO_SECTION:
                return inflater.inflate(R.layout.section_layout, mSectionsContainer, false);
            case MainActivity.TRACKED_SECTION:
                return inflater.inflate(R.layout.full_section_layout, mSectionsContainer, false);
            default:
                return inflater.inflate(R.layout.section_layout, mSectionsContainer, false);
        }
    }

    private void setOnClickSectionClickListener() {
        sectionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragment(createArgs((Parcelable) v.getTag()), v);
            }
        });
    }

    private void createFragment(Bundle b, View v) {
        SectionInfoFragment sectionInfoFragment = new SectionInfoFragment();
        @SuppressLint("CommitTransaction") FragmentTransaction ft =
                mContext.getFragmentManager().beginTransaction();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sectionInfoFragment.setEnterTransition(new Fade(Fade.IN).excludeTarget(ImageView.class, true));
            sectionInfoFragment.setExitTransition(new Fade(Fade.OUT).excludeTarget(ImageView.class, true));
            sectionInfoFragment.setReenterTransition(new Fade(Fade.IN).excludeTarget(ImageView.class, true));
            sectionInfoFragment.setReturnTransition(new Fade(Fade.OUT).excludeTarget(ImageView.class, true));
            sectionInfoFragment.setAllowReturnTransitionOverlap(true);
            sectionInfoFragment.setAllowEnterTransitionOverlap(true);
            sectionInfoFragment.setSharedElementEnterTransition(new ChangeBounds().setInterpolator(new EaseOutQuint()));
            sectionInfoFragment.setSharedElementReturnTransition(new ChangeBounds().setInterpolator(new EaseOutQuint()));

            if (mFab != null)
                ft.addSharedElement(mFab, "fab");

            ft.addSharedElement(mToolbar, "toolbar_background");
            ft.addSharedElement(mCourseTitleText, "course_title");
            ft.addSharedElement(mSectionNumberBackground, "section_background");
//            ft.addSharedElement(mInstructors, "instructor_name");
            //ft.addSharedElement(credits, "credit_number");

        }
        sectionInfoFragment.setArguments(b);
        ft.replace(R.id.container, sectionInfoFragment).addToBackStack(mCallingFragment.toString())
                .commit();
    }

    private Bundle createArgs(Parcelable parcelable) {
        Bundle bundle = new Bundle();
        ArrayList<Course> c = new ArrayList<>();
        c.add(mCourse);
        bundle.putParcelableArrayList(MainActivity.COURSE_LIST, c);
        bundle.putParcelable(MainActivity.REQUEST, parcelable);
        return bundle;
    }

    void setTimes(Course.Sections s) {
        //sort times so that Monday > Tuesday and Lecture > Recitation
        Collections.sort(s.getMeetingTimes());
        for (Course.Sections.MeetingTimes time : s.getMeetingTimes()) {
            @SuppressLint("InflateParams")
            View timeLayout =
                    inflater.inflate(R.layout.time, null);

            ButterKnife.inject(this, timeLayout);

            if (time.isByArrangement()) {
                mDayText.setText(SectionUtils.getMeetingDayName(time));
                mTimeText.setText(SectionUtils.getMeetingHours(time));
                mSectionLocationText.setText("");
            } else {
                mDayText.setText(SectionUtils.getMeetingDayName(time));
                mTimeText.setText(SectionUtils.getMeetingHours(time));
                mSectionLocationText.setText(SectionUtils.getClassLocation(time));
            }
            mSectionTimeContainer.addView(timeLayout);
        }
    }

    private void setSectionNumber(Course.Sections s) {
        mSectionNumber.setText(s.getNumber());
    }

    private void setInstructors(Course.Sections s) {
        mInstructors.setText(s.getToStringInstructors(" | "));
    }

    void setOpenStatus(Course.Sections s) {
        if (s.isOpenStatus()) {
            mSectionNumberBackground.setBackgroundColor(mContext.getResources().getColor(R.color.green));
        } else {
            mSectionNumberBackground.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        }
    }
}