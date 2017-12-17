package com.tevinjeffrey.rmp.scraper.search

import com.tevinjeffrey.rmp.common.Parameter
import com.tevinjeffrey.rmp.common.Professor

import java.util.Collections

object Decider {

  fun determineProfessor(
      listings: MutableList<Professor>,
      params: Parameter): List<Professor> {
    val comparators = Professor.ProfessorComparator(params)
    Collections.sort(listings, comparators.locationComparator)
    Collections.sort(listings, comparators.departmentComparator)
    Collections.sort(listings, comparators.nameComparator)

    run {
      val iterator = listings.iterator()
      while (iterator.hasNext()) {
        val scrapeProfessor = iterator.next()
        if (params.location?.contains(scrapeProfessor.location!!.city!!) != true) {
          println("Decider: removing on location" + scrapeProfessor.toString())
          iterator.remove()
        }
      }
    }

    if (params.firstName !== "") {
      val iterator = listings.iterator()
      while (iterator.hasNext()) {
        val scrapeProfessor = iterator.next()
        if (scrapeProfessor.firstName!![0] != params.firstName?.get(0)) {
          println("Decider: removing on firstname" + scrapeProfessor.toString())
          iterator.remove()
        }
      }
    }

    return listings
  }
}
