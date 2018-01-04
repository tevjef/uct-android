package com.tevinjeffrey.rmp.common

import com.tevinjeffrey.rmp.common.utils.FuzzyDistance
import java.util.Comparator
import java.util.Locale

data class Professor(
    var lastName: String? = null,
    var title: String? = null,
    var email: String? = null,
    var department: String? = null,
    var firstName: String? = null,
    var rating: Rating? = null,
    var location: Location? = null) {

  class ProfessorComparator(params: Parameter) {
    private val location: String = params.location.orEmpty()
    private val subject: String = params.department.orEmpty()
    private val firstName: String = params.firstName.orEmpty()

    val locationComparator: LocationComparator
    val nameComparator: NameComparator
    val departmentComparator: DepartmentComparator

    init {
      locationComparator = LocationComparator(Location(city = location))
      nameComparator = NameComparator(firstName)
      departmentComparator = DepartmentComparator(subject)
    }

    class DepartmentComparator(private val mDepartment: String) : Comparator<Professor> {

      override fun compare(prof1: Professor, prof2: Professor): Int {
        return when {
          FuzzyDistance.compare(prof1.department,
              mDepartment,
              Locale.ENGLISH) > FuzzyDistance.compare(prof2.department,
              mDepartment,
              Locale.ENGLISH) -> -1
          FuzzyDistance.compare(prof1.department,
              mDepartment,
              Locale.ENGLISH) < FuzzyDistance.compare(prof2.department,
              mDepartment,
              Locale.ENGLISH) -> 1
          else -> 0
        }
      }
    }

    class LocationComparator(private val location: Location) : Comparator<Professor> {

      override fun compare(prof1: Professor, prof2: Professor): Int {
        return if (prof1.location == location && prof2.location != location) {
          -1
        } else if (prof1.location != location && prof2
            .location == location) {
          1
        } else {
          prof1.location!!.city!!.compareTo(prof2.location!!.city!!)
        }
      }
    }

    class NameComparator(private val firstName: String) : Comparator<Professor> {

      override fun compare(prof1: Professor, prof2: Professor): Int {
        return if (prof1.firstName.orEmpty().isNotEmpty() && prof1.firstName!!.get(0) == firstName[0]
            &&
            prof2.firstName.orEmpty().isNotEmpty()
            && prof2.firstName!![0] != firstName[0]) {
          -1
        } else if (prof1.firstName.orEmpty().isNotEmpty()
            && prof1.firstName!![0] != firstName[0] &&
            prof2.firstName != null
            && prof2.firstName!!.get(0) == firstName[0]) {
          1
        } else {
          0
        }
      }
    }
  }
}
