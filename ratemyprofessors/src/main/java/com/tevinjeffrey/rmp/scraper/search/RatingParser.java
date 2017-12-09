package com.tevinjeffrey.rmp.scraper.search;

import com.tevinjeffrey.rmp.scraper.ScrapeLocation;
import com.tevinjeffrey.rmp.scraper.ScrapeProfessor;
import com.tevinjeffrey.rmp.scraper.ScrapeRating;

import org.apache.commons.lang3.StringUtils;

public class RatingParser {

  public static String findAverageGrade(String html) {
    String dirty = StringUtils.substringAfter(html, "Average Grade");
    String dirty2 = StringUtils.substringAfter(dirty, "\">");
    return StringUtils.substringBefore(dirty2, "<").trim();
  }

  public static String findClarity(String html) {
    String dirty = StringUtils.substringAfter(html, "<div class=\"label\">Clarity</div>");
    String dirty2 = StringUtils.substringAfter(dirty, "<div class=\"rating\">");
    return StringUtils.substringBefore(dirty2, "<").trim();
  }

  public static String findDepartment(String html) {
    String dirty = StringUtils.substringAfter(html, "Professor in the ");
    return StringUtils.substringBefore(dirty, " department").trim();
  }

  public static String findEasiness(String html) {
    String dirty = StringUtils.substringAfter(html, "Level of Difficulty");
    String dirty2 = StringUtils.substringAfter(dirty, "<div class=\"grade\">");
    return StringUtils.substringBefore(dirty2, "<").trim();
  }

  public static String findFirstName(String html) {
    String dirty = StringUtils.substringAfter(html, "<span class=\"pfname\">");
    return StringUtils.substringBefore(dirty, "<").trim();
  }

  public static String findHelpfulness(String html) {
    String dirty = StringUtils.substringAfter(html, "<div class=\"label\">Helpfulness</div>");
    String dirty2 = StringUtils.substringAfter(dirty, "<div class=\"rating\">");
    return StringUtils.substringBefore(dirty2, "<").trim();
  }

  public static boolean findHotness(String html) {
    String dirty = StringUtils.substringAfter(html, "Hotness");
    String dirty2 = StringUtils.substringAfter(dirty, "<img src=\"");
    return !dirty2.contains("cold");
  }

  public static String findLastName(String html) {
    String dirty = StringUtils.substringAfter(html, "class=\"plname\">");
    return StringUtils.substringBefore(dirty, "<").trim();
  }

  public static String findLocation(String html) {
    String dirty = StringUtils.substringAfter(html, "</a>,");
    return StringUtils.substringBefore(dirty, ", ").trim();
  }

  public static String findOverallQuality(String html) {
    String dirty = StringUtils.substringAfter(html, "Overall Quality");
    String dirty2 = StringUtils.substringAfter(dirty, "\">");
    return StringUtils.substringBefore(dirty2, "<").trim();
  }

  public static ScrapeProfessor findProfessor(Listing listing, String html) {
    ScrapeProfessor scrapeProfessor = new ScrapeProfessor();
    scrapeProfessor.firstName = findFirstName(html);
    scrapeProfessor.lastName = findLastName(html);
    scrapeProfessor.department = findDepartment(html);

    ScrapeLocation scrapeLocation = new ScrapeLocation();
    scrapeLocation.city = findLocation(html);
    scrapeLocation.university = findUniversity(html);

    ScrapeRating scrapeRating = new ScrapeRating();
    scrapeRating.overall = toStringDouble(findOverallQuality(html));
    scrapeRating.clarity = toStringDouble(findClarity(html));
    scrapeRating.easiness = toStringDouble(findEasiness(html));
    scrapeRating.helpfulness = toStringDouble(findHelpfulness(html));
    scrapeRating.isHotness = findHotness(html);
    scrapeRating.ratingsCount = findRatingCount(html);
    scrapeRating.ratingUrl = listing.getUrl();
    scrapeRating.averageGrade = findAverageGrade(html);

    scrapeProfessor.location = scrapeLocation;
    scrapeProfessor.rating = scrapeRating;

    return scrapeProfessor;
  }

  private static int findRatingCount(String html) {
    String dirty = StringUtils.substringAfter(html, "count active\" data-table=\"rating-filter\">");
    try {
      return Integer.parseInt(StringUtils.substringBefore(dirty, " Student Ratings").trim());
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  public static String findUniversity(String html) {
    String dirty = StringUtils.substringAfter(html, "class=\"school\">");
    return StringUtils.substringBefore(dirty, "<").trim();
  }

  public static boolean isLegit(String html) {
    return !findFirstName(html).trim().equals("");
  }

  private static double toStringDouble(String str) {
    try {
      return Double.parseDouble(str);
    } catch (NumberFormatException e) {
      return 0;
    }
  }
}
