package com.tevinjeffrey.rmp.professor;

import com.tevinjeffrey.rmp.ratings.Ratings;
import com.tevinjeffrey.rmp.search.Listing;

public class Professor extends Listing implements Comparable<Professor> {
	final private Ratings ratings;
    final private Name name;
    final private String location;

	public Professor(Name name, String university, String department, Ratings ratings, String location, String url) {
		super(name.toString(), university, department, url);
		this.ratings = ratings;
        this.name = name;
        this.location = location;
	}

    public Ratings getRatings() {
        return ratings;
    }

    public Name getFullName() {
        return name;
    }

    public String getLocation() {
        return location;
    }


    @Override
    public int compareTo(Professor o) {
        return 0;
    }

    public static class Name {
        String first;
        String last;

        public Name(String first, String last) {
            this.first = first;
            this.last = last;
        }

        public String getFirst() {
            return first;
        }

        @Override
        public String toString() {
            return first  +  " "  + last;
        }

        public String getLast() {
            return last;
        }
    }

    @Override
    public String toString() {
        return "Professor: " + name+ " \n" +
                "Department: " + getDepartment() + "\n" +
                 ratings +  "\n" +
                "Location: " + location;
    }

}