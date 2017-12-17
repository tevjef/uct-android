package com.tevinjeffrey.rutgersct.data.model

fun Course.openSections(): Int {
  return this.sections.count { it.status == "Open" }
}

fun Instructor.firstName(): String {
  val firstName = this.name.substringAfter(",")
  return if (this.name != null) {
    if (this.name == firstName) {
      ""
    } else {
      firstName.trim { it <= ' ' }
    }
  } else ""
}

fun Instructor.lastName(): String {
  return this.name.substringBefore(",")
}

fun Instructor.realName(): String {
  return if (this.name != null) {
    this.name.replace("\\s+".toRegex(), " ").trim { it == ' ' }
  } else ""
}

fun Semester?.string(): String {
  if (this == null) return ""
  return this.season.capitalize() + " " + this.year
}
