package com.tevinjeffrey.rmp.common;

public class Parameter {
    public String university;
    public String department;
    public String location;
    public String courseNumber;
    public String firstName;
    public String lastName;


    public Parameter(String university, String department, String location, String courseNumber, String firstName, String lastName) {
        this.university = university;
        this.department = department;
        this.location = location;
        this.courseNumber = courseNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "university='" + university + '\'' +
                ", department='" + department + '\'' +
                ", location='" + location + '\'' +
                ", courseNumber='" + courseNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
