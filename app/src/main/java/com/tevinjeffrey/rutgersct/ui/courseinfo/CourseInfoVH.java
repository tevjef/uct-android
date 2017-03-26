package com.tevinjeffrey.rutgersct.ui.courseinfo;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoVH;
import com.tevinjeffrey.rutgersct.ui.utils.CircleView;

public final class CourseInfoVH extends SectionInfoVH {

  public static CourseInfoVH newInstance(View parent) {
    SectionInfoVH sectionInfoVH = SectionInfoVH.newInstance(parent);
    TextView instructors = sectionInfoVH.mInstructors;
    CircleView sectionNumberBackground = sectionInfoVH.mSectionNumberBackground;
    ViewGroup sectionTimeContainer = sectionInfoVH.mSectionTimeContainer;

    return new CourseInfoVH(parent, instructors, sectionNumberBackground, sectionTimeContainer);
  }

  private CourseInfoVH(
      View parent,
      TextView instructors,
      CircleView sectionNumberBackground,
      ViewGroup mSectionTimeContainer) {
    super(parent, instructors, sectionNumberBackground, mSectionTimeContainer);
  }
}
