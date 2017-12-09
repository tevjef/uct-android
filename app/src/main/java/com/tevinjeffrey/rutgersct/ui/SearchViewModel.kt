package com.tevinjeffrey.rutgersct.ui

import android.arch.lifecycle.ViewModel
import com.tevinjeffrey.rutgersct.data.model.Course
import com.tevinjeffrey.rutgersct.data.model.Section
import com.tevinjeffrey.rutgersct.data.model.Semester
import com.tevinjeffrey.rutgersct.data.model.Subject
import com.tevinjeffrey.rutgersct.data.model.University
import com.tevinjeffrey.rutgersct.data.search.UCTSubscription

class SearchViewModel : ViewModel() {
  var university: University? = null
  var semester: Semester? = null
  var subject: Subject? = null
  var course: Course? = null
  var section: Section? = null

  fun buildSubscription(): UCTSubscription {
    val uctSubscription = UCTSubscription(section!!.topic_name)

    val courseBuilder = course!!.newBuilder()
    courseBuilder.sections.clear()
    courseBuilder.sections.add(section)

    val newCourse = courseBuilder.build()

    val subjectBuilder = subject!!.newBuilder()
    subjectBuilder.courses.clear()
    subjectBuilder.courses.add(newCourse)

    val newSubject = subjectBuilder.build()

    val universityBuilder = university!!.newBuilder()
    universityBuilder.subjects.clear()
    universityBuilder.subjects.add(newSubject)

    universityBuilder.available_semesters.clear()
    universityBuilder.available_semesters.add(semester)

    uctSubscription.university = universityBuilder.build()
    return uctSubscription
  }

  fun newSearch() {
    university = null
    semester = null
    subject = null
    course = null
    section = null
  }
}
