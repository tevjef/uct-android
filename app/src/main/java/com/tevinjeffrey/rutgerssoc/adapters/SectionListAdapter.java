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
import android.text.Html;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tevinjeffrey.rutgerssoc.R;
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
    private final LinearLayout mSectionsContainer;
    private final Course mCourse;
    private final List<Course.Sections> sectionData;
    TextView mProfText;
    TextView mSectionNumber;
    TextView mCourseTitleText;
    RelativeLayout mSectionNumberBackground;
    LinearLayout mSectionTimeContainer;
    @InjectView(R.id.day_text)
    TextView mDayText;
    @InjectView(R.id.sectionLocation_text)
    TextView mSectionLocationText;
    @InjectView(R.id.time_text)
    TextView mTimeText;
    private LayoutInflater inflater;
    private View sectionLayout;

    public SectionListAdapter(Activity context, Course course, LinearLayout sectionsContainer, Request request, String inflationType) {
        this.sectionData = course.getSections();
        this.mContext = context;
        this.mCourse = course;
        this.mInflationType = inflationType;
        this.mRequest = request;
        this.mSectionsContainer = sectionsContainer;
    }

    public void init() {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (Course.Sections s : sectionData) {
            setData(s);
        }
    }

    public void setData(Course.Sections s) {
        // remove classes with a stopPint of 0. These represent some kind of hidden class taught
        // by STAFF. Though the obvious solution is to loop through the list and on some condition,
        // remove the class, this results in a ConcurrentModificationException.
        // Update: Uncommented because it produced unexpected behaviour in the types of classes.
        // Some classes are real classes evern though the stopPoint is 0
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTransitions();
        }

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
        if (mCourseTitleText != null) {
            mCourseTitleText.setText(mCourse.getSubject() + ":" + mCourse.getCourseNumber() + ": " + CourseUtils.getTitle(mCourse));
        }
    }

    private void initValues() {
        mProfText = ButterKnife.findById(sectionLayout, R.id.prof_text);
        mSectionNumber = ButterKnife.findById(sectionLayout, R.id.sectionNumber);
        mSectionNumberBackground = ButterKnife.findById(sectionLayout, R.id.sectionNumberBackground);
        mSectionTimeContainer = ButterKnife.findById(sectionLayout, R.id.sectionTimeContainer);

        if (mInflationType.equals(MainActivity.TRACKED_SECTION)) {
            mCourseTitleText = ButterKnife.findById(sectionLayout, R.id.courseTitle_text);
        }
    }

    private View inflateSectionLayout() {

        if (mInflationType.equals(MainActivity.COURSE_INFO_SECTION)) {
            return inflater.inflate(R.layout.section_layout, null);
        } else if (mInflationType.equals(MainActivity.TRACKED_SECTION)) {
            return inflater.inflate(R.layout.full_section_layout, null);
        } else return inflater.inflate(R.layout.section_layout, null);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setTransitions() {
        mSectionNumberBackground.setTransitionName("section_background");
        mProfText.setTransitionName("instructor_name");
    }

    private void setOnClickSectionClickListener() {
        sectionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragment(createArgs((Parcelable) v.getTag()));
            }
        });
    }

    private void createFragment(Bundle b) {
        Fragment sectionInfoFragment = new SectionInfoFragment();
        @SuppressLint("CommitTransaction") FragmentTransaction ft =
                mContext.getFragmentManager().beginTransaction();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sectionInfoFragment.setEnterTransition(new AutoTransition());
            sectionInfoFragment.setReturnTransition(new AutoTransition());
            //sectionInfoFragment.setEnterTransition(new AutoTransition());

            sectionInfoFragment.setExitTransition(new AutoTransition());
            sectionInfoFragment.setSharedElementEnterTransition(new ChangeBounds());
            sectionInfoFragment.setSharedElementReturnTransition(new ChangeBounds());

            ft.addSharedElement(mSectionNumberBackground, "section_background");
            ft.addSharedElement(mProfText, "instructor_name");
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

    public void setTimes(Course.Sections s) {
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

    public void setOpenStatus(Course.Sections s) {
        if (s.isOpenStatus()) {
            mSectionNumberBackground.setBackgroundColor(mContext.getResources().getColor(R.color.green));
        } else {
            mSectionNumberBackground.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        }
    }
}