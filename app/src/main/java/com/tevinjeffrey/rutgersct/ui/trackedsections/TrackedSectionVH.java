package com.tevinjeffrey.rutgersct.ui.trackedsections;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoVH;
import com.tevinjeffrey.rutgersct.ui.utils.CircleView;

public final class TrackedSectionVH extends SectionInfoVH {

  private final TextView mCourseTitleText;

  private TrackedSectionVH(
      View parent,
      TextView instructors,
      TextView courseTitleText,
      CircleView sectionNumberBackground,
      ViewGroup mSectionTimeContainer) {
    super(parent, instructors, sectionNumberBackground, mSectionTimeContainer);
    this.mCourseTitleText = courseTitleText;
  }

  public static TrackedSectionVH newInstance(View parent) {
    SectionInfoVH sectionInfoVH = SectionInfoVH.newInstance(parent);
    TextView courseTitleText = ButterKnife.findById(parent, R.id.course_title_text);
    TextView instructors = sectionInfoVH.mInstructors;
    CircleView sectionNumberBackground = sectionInfoVH.mSectionNumberBackground;
    ViewGroup sectionTimeContainer = sectionInfoVH.mSectionTimeContainer;
    return new TrackedSectionVH(
        parent,
        instructors,
        courseTitleText,
        sectionNumberBackground,
        sectionTimeContainer
    );
  }

  public void setCourseTitle(String subjectNumber, String courseName) {
    mCourseTitleText.setText(subjectNumber + " | " + courseName);
  }
}
