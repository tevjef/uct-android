package com.tevinjeffrey.rmp.ratings;

import com.tevinjeffrey.rmp.Listing;
import com.tevinjeffrey.rmp.Professor;

import org.apache.commons.lang3.StringUtils;

public class RatingParser {

    public static Ratings getProfRatings(String html) {
        return new Ratings(
                new Rating.Easiness(findEasiness(html)),
                new Rating.Helpfulness(findHelpfulness(html)),
                new Rating.Clarity(findClarity(html)),
                new Rating.OverallQuality(findOverallQuality(html)),
                new Rating.AverageGrade(findAverageGrade(html)));
    }

    public static Professor findProfessor(Listing listing, String html) {
        return isLegit(html)? new Professor(findName(html),
                findUniversity(html),
                listing.getDepartment(),
                getProfRatings(html),
                findLocation(html),
                listing.getUrl()): null;
    }

    public static boolean isLegit(String html) {
        return !findName(html).toString().trim().equals("");
    }

    public static Professor.Name findName(String html) {
        String dirty = StringUtils.substringAfter(html, "<span class=\"pfname\">");
        String firstName = StringUtils.substringBefore(dirty, "<").trim();

        dirty = StringUtils.substringAfter(html, "class=\"plname\">");
        String lastName = StringUtils.substringBefore(dirty, "<").trim();

        return new Professor.Name(firstName, lastName);
    }

    public static String findUniversity(String html) {
        String dirty = StringUtils.substringAfter(html, "class=\"school\">");
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

    public static String findAverageGrade(String html) {
        String dirty = StringUtils.substringAfter(html, "Average Grade");
        String dirty2 = StringUtils.substringAfter(dirty, "\">");
        return StringUtils.substringBefore(dirty2, "<").trim();
    }

    public static String findHotness(String html) {
        String dirty = StringUtils.substringAfter(html, "Hotness");
        String dirty2 = StringUtils.substringAfter(dirty, "<img src=\"");
        return StringUtils.substringBefore(dirty2, "width").trim();
    }

    public static String findHelpfulness(String html) {
        String dirty = StringUtils.substringAfter(html, "<div class=\"label\">Helpfulness</div>");
        String dirty2 = StringUtils.substringAfter(dirty, "<div class=\"rating\">");
        return StringUtils.substringBefore(dirty2, "<").trim();
    }

    public static String findClarity(String html) {
        String dirty = StringUtils.substringAfter(html, "<div class=\"label\">Clarity</div>");
        String dirty2 = StringUtils.substringAfter(dirty, "<div class=\"rating\">");
        return StringUtils.substringBefore(dirty2, "<").trim();
    }

    public static String findEasiness(String html) {
        String dirty = StringUtils.substringAfter(html, "<div class=\"label\">Easiness</div>");
        String dirty2 = StringUtils.substringAfter(dirty, "<div class=\"rating\">");
        return StringUtils.substringBefore(dirty2, "<").trim();
    }

}
