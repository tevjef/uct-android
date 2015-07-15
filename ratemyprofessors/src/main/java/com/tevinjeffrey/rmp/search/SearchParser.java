package com.tevinjeffrey.rmp.search;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchParser {

    public static List<Listing> getSearchResults(String html) {
        String profName;
        String profDepartment;
        String profUrl;
        String profUniversity;
        List<String> rawListings = getRawListings(html);
        List<Listing> listings = new ArrayList<Listing>();

        for (String rawListing : rawListings) {
            profName = getListingName(rawListing);
            profUniversity = getListingUniversity(rawListing);
            profDepartment = getListingDepartment(profUniversity);
            profUrl = getListingUrl(rawListing);

            listings.add(new Listing(profName, profUniversity, profDepartment, profUrl));
        }

        return listings;

    }

    public static int getNumberOfProfessors(String html) {
        String d1 = StringUtils.substringAfter(html, "<div class=\"result-count\">");
        String d2 = StringUtils.substringAfter(d1, "of ");
        String d3 = StringUtils.substringBefore(d2, " results</div>");

        int num = 0;
        try {
            num = Integer.parseInt(d3);
        } catch (NumberFormatException e) {
            //noop
        }
        return num;
    }

    private static String getListingUrl(String rawListing) {
        String dirty = StringUtils.substringAfter(rawListing, "<a href=\"");
        return StringUtils.substringBefore(dirty, "\">");
    }

    private static String getListingUniversity(String rawListing) {
        String dirty = StringUtils.substringAfter(rawListing, "<span class=\"sub\">");
        return StringUtils.substringBefore(dirty, "</span>");
    }

    private static String getListingDepartment(String profUniversity) {
        return StringUtils.substringAfter(profUniversity, ", ");
    }

    private static String getListingName(String rawListing) {
        String dirty = StringUtils.substringAfter(rawListing, "<span class=\"main\">");
        return StringUtils.substringBefore(dirty, "</span>");
    }

    private static List<String> getRawListings(String html) {
        List<String> rawListing = new ArrayList<String>();
        rawListing.addAll(Arrays.asList(StringUtils.splitByWholeSeparator(html, "<li class=\"listing PROFESSOR\">")));
        rawListing.remove(0);
        return rawListing;
    }
}
