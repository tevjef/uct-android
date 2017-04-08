package com.tevinjeffrey.rmp.common;

public class Professor {

  public String lastName;
  public String title;
  public String email;
  public String department;
  public String firstName;
  public Rating rating;
  public Location location;

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

  public String getDepartment() {
    return department;
  }

  public String getEmail() {
    return email;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public Location getLocation() {
    return location;
  }

  public Rating getRating() {
    return rating;
  }

  public String getTitle() {
    return title;
  }
}
