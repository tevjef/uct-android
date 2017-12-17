package com.tevinjeffrey.rmp.scraper.search

import org.apache.commons.lang3.StringUtils

import java.util.ArrayList
import java.util.Arrays

object SearchParser {

  private fun getListingUrl(rawListing: String): String {
    val dirty = StringUtils.substringAfter(rawListing, "<a href=\"")
    return StringUtils.substringBefore(dirty, "\">")
  }

  fun getNumberOfProfessors(html: String): Int {
    val d1 = StringUtils.substringAfter(html, "<div class=\"result-count\">")
    val d2 = StringUtils.substringAfter(d1, "of ")
    val d3 = StringUtils.substringBefore(d2, " results</div>")

    var num = 0
    try {
      num = Integer.parseInt(d3)
    } catch (e: NumberFormatException) {
      //noop
    }

    return num
  }

  private fun getRawListings(html: String): List<String> {
    val rawListing = ArrayList<String>()
    rawListing.addAll(Arrays.asList(*StringUtils.splitByWholeSeparator(
        html,
        "<li class=\"listing PROFESSOR\">"
    )))
    rawListing.removeAt(0)
    return rawListing
  }

  fun getSearchResults(html: String): List<Listing> {
    var profUrl: String
    val rawListings = getRawListings(html)
    val listings = ArrayList<Listing>()

    for (rawListing in rawListings) {
      profUrl = getListingUrl(rawListing)

      listings.add(Listing(profUrl))
    }

    return listings
  }
}
