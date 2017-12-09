package com.tevinjeffrey.rmp.scraper;

import com.tevinjeffrey.rmp.common.Parameter;
import com.tevinjeffrey.rmp.common.Professor;
import com.tevinjeffrey.rmp.common.utils.FuzzyDistance;

import java.util.Comparator;
import java.util.Locale;

public class ScrapeProfessor extends Professor {

  public int compareTo(Professor o) {
    return 0;
  }

  public static class ProfessorComparator {

    final private String mLocation;
    final private String mSubject;
    final private String mFirstName;

    final private LocationComparator locationComparator;
    final private NameComparator nameComparator;
    final private DepartmentComparator departmentComparator;

    public ProfessorComparator(Parameter params) {
      this.mLocation = params.location;
      this.mSubject = params.department;
      this.mFirstName = params.firstName;

      locationComparator = new LocationComparator(mLocation);
      nameComparator = new NameComparator(mFirstName);
      departmentComparator = new DepartmentComparator(mSubject);
    }

    public DepartmentComparator getDepartmentComparator() {
      return departmentComparator;
    }

    public LocationComparator getLocationComparator() {
      return locationComparator;
    }

    public NameComparator getNameComparator() {
      return nameComparator;
    }

    public static class DepartmentComparator implements Comparator<ScrapeProfessor> {

      final private String mDepartment;

      public DepartmentComparator(String department) {
        this.mDepartment = department;
      }

      @Override
      public int compare(ScrapeProfessor prof1, ScrapeProfessor prof2) {
        if (FuzzyDistance.compare(prof1.getDepartment(), mDepartment, Locale.ENGLISH) >
            FuzzyDistance.compare(prof2.getDepartment(), mDepartment, Locale.ENGLISH)) {
          return -1;
        } else if (FuzzyDistance.compare(prof1.getDepartment(), mDepartment, Locale.ENGLISH) <
            FuzzyDistance.compare(prof2.getDepartment(), mDepartment, Locale.ENGLISH)) {
          return 1;
        } else {
          return 0;
        }
      }
    }

    public static class LocationComparator implements Comparator<ScrapeProfessor> {

      final private String mLocation;

      public LocationComparator(String location) {
        this.mLocation = location;
      }

      @Override
      public int compare(ScrapeProfessor prof1, ScrapeProfessor prof2) {
        if (prof1.getLocation().equals(mLocation) && !prof2.getLocation().equals(mLocation)) {
          return -1;
        } else if (!prof1.getLocation().equals(mLocation) && prof2
            .getLocation()
            .equals(mLocation)) {
          return 1;
        } else {
          return prof1.getLocation().getCity().compareTo(prof2.getLocation().getCity());
        }
      }
    }

    public static class NameComparator implements Comparator<ScrapeProfessor> {

      final private String firstName;

      public NameComparator(String firstName) {
        this.firstName = firstName;
      }

      @Override
      public int compare(ScrapeProfessor prof1, ScrapeProfessor prof2) {
        if (firstName.length() == 0) {
          return prof1.compareTo(prof2);
        } else {
          if (prof1.getFirstName() != null && prof1.getFirstName().charAt(0) == firstName.charAt(0)
              &&
              prof2.getFirstName() != null
              && prof2.getFirstName().charAt(0) != firstName.charAt(0)) {
            return -1;
          } else if (prof1.getFirstName() != null
              && prof1.getFirstName().charAt(0) != firstName.charAt(0) &&
              prof2.getFirstName() != null
              && prof2.getFirstName().charAt(0) == firstName.charAt(0)) {
            return 1;
          } else {
            return prof1.compareTo(prof2);
          }
        }
      }
    }
  }
}