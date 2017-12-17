package com.tevinjeffrey.rutgersct.data

import com.tevinjeffrey.rutgersct.data.model.Section

class UrlUtils {
  companion object {
    private fun createSearchUrl(query: String, section: Section): String {
      return section.instructors
          .map { it.name }
          .fold("", { acc, s -> acc + "+OR+" + s }) + query
    }

    fun getGoogleUrl(s: Section): String {
      val query = "+rutgers"
      return createSearchUrl(query, s)
    }
  }
}
