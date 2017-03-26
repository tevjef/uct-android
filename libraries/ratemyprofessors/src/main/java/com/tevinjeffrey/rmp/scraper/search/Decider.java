package com.tevinjeffrey.rmp.scraper.search;

import com.tevinjeffrey.rmp.common.Parameter;
import com.tevinjeffrey.rmp.scraper.ScrapeProfessor;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Decider {

  public static List<ScrapeProfessor> determineProfessor(
      List<ScrapeProfessor> listings,
      final Parameter params) {
    ScrapeProfessor.ProfessorComparator comparators =
        new ScrapeProfessor.ProfessorComparator(params);
    Collections.sort(listings, comparators.getLocationComparator());
    Collections.sort(listings, comparators.getDepartmentComparator());
    Collections.sort(listings, comparators.getNameComparator());

    for (final Iterator<ScrapeProfessor> iterator = listings.iterator(); iterator.hasNext(); ) {
      ScrapeProfessor scrapeProfessor = iterator.next();
      if (!params.location.contains(scrapeProfessor.getLocation().getCity())) {
        System.out.println("Decider: removing on location" + scrapeProfessor.toString());
        iterator.remove();
      }
    }

    if (params.firstName != null && params.firstName != "") {
      for (final Iterator<ScrapeProfessor> iterator = listings.iterator(); iterator.hasNext(); ) {
        ScrapeProfessor scrapeProfessor = iterator.next();
        if (!(scrapeProfessor.getFirstName().charAt(0) == params.firstName.charAt(0))) {
          System.out.println("Decider: removing on firstname" + scrapeProfessor.toString());
          iterator.remove();
        }
      }
    }

    return listings;
  }
}
