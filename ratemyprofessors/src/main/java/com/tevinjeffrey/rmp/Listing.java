package com.tevinjeffrey.rmp;

public class Listing {
    final private String name;
    final private String university;
    final private String department;
    final private String url;

    public Listing(String name, String university, String department, String url) {
        this.name = name;
        this.university = university;
        this.department = department;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUniversity() {
        return university;
    }

    public String getDepartment() {
        return department;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Listing{" +
                "mName='" + name + '\'' +
                ", mUniversity='" + university + '\'' +
                ", department='" + department + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
