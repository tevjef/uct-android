package com.tevinjeffrey.rutgersct.adapters;

import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.model.Course;
import com.tevinjeffrey.rutgersct.model.Request;
import com.tevinjeffrey.rutgersct.ui.MainActivity;
import com.tevinjeffrey.rutgersct.ui.MainFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CourseInfoAdapter {

    private final MainActivity mContext;
    private final Course course;
    private final View rootView;
    private final MainFragment callingFragment;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.courseTitle_text)
    TextView mCourseTitleText;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.subtitle)
    TextView mCreditsText;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.shortenedCourseInfo)
    TextView mShortenedCourseInfo;
    @InjectView(R.id.credits_title)
    TextView mCreditsTitle;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.openSections_text)
    TextView mOpenSectionsText;
    @InjectView(R.id.openSections_title)
    TextView mOpenSectionsTitle;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.totalSections_text)
    TextView mTotalSectionsText;
    @InjectView(R.id.course_header_container)
    RelativeLayout mCourseHeaderContainer;
    @InjectView(R.id.toolbar)
    Toolbar mToolbarHeaderInfo;
    @InjectView(R.id.sectionNotes_title)
    TextView mSectionNotesTitle;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.courseNotes_text)
    TextView mCourseNotesText;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.courseNotesContainer)
    RelativeLayout mCourseNotesContainer;
    @InjectView(R.id.sectionComments_title)
    TextView mSectionCommentsTitle;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.subjectNotes_text)
    TextView mSubjectNotesText;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.subjectNotesContainer)
    RelativeLayout mSubjectNotesContainer;
    @InjectView(R.id.prereq_title)
    TextView mPrereqTitle;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.prereq_text)
    TextView mPrereqText;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.prereqContainer)
    RelativeLayout mPrereqContainer;
    @InjectView(R.id.course_metadata)
    RelativeLayout mCourseMetadata;
    @InjectView(R.id.section_title)
    TextView mSectionTitle;
    @InjectView(R.id.section_metadata_container)
    RelativeLayout mSectionMetadataContainer;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.sectionsContainer)
    LinearLayout mSectionsContainer;
    private Request request;

    private CourseInfoAdapter(MainFragment callingFragment, Course item, View rootView) {
        this.callingFragment = callingFragment;
        this.course = item;
        this.rootView = rootView;
        this.mContext = callingFragment.getParentActivity();
    }

    public CourseInfoAdapter(MainFragment callingFragment, Course course, View rootView, Request request) {
        this(callingFragment, course, rootView);
        this.request = request;
    }

    public void init() {
        ButterKnife.inject(this, rootView);
        setData();
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
        setSections(course);
    }

    void setCourseTitle(Course course) {
        mCourseTitleText.setText(course.getTrueTitle());
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
            mCourseNotesText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    void setSubjectNotes(Course course) {
        if (course.getSubjectNotes() == null) {
            mSubjectNotesContainer.setVisibility(View.GONE);
        } else {
            mSubjectNotesText.setText(Html.fromHtml(course.getSubjectNotes()));
            mSubjectNotesText.setMovementMethod(LinkMovementMethod.getInstance());
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
            mPrereqText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    void setSections(Course course) {
        new SectionListAdapter(callingFragment, course, rootView, request, MainActivity.COURSE_INFO_SECTION).init();
    }
}
