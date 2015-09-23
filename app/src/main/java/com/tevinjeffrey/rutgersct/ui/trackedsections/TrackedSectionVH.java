package com.tevinjeffrey.rutgersct.ui.trackedsections;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.ui.utils.CircleView;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoVH;

import butterknife.ButterKnife;

public final class TrackedSectionVH extends SectionInfoVH {

    private final TextView mCourseTitleText;

    public static TrackedSectionVH newInstance(View parent) {
        SectionInfoVH sectionInfoVH = SectionInfoVH.newInstance(parent);
        TextView courseTitleText = ButterKnife.findById(parent, R.id.course_title_text);
        TextView instructors = sectionInfoVH.mInstructors;
        CircleView sectionNumberBackground = sectionInfoVH.mSectionNumberBackground;
        ViewGroup sectionTimeContainer = sectionInfoVH.mSectionTimeContainer;
        return new TrackedSectionVH(parent, instructors, courseTitleText, sectionNumberBackground, sectionTimeContainer);
    }

    private TrackedSectionVH(View parent, TextView instructors, TextView courseTitleText, CircleView sectionNumberBackground, ViewGroup mSectionTimeContainer) {
        super(parent, instructors, sectionNumberBackground, mSectionTimeContainer);
        this.mCourseTitleText = courseTitleText;
    }

    public void setCourseTitle(Course course) {
        mCourseTitleText.setText(course.getSubject() + " | " + course.getTrueTitle());
    }
}
