package com.tevinjeffrey.rutgersct.ui.course

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.data.model.Course
import com.tevinjeffrey.rutgersct.data.model.extensions.Utils

class CourseViewHolder private constructor(
    private val parent: View,
    private val courseTitle: TextView,
    private val sectionInfo: TextView) : RecyclerView.ViewHolder(parent) {

  fun setCourseTitle(course: Course) {
    courseTitle.text = course.number + ":  " + course.name
  }

  fun setOnClickListener(listener: View.OnClickListener) {
    parent.setOnClickListener(listener)
  }

  fun setSectionsInfo(course: Course) {
    sectionInfo.text = Utils.CourseUtils.getOpenSections(course).toString() + " open sections of " + course.sections.size
  }

  companion object {
    fun newInstance(parent: View): CourseViewHolder {
      val courseTitle = parent.findViewById<TextView>(R.id.list_item_title)
      val sectionInfo = parent.findViewById<TextView>(R.id.course_list_sections)
      return CourseViewHolder(parent, courseTitle, sectionInfo)
    }
  }
}
