package com.tevinjeffrey.rmp.common

data class Rating(
    var overall: Double = 0.toDouble(),
    var helpfulness: Double = 0.toDouble(),
    var clarity: Double = 0.toDouble(),
    var easiness: Double = 0.toDouble(),
    var ratingsCount: Int = 0,
    var isHotness: Boolean = false,
    var ratingUrl: String? = null,
    var averageGrade: String? = null) {

  val fullRatingUrl: String
    get() = RMP.RMP_BASE_URL + ratingUrl!!
}
