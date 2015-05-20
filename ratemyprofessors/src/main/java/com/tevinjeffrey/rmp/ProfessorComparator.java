package com.tevinjeffrey.rmp;

import org.apache.commons.lang3.StringUtils;
import java.util.Comparator;
import java.util.Locale;

public class ProfessorComparator {

    final private String mLocation;
    final private String mSubject;
    final private String mUniversity;
    final private Professor.Name mName;

    final private LocationComparator locationComparator;
    final private UniversityComparator universityComparator;
    final private NameComparator nameComparator;
    final private DepartmentComparator departmentComparator;

    public ProfessorComparator(Decider.Parameter params) {
        this.mLocation = params.location;
        this.mSubject = params.department;
        this.mUniversity = params.university;
        this.mName = params.name;

        locationComparator = new LocationComparator(mLocation);
        universityComparator = new UniversityComparator(mUniversity);
        nameComparator = new NameComparator(mName);
        departmentComparator = new DepartmentComparator(mSubject);
    }

    public LocationComparator getLocationComparator() {
        return locationComparator;
    }

    public UniversityComparator getUniversityComparator() {
        return universityComparator;
    }

    public NameComparator getNameComparator() {
        return nameComparator;
    }

    public DepartmentComparator getDepartmentComparator() {
        return departmentComparator;
    }

    public static class LocationComparator implements Comparator<Professor> {

        final private String mLocation;

        public LocationComparator(String location) {
            this.mLocation = location;
        }

        @Override
        public int compare(Professor prof1, Professor prof2) {
            if (prof1.getLocation().equals(mLocation) && !prof2.getLocation().equals(mLocation)) {
                return -1;
            } else if (!prof1.getLocation().equals(mLocation) && prof2.getLocation().equals(mLocation)) {
                return 1;
            } else {
                return prof1.getLocation().compareTo(prof2.getLocation());
            }        
        }
    }

    public static class UniversityComparator implements Comparator<Professor> {

        final private String mUniversity;

        public UniversityComparator(String university) {
            this.mUniversity = university;
        }

        @Override
        public int compare(Professor prof1, Professor prof2) {
            if (prof1.getUniversity().equals(mUniversity) && !prof2.getUniversity().equals(mUniversity)) {
                return -1;
            } else if (!prof1.getUniversity().equals(mUniversity) && prof2.getUniversity().equals(mUniversity)) {
                return 1;
            } else {
                return prof1.getUniversity().compareTo(prof2.getUniversity());
            }
        }
    }

    public static class NameComparator implements Comparator<Professor> {

        final private Professor.Name mName;

        public NameComparator(Professor.Name name) {
            this.mName = name;
        }

        @Override
        public int compare(Professor prof1, Professor prof2) {
            if (mName.getFirst().length() == 0) {
                return prof1.compareTo(prof2);
            } else {
                if (prof1.getFullName().getFirst().charAt(0) == mName.getFirst().charAt(0) &&
                        prof2.getFullName().getFirst().charAt(0) != mName.getFirst().charAt(0)) {
                    return -1;
                } else if (prof1.getFullName().getFirst().charAt(0) != mName.getFirst().charAt(0) &&
                        prof2.getFullName().getFirst().charAt(0) == mName.getFirst().charAt(0)) {
                    return 1;
                } else {
                    return prof1.compareTo(prof2);
                }
            }
        }
    }

    public static class DepartmentComparator implements Comparator<Professor> {

        final private String mDepartment;

        public DepartmentComparator(String department) {
            this.mDepartment = department;
        }

        @Override
        public int compare(Professor prof1, Professor prof2) {
            double score1 = StringUtils.getJaroWinklerDistance(prof1.getDepartment(), mDepartment);
            double score2 = StringUtils.getJaroWinklerDistance(prof2.getDepartment(), mDepartment);

            if (StringUtils.getFuzzyDistance(prof1.getDepartment(), mDepartment, Locale.ENGLISH) >
                    StringUtils.getFuzzyDistance(prof2.getDepartment(), mDepartment, Locale.ENGLISH)) {
                return -1;
            } else if (StringUtils.getFuzzyDistance(prof1.getDepartment(), mDepartment, Locale.ENGLISH) <
                    StringUtils.getFuzzyDistance(prof2.getDepartment(), mDepartment, Locale.ENGLISH)) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private int profScore(Professor prof1) {
        int prof1Location = StringUtils.getFuzzyDistance(prof1.getLocation(), mLocation, Locale.ENGLISH);
        int prof1Subject = StringUtils.getFuzzyDistance(prof1.getDepartment(), mSubject, Locale.ENGLISH);
        int prof1University = StringUtils.getFuzzyDistance(prof1.getUniversity(), mUniversity, Locale.ENGLISH);

        return prof1Location + prof1Subject + prof1University;
    }
}