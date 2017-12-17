package com.tevinjeffrey.rmp.common.utils

import java.util.Locale

/**
 *
 *
 * This string matching algorithm is similar to the algorithms of editors such
 * as Sublime Text, TextMate, Atom and others. One point is given for every
 * matched character. Subsequent matches yield two bonus points. A higher score
 * indicates a higher similarity.
 *
 *
 *
 *
 * This code has been adapted from Apache Commons Lang 3.3.
 *
 */
class FuzzyDistance {

  /**
   *
   *
   * Find the Fuzzy Distance which indicates the similarity score between two
   * Strings. This method uses the default locale.
   *
   *
   * @param term a full term that should be matched against, must not be null
   * @param query the query that will be matched against a term, must not be
   * null
   * @return result score
   * @throws IllegalArgumentException if either String input `null`
   */
  fun compare(term: CharSequence, query: CharSequence): Int? {
    return compare(term, query, Locale.getDefault())
  }

  companion object {
    /**
     *
     *
     * Find the Fuzzy Distance which indicates the similarity score between two
     * Strings.
     *
     *
     * <pre>
     * distance.getFuzzyDistance(null, null, null)                                    =
     * IllegalArgumentException
     * distance.getFuzzyDistance("", "", Locale.ENGLISH)                              = 0
     * distance.getFuzzyDistance("Workshop", "b", Locale.ENGLISH)                     = 0
     * distance.getFuzzyDistance("Room", "o", Locale.ENGLISH)                         = 1
     * distance.getFuzzyDistance("Workshop", "w", Locale.ENGLISH)                     = 1
     * distance.getFuzzyDistance("Workshop", "ws", Locale.ENGLISH)                    = 2
     * distance.getFuzzyDistance("Workshop", "wo", Locale.ENGLISH)                    = 4
     * distance.getFuzzyDistance("Apache Software Foundation", "asf", Locale.ENGLISH) = 3
    </pre> *
     *
     * @param term a full term that should be matched against, must not be null
     * @param query the query that will be matched against a term, must not be
     * null
     * @param locale This string matching logic is case insensitive. A locale is
     * necessary to normalize both Strings to lower case.
     * @return result score
     * @throws IllegalArgumentException if either String input `null` or
     * Locale input `null`
     */
    fun compare(term: CharSequence?, query: CharSequence?, locale: Locale?): Int {
      if (term == null || query == null) {
        throw IllegalArgumentException("Strings must not be null")
      } else if (locale == null) {
        throw IllegalArgumentException("Locale must not be null")
      }
      // fuzzy logic is case insensitive. We normalize the Strings to lower
      // case right from the start. Turning characters to lower case
      // via Character.toLowerCase(char) is unfortunately insufficient
      // as it does not accept a locale.
      val termLowerCase = term.toString().toLowerCase(locale)
      val queryLowerCase = query.toString().toLowerCase(locale)
      // the resulting score
      var score = 0
      // the position in the term which will be scanned next for potential
      // query character matches
      var termIndex = 0
      // index of the previously matched character in the term
      var previousMatchingCharacterIndex = Integer.MIN_VALUE
      for (queryIndex in 0 until queryLowerCase.length) {
        val queryChar = queryLowerCase.get(queryIndex)
        var termCharacterMatchFound = false
        while (termIndex < termLowerCase.length && !termCharacterMatchFound) {
          val termChar = termLowerCase.get(termIndex)
          if (queryChar == termChar) {
            // simple character matches result in one point
            score++
            // subsequent character matches further improve
            // the score.
            if (previousMatchingCharacterIndex + 1 == termIndex) {
              score += 2
            }
            previousMatchingCharacterIndex = termIndex
            // we can leave the nested loop. Every character in the
            // query can match at most one character in the term.
            termCharacterMatchFound = true
          }
          termIndex++
        }
      }
      return score
    }
  }
}