package com.tevinjeffrey.rutgersct.ui.courseinfo

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoViewHolder
import com.tevinjeffrey.rutgersct.ui.utils.CircleView

class CourseInfoViewHolder private constructor(
    parent: View,
    instructors: TextView,
    sectionNumberBackground: CircleView,
    mSectionTimeContainer: ViewGroup)
  : SectionInfoViewHolder(parent, instructors, sectionNumberBackground, mSectionTimeContainer) {
  companion object {

    fun newInstance(parent: View): CourseInfoViewHolder {
      val sectionInfoVH = SectionInfoViewHolder.newInstance(parent)
      val instructors = sectionInfoVH.instructors
      val sectionNumberBackground = sectionInfoVH.sectionNumberBackground
      val sectionTimeContainer = sectionInfoVH.sectionTimeContainer

      return CourseInfoViewHolder(parent, instructors, sectionNumberBackground, sectionTimeContainer)
    }
  }
}
