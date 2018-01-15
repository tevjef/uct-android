package com.tevinjeffrey.rutgersct.data.database.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.squareup.moshi.Json
import com.tevinjeffrey.rutgersct.data.model.Course
import com.tevinjeffrey.rutgersct.data.model.Section
import com.tevinjeffrey.rutgersct.data.model.Semester
import com.tevinjeffrey.rutgersct.data.model.Subject
import com.tevinjeffrey.rutgersct.data.model.University

@Entity(tableName = "uct_subscription")
data class UCTSubscription(@PrimaryKey
                           @ColumnInfo(name = "section_topic_name")
                           @Json(name = "sectionTopicName")
                           val sectionTopicName: String,
                           @Json(name = "university")
                           var university: University) : Comparable<UCTSubscription> {

  val subject: Subject
    get() = university.subjects!![0]

  val semester: Semester
    get() = university.available_semesters[0]

  val course: Course
    get() = subject.courses!![0]

  val section: Section
    get() = course.sections[0]

  override operator fun compareTo(other: UCTSubscription): Int {
    val subjectLHS = subject
    val courseLHS = course
    val sectionLHS = section
    val compLHS = subjectLHS.name + courseLHS.number + sectionLHS.number

    val subjectRHS = other.subject
    val courseRHS = other.course
    val sectionRHS = other.section
    val compRHS = subjectRHS.name + courseRHS.number + sectionRHS.number

    return compLHS.compareTo(compRHS)
  }

  override fun toString(): String {
    return "UCTSubscription(sectionTopicName=$sectionTopicName)"
  }

  fun updateSection(section: Section): University {
    val courseBuilder = course.newBuilder()
    courseBuilder.sections.clear()
    courseBuilder.sections.add(section)

    val newCourse = courseBuilder.build()

    val subjectBuilder = subject.newBuilder()
    subjectBuilder.courses.clear()
    subjectBuilder.courses.add(newCourse)

    val newSubject = subjectBuilder.build()

    val universityBuilder = university.newBuilder()
    universityBuilder.subjects.clear()
    universityBuilder.subjects.add(newSubject)

    this.university = universityBuilder.build()

    return university
  }
}
