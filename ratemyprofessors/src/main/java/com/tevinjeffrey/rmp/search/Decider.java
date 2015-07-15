package com.tevinjeffrey.rmp.search;

import com.tevinjeffrey.rmp.professor.Professor;
import com.tevinjeffrey.rmp.professor.ProfessorComparator;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Decider {

    public static List<Professor> determineProfessor(List<Professor> listings, final Parameter params) {
        ProfessorComparator comparators = new ProfessorComparator(params);
        Collections.sort(listings, comparators.getLocationComparator());
        Collections.sort(listings, comparators.getDepartmentComparator());
        Collections.sort(listings, comparators.getNameComparator());
        //Collections.sort(listings, comparators.getUniversityComparator());

        for(final Iterator<Professor> iterator = listings.iterator(); iterator.hasNext();) {
            if(!iterator.next().getLocation().equals(params.location)) {
                iterator.remove();
            }
        }

        if (!params.name.getFirst().equals(" ")) {
            for (final Iterator<Professor> iterator = listings.iterator(); iterator.hasNext(); ) {
                if (!(iterator.next().getFullName().getFirst().charAt(0) == params.name.getFirst().charAt(0))) {
                    iterator.remove();
                }
            }
        }

        return listings;
    }

    public static class Parameter {
        public String university;
        public String department;
        public String location;
        public Professor.Name name;

        public Parameter(String university, String department, String location, Professor.Name name) {
            this.university = university;
            this.department = department;
            this.location = location;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Parameter{" +
                    "university='" + university + '\'' +
                    ", department='" + department + '\'' +
                    ", location='" + location + '\'' +
                    ", name=" + name +
                    '}';
        }
    }
}
