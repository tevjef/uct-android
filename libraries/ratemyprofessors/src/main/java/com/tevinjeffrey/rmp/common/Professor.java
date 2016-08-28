package com.tevinjeffrey.rmp.common;

public class Professor {

    public String lastName;
    public String title;
    public String email;
    public String department;
    public String firstName;
    public Rating rating;
    public Location location;

    public String getLastName() {
        return lastName;
    }

    public String getTitle() {
        return title;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public String getFirstName() {
        return firstName;
    }

    public Rating getRating() {
        return rating;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Professor{" +
                "lastName='" + lastName + '\'' +
                ", title='" + title + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", firstName='" + firstName + '\'' +
                ", rating=" + rating +
                ", location=" + location +
                '}';
    }
}
