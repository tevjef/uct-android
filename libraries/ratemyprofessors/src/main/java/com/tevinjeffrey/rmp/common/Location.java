package com.tevinjeffrey.rmp.common;

public class Location {

  public String university;
  public String city;
  public String state;
  public String room;
  public String address;

  public String getUniversity() {
    return university;
  }

  public String getCity() {
    return city;
  }

  public String getState() {
    return state;
  }

  public String getRoom() {
    return room;
  }

  public String getAddress() {
    return address;
  }

  @Override
  public String toString() {
    return "Location{" +
        "university='" + university + '\'' +
        ", city='" + city + '\'' +
        ", state='" + state + '\'' +
        ", room='" + room + '\'' +
        ", address='" + address + '\'' +
        '}';
  }
}
