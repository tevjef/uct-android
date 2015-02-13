package com.tevinjeffrey.rutgerssoc.adapters;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.CircularPropagation;
import android.transition.Fade;
import android.transition.SidePropagation;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.animator.EaseOutQuint;
import com.tevinjeffrey.rutgerssoc.model.Course;
import com.tevinjeffrey.rutgerssoc.model.Request;
import com.tevinjeffrey.rutgerssoc.ui.MainActivity;
import com.tevinjeffrey.rutgerssoc.ui.SectionInfoFragment;
import com.tevinjeffrey.rutgerssoc.utils.CourseUtils;
import com.tevinjeffrey.rutgerssoc.utils.SectionUtils;
import com.tevinjeffrey.rutgerssoc.utils.UrlUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SectionListAdapter {

    private final Activity mContext;
    private final String mInflationType;
    private final Request mRequest;
    private final View rootView;
    private LinearLayout mSectionsContainer;
    private Toolbar mToolbar;
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
    private TextView mProfText;
    private TextView mSectionNumber;
    private TextView mCourseTitleText;
    private RelativeLayout mSectionNumberBackground;
    private LinearLayout mSectionTimeContainer;
    private LayoutInflater inflater;
    private View sectionLayout;
    private FloatingActionButton mFab;

    public SectionListAdapter(Activity context, Course course, View rootView, Request request, String inflationType) {
        this.sectionData = course.getSections();
        this.mContext = context;
        this.mCourse = course;
        this.mInflationType = inflationType;
        this.mRequest = request;
        this.rootView = rootView;
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
        setOnClickSectionClickListener();
        //setSectionBackground(s, mSectionRoot);

        sectionLayout.setTag(new Request(mRequest.getSubject(), mRequest.getSemester(),
                mRequest.getLocations(), mRequest.getLevels(), s.getIndex()));

        mSectionsContainer.addView(sectionLayout);
    }
/*
        private void setSectionBackground(Course.Sections section, View sectionRoot) {
            if (section.hasMetaData()) {
                sectionRoot.setBackgroundColor(mContext.getResources()
                        .getColor(R.color.section_meta_bg));
            }
        }*/

    private void setCourseTitle() {
        if (mCourseTitleText != null && mInflationType.equals(MainActivity.TRACKED_SECTION)) {
            mCourseTitleText.setText(mCourse.getSubject() + ":" + mCourse.getCourseNumber() + ": " + CourseUtils.getTitle(mCourse));
        }
    }

    private void initValues() {
        mProfText = ButterKnife.findById(sectionLayout, R.id.prof_text);
        mSectionNumber = ButterKnife.findById(sectionLayout, R.id.sectionNumber);
        mSectionNumberBackground = ButterKnife.findById(sectionLayout, R.id.sectionNumberBackground);
        mSectionTimeContainer = ButterKnife.findById(sectionLayout, R.id.sectionTimeContainer);

        mSectionsContainer = ButterKnife.findById(rootView, R.id.sectionsContainer);
        mCourseTitleText = ButterKnife.findById(rootView, R.id.courseTitle_text);
        mToolbar = ButterKnife.findById(rootView, R.id.toolbar);
        mFab = ButterKnife.findById(rootView, R.id.fab);

        if (mInflationType.equals(MainActivity.TRACKED_SECTION)) {
            mCourseTitleText = ButterKnife.findById(sectionLayout, R.id.courseTitle_text);
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
        Fragment sectionInfoFragment = new SectionInfoFragment();
        @SuppressLint("CommitTransaction") FragmentTransaction ft =
                mContext.getFragmentManager().beginTransaction();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.RIGHT);
            slide.setInterpolator(new EaseOutQuint());


            sectionInfoFragment.setEnterTransition(slide);
            sectionInfoFragment.setReturnTransition(slide);
            //sectionInfoFragment.setEnterTransition(new AutoTransition());

            sectionInfoFragment.setAllowReturnTransitionOverlap(false);
            sectionInfoFragment.setAllowEnterTransitionOverlap(false);

            sectionInfoFragment.setExitTransition(new Fade(Fade.OUT));

            sectionInfoFragment.setSharedElementEnterTransition(new ChangeBounds().setInterpolator(new EaseOutQuint()));
            sectionInfoFragment.setSharedElementReturnTransition(new ChangeBounds().setInterpolator(new EaseOutQuint()));

            if(mFab != null)
            ft.addSharedElement(mFab, "fab");

            ft.addSharedElement(mToolbar, "toolbar_background");
            ft.addSharedElement(mCourseTitleText, "course_title");
            ft.addSharedElement(mSectionNumberBackground, "section_background");
//            ft.addSharedElement(mProfText, "instructor_name");
            //ft.addSharedElement(credits, "credit_number");

        }

        sectionInfoFragment.setArguments(b);
        ft.replace(R.id.container, sectionInfoFragment).addToBackStack(null)
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
                mDayText.setText(Html.fromHtml(SectionUtils.getMeetingDayName(time)));
                mTimeText.setText(Html.fromHtml(SectionUtils.getMeetingHours(time)));
                mSectionLocationText.setText("");
            } else {
                mDayText.setText(Html.fromHtml(SectionUtils.getMeetingDayName(time)));
                mTimeText.setText(Html.fromHtml(SectionUtils.getMeetingHours(time)));
                mSectionLocationText.setText(Html.fromHtml(SectionUtils.getClassLocation(time)));
            }
            mSectionTimeContainer.addView(timeLayout);
        }
    }

    private void setSectionNumber(Course.Sections s) {
        mSectionNumber.setText(s.getNumber());
    }

    private void setInstructors(Course.Sections s) {
        StringBuilder sb = new StringBuilder();
        for (Course.Sections.Instructors i : s.getInstructors()) {
            sb.append(i.getName());
            sb.append(" | ");
        }
        mProfText.setText(UrlUtils.trimTrailingChar(sb.toString(), '|'));
    }

    void setOpenStatus(Course.Sections s) {
        if (s.isOpenStatus()) {
            mSectionNumberBackground.setBackgroundColor(mContext.getResources().getColor(R.color.green));
        } else {
            mSectionNumberBackground.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        }
    }
}