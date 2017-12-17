package com.tevinjeffrey.rmp.scraper.search

import com.tevinjeffrey.rmp.common.Location
import com.tevinjeffrey.rmp.common.Professor
import com.tevinjeffrey.rmp.common.Rating
import org.apache.commons.lang3.StringUtils

object RatingParser {

  private fun findAverageGrade(html: String): String {
    val dirty = StringUtils.substringAfter(html, "Average Grade")
    val dirty2 = StringUtils.substringAfter(dirty, "\">")
    return StringUtils.substringBefore(dirty2, "<").trim { it <= ' ' }
  }

  private fun findClarity(html: String): String {
    val dirty = StringUtils.substringAfter(html, "<div class=\"label\">Clarity</div>")
    val dirty2 = StringUtils.substringAfter(dirty, "<div class=\"rating\">")
    return StringUtils.substringBefore(dirty2, "<").trim { it <= ' ' }
  }

  private fun findDepartment(html: String): String {
    val dirty = StringUtils.substringAfter(html, "Professor in the ")
    return StringUtils.substringBefore(dirty, " department").trim { it <= ' ' }
  }

  private fun findEasiness(html: String): String {
    val dirty = StringUtils.substringAfter(html, "Level of Difficulty")
    val dirty2 = StringUtils.substringAfter(dirty, "<div class=\"grade\">")
    return StringUtils.substringBefore(dirty2, "<").trim { it <= ' ' }
  }

  private fun findFirstName(html: String): String {
    val dirty = StringUtils.substringAfter(html, "<span class=\"pfname\">")
    return StringUtils.substringBefore(dirty, "<").trim { it <= ' ' }
  }

  private fun findHelpfulness(html: String): String {
    val dirty = StringUtils.substringAfter(html, "<div class=\"label\">Helpfulness</div>")
    val dirty2 = StringUtils.substringAfter(dirty, "<div class=\"rating\">")
    return StringUtils.substringBefore(dirty2, "<").trim { it <= ' ' }
  }

  private fun findHotness(html: String): Boolean {
    val dirty = StringUtils.substringAfter(html, "Hotness")
    val dirty2 = StringUtils.substringAfter(dirty, "<img src=\"")
    return !dirty2.contains("cold")
  }

  private fun findLastName(html: String): String {
    val dirty = StringUtils.substringAfter(html, "class=\"plname\">")
    return StringUtils.substringBefore(dirty, "<").trim { it <= ' ' }
  }

  private fun findLocation(html: String): String {
    val dirty = StringUtils.substringAfter(html, "</a>,")
    return StringUtils.substringBefore(dirty, ", ").trim { it <= ' ' }
  }

  private fun findOverallQuality(html: String): String {
    val dirty = StringUtils.substringAfter(html, "Overall Quality")
    val dirty2 = StringUtils.substringAfter(dirty, "\">")
    return StringUtils.substringBefore(dirty2, "<").trim { it <= ' ' }
  }

  fun findProfessor(listing: Listing, html: String): Professor {
    return Professor(
        firstName = findFirstName(html),
        lastName = findLastName(html),
        department = findDepartment(html),
        location = Location(
            city = findLocation(html),
            university = findUniversity(html)
        ),
        rating =  Rating(
            overall = toStringDouble(findOverallQuality(html)),
            clarity = toStringDouble(findClarity(html)),
            easiness = toStringDouble(findEasiness(html)),
            helpfulness = toStringDouble(findHelpfulness(html)),
            isHotness = findHotness(html),
            ratingsCount = findRatingCount(html),
            ratingUrl = listing.url,
            averageGrade = findAverageGrade(html))
    )
  }

  private fun findRatingCount(html: String): Int {
    val dirty = StringUtils.substringAfter(html, "count active\" data-table=\"rating-filter\">")
    return try {
      Integer.parseInt(StringUtils.substringBefore(dirty,
          " Student Ratings").trim { it <= ' ' })
    } catch (e: NumberFormatException) {
      0
    }
  }

  private fun findUniversity(html: String): String {
    val dirty = StringUtils.substringAfter(html, "class=\"school\">")
    return StringUtils.substringBefore(dirty, "<").trim { it <= ' ' }
  }


  private fun toStringDouble(str: String): Double {
    return try {
      java.lang.Double.parseDouble(str)
    } catch (e: NumberFormatException) {
      0.0
    }
  }
}
