package com.tevinjeffrey.rutgersct.ui.courseinfo

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoVH
import com.tevinjeffrey.rutgersct.ui.utils.CircleView

class CourseInfoViewHolder private constructor(
    parent: View,
    instructors: TextView,
    sectionNumberBackground: CircleView,
    mSectionTimeContainer: ViewGroup) : SectionInfoVH(parent, instructors, sectionNumberBackground, mSectionTimeContainer) {
  companion object {

    fun newInstance(parent: View): CourseInfoViewHolder {
      val sectionInfoVH = SectionInfoVH.newInstance(parent)
      val instructors = sectionInfoVH.mInstructors
      val sectionNumberBackground = sectionInfoVH.mSectionNumberBackground
      val sectionTimeContainer = sectionInfoVH.mSectionTimeContainer

      return CourseInfoViewHolder(parent, instructors, sectionNumberBackground, sectionTimeContainer)
    }
  }
}
