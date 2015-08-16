package com.tevinjeffrey.rmp.scraper.search;

import com.tevinjeffrey.rmp.common.Parameter;
import com.tevinjeffrey.rmp.scraper.ScrapeProfessor;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Decider {

    public static List<ScrapeProfessor> determineProfessor(List<ScrapeProfessor> listings, final Parameter params) {
        ScrapeProfessor.ProfessorComparator comparators = new ScrapeProfessor.ProfessorComparator(params);
        Collections.sort(listings, comparators.getLocationComparator());
        Collections.sort(listings, comparators.getDepartmentComparator());
        Collections.sort(listings, comparators.getNameComparator());

        for(final Iterator<ScrapeProfessor> iterator = listings.iterator(); iterator.hasNext();) {
            if(!iterator.next().getLocation().getCity().equals(params.location)) {
                iterator.remove();
            }
        }

        if (params.firstName != null && params.firstName != "") {
            for (final Iterator<ScrapeProfessor> iterator = listings.iterator(); iterator.hasNext(); ) {
                if (!(iterator.next().getFirstName().charAt(0) == params.firstName.charAt(0))) {
                    iterator.remove();
                }
            }
        }

        return listings;
    }
}
