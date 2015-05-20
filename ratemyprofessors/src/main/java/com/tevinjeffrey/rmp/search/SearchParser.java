package com.tevinjeffrey.rmp.search;

import com.tevinjeffrey.rmp.Listing;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

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
