package com.tevinjeffrey.rmp.common;

import static com.tevinjeffrey.rmp.common.RMP.RMP_BASE_URL;

public class Rating {

  public double overall;
  public double helpfulness;
  public double clarity;
  public double easiness;
  public int ratingsCount;
  public boolean isHotness;
  public String ratingUrl;
  public String averageGrade;

  @Override
  public String toString() {
    return "Rating{" +
        "overall=" + overall +
        ", helpfulness=" + helpfulness +
        ", clarity=" + clarity +
        ", easiness=" + easiness +
        ", ratingsCount=" + ratingsCount +
        ", isHotness=" + isHotness +
        ", ratingUrl='" + ratingUrl + '\'' +
        ", averageGrade='" + averageGrade + '\'' +
        '}';
  }

  public String getAverageGrade() {
    return averageGrade;
  }

  public double getClarity() {
    return clarity;
  }

  public double getEasiness() {
    return easiness;
  }

  public String getFullRatingUrl() {
    return RMP_BASE_URL + ratingUrl;
  }

  public double getHelpfulness() {
    return helpfulness;
  }

  public double getOverall() {
    return overall;
  }

  public String getRatingUrl() {
    return ratingUrl;
  }

  public int getRatingsCount() {
    return ratingsCount;
  }

  public boolean isHotness() {
    return isHotness;
  }
}
