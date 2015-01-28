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
import android.support.v7.widget.Toolbar;
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
import com.tevinjeffrey.rutgerssoc.ui.SectionInfoFragment;
import com.tevinjeffrey.rutgerssoc.utils.CourseUtils;
import com.tevinjeffrey.rutgerssoc.utils.SectionUtils;
import com.tevinjeffrey.rutgerssoc.utils.UrlUtils;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CourseInfoAdapter {

    private final Activity context;
    private final Course course;
    private final View rootView;
    private Request request;

    @InjectView(R.id.courseTitle_text)
    TextView mCourseTitleText;
    @InjectView(R.id.subtitle)
    TextView mCreditsText;
    @InjectView(R.id.shortenedCourseInfo)
    TextView mShortenedCourseInfo;
    @InjectView(R.id.credits_title)
    TextView mCreditsTitle;
    @InjectView(R.id.openSections_text)
    TextView mOpenSectionsText;
    @InjectView(R.id.openSections_title)
    TextView mOpenSectionsTitle;
    @InjectView(R.id.totalSections_text)
    TextView mTotalSectionsText;
    @InjectView(R.id.course_header_container)
    RelativeLayout mCourseHeaderContainer;
    @InjectView(R.id.toolbar_header_info)
    Toolbar mToolbarHeaderInfo;
    @InjectView(R.id.sectionNotes_title)
    TextView mSectionNotesTitle;
    @InjectView(R.id.courseNotes_text)
    TextView mCourseNotesText;
    @InjectView(R.id.courseNotesContainer)
    RelativeLayout mCourseNotesContainer;
    @InjectView(R.id.sectionComments_title)
    TextView mSectionCommentsTitle;
    @InjectView(R.id.subjectNotes_text)
    TextView mSubjectNotesText;
    @InjectView(R.id.subjectNotesContainer)
    RelativeLayout mSubjectNotesContainer;
    @InjectView(R.id.prereq_title)
    TextView mPrereqTitle;
    @InjectView(R.id.prereq_text)
    TextView mPrereqText;
    @InjectView(R.id.prereqContainer)
    RelativeLayout mPrereqContainer;
    @InjectView(R.id.course_metadata)
    RelativeLayout mCourseMetadata;
    @InjectView(R.id.section_title)
    TextView mSectionTitle;
    @InjectView(R.id.section_metadata_container)
    RelativeLayout mSectionMetadataContainer;
    @InjectView(R.id.sections_container)
    LinearLayout mSectionsContainer;

    private LayoutInflater inflater;

    private CourseInfoAdapter(Activity context, Course item, View rootView) {
        this.context = context;
        this.course = item;
        this.rootView = rootView;
    }

    public CourseInfoAdapter(Activity context, Course course, View rootView, Request request) {
        this(context, course, rootView);
        this.request = request;
    }

    public CourseInfoAdapter init() {
        ButterKnife.inject(this, rootView);
        setData();
        return this;
    }

    private void setData() {
        setCourseNotes(course);
        setCourseTitle(course);
        setCredits(course);
        setSubjectNotes(course);
        setShortenedCourseInfo(course);
        setOpenSections(course);
        setTotalSections(course);
        setPreReqNotes(course);
        setSections(course.getSections());
    }

    void setCourseTitle(Course course) {
        mCourseTitleText.setText(CourseUtils.getTitle(course));
    }

    void setShortenedCourseInfo(Course course) {
        mShortenedCourseInfo.setText(formatShortenedCourseInfo(course));
    }

    String formatShortenedCourseInfo(Course course) {
        String offeringUnitCode = course.getOfferingUnitCode();
        String subject = course.getSubject();
        String courseNumber = course.getCourseNumber();
        return offeringUnitCode + ":" + subject + ":" + courseNumber;
    }

    void setCredits(Course course) {
        mCreditsText.setText(String.valueOf(course.getCredits()));
    }

    void setCourseNotes(Course course) {
        if (course.getCourseNotes() == null) {
            mCourseNotesContainer.setVisibility(View.GONE);
        } else {
            mCourseNotesText.setText(Html.fromHtml(course.getCourseNotes()));
        }
    }

    void setSubjectNotes(Course course) {
        if (course.getSubjectNotes() == null) {
            mSubjectNotesContainer.setVisibility(View.GONE);
        } else {
            mSubjectNotesText.setText(Html.fromHtml(course.getSubjectNotes()));
        }
    }

    void setOpenSections(Course course) {
        mOpenSectionsText.setText(String.valueOf(course.getOpenSections()));
    }

    void setTotalSections(Course course) {
        mTotalSectionsText.setText(String.valueOf(course.getSectionsTotal()));
    }

    void setPreReqNotes(Course course) {
        if (course.getPreReqNotes() == null) {
            mPrereqContainer.setVisibility(View.GONE);
        } else {
            mPrereqText.setText(Html.fromHtml(course.getPreReqNotes()));
        }
    }

    void setSections(List<Course.Sections> sections) {
        new SectionAdapter(sections).init();
    }

    public class SectionAdapter {

        TextView mProfText;
        TextView mSectionNumber;
        RelativeLayout mSectionNumberBackground;
        LinearLayout mSectionTimeContainer;

        private View sectionLayout;
        private final List<Course.Sections> sectionData;

        public SectionAdapter(List<Course.Sections> sectionData) {
            this.sectionData = sectionData;
        }

        public void init() {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            sectionLayout = inflater.inflate(R.layout.section_layout, null);
            mProfText = ButterKnife.findById(sectionLayout, R.id.prof_text);
            mSectionNumber = ButterKnife.findById(sectionLayout, R.id.sectionNumber);
            mSectionNumberBackground = ButterKnife.findById(sectionLayout, R.id.sectionNumberBackground);
            mSectionTimeContainer = ButterKnife.findById(sectionLayout, R.id.sectionTimeContainer);
            RelativeLayout mSectionRoot = ButterKnife.findById(sectionLayout, R.id.sectionRoot);

            setOpenStatus(s);
            setSectionNumber(s);
            setInstructors(s);
            setTimes(s);
            setOnClickSectionClickListener();
            //setSectionBackground(s, mSectionRoot);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setTransitions();
            }

            sectionLayout.setTag(new Request(request.getSubject(), request.getSemester(),
                    request.getLocations(), request.getLevels(), s.getIndex()));
            mSectionsContainer.addView(sectionLayout);
        }
/*
        private void setSectionBackground(Course.Sections section, View sectionRoot) {
            if (section.hasMetaData()) {
                sectionRoot.setBackgroundColor(context.getResources()
                        .getColor(R.color.section_meta_bg));
            }
        }*/

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
                    context.getFragmentManager().beginTransaction();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                sectionInfoFragment.setEnterTransition(new AutoTransition());
                sectionInfoFragment.setReturnTransition(new AutoTransition());
                //sectionInfoFragment.setEnterTransition(new AutoTransition());

                sectionInfoFragment.setExitTransition(new AutoTransition());
                sectionInfoFragment.setSharedElementEnterTransition(new ChangeBounds());
                sectionInfoFragment.setSharedElementReturnTransition(new ChangeBounds());

                ft.addSharedElement(mCourseTitleText, "course_title");
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
            bundle.putParcelable("request", parcelable);
            return bundle;
        }


        @InjectView(R.id.day_text)
        TextView mDayText;

        @InjectView(R.id.sectionLocation_text)
        TextView mSectionLocationText;

        @InjectView(R.id.time_text)
        TextView mTimeText;

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
                    mSectionLocationText.setText(Html.fromHtml(""));
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
                sb.append(UrlUtils.trimTrailingChar(i.getName(), ';'));
                sb.append("; ");
            }
            mProfText.setText(sb.toString());
        }

        public void setOpenStatus(Course.Sections s) {
            if (s.isOpenStatus()) {
                mSectionNumberBackground.setBackgroundColor(context.getResources().getColor(R.color.green));
            } else {
                mSectionNumberBackground.setBackgroundColor(context.getResources().getColor(R.color.red));
            }
        }
    }
}
