package com.tevinjeffrey.rutgersct.rutgersapi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tevinjeffrey.rutgersct.rutgersapi.utils.SemesterUtils.Semester;
import com.tevinjeffrey.rutgersct.rutgersapi.utils.UrlUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;

public class Request implements Parcelable {

    private String subject;
    private Semester semester;
    private ArrayList<String> locations;
    private ArrayList<String> levels;
    private String index;

    public Request() {
    }

    public Request(String subject, Semester semester, ArrayList<String> locations, ArrayList<String> levels) {
        this.subject = subject;
        this.semester = semester;
        this.locations = locations;
        this.levels = levels;
    }

    public Request(String subject, Semester semester, ArrayList<String> locations, ArrayList<String> levels, String index) {
        this(subject, semester, locations, levels);
        this.index = index;
    }

    public static String toStringList(Iterable<String> strings) {
        return StringUtils.join(strings, ", ");
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public AbstractList<String> getLocations() {
        return locations;
    }

    public String getLocationsString() {
        Collection<String> al = new ArrayList<>();
        for (String s : this.getLocations()) {
            al.add(UrlUtils.getAbbreviatedLocationName(s));
        }
        return toStringList(al);
    }

    public void setLocations(ArrayList<String> locations) {
        this.locations = locations;
    }

    public ArrayList<String> getLevels() {
        return levels;
    }

    public String getlevelsString() {
        return toStringList(getLevels());
    }

    public void setLevels(ArrayList<String> levels) {
        this.levels = levels;
    }

    public boolean isCourseRequest() {
        return getSubject() != null;
    }

    //All code below allows the android system to serialize this object.
    // It's actually quite faster than serialization.

    @Override
    public String toString() {
        return UrlUtils.buildParamUrl(this) + " index: " + getIndex();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Request) {
            Request b = (Request) o;
            return (subject.equals(b.getSubject()) && semester.equals(b.getSemester()) &&
                    locations.equals(b.getLocations()) && levels.equals(b.getLevels()) &&
                    levels.equals(b.getLevels()));
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.subject);
        dest.writeParcelable(this.semester, 0);
        dest.writeStringList(this.locations);
        dest.writeStringList(this.levels);
        dest.writeString(this.index);
    }

    protected Request(Parcel in) {
        this.subject = in.readString();
        this.semester = in.readParcelable(Semester.class.getClassLoader());
        this.locations = in.createStringArrayList();
        this.levels = in.createStringArrayList();
        this.index = in.readString();
    }

    public static final Parcelable.Creator<Request> CREATOR = new Parcelable.Creator<Request>() {
        public Request createFromParcel(Parcel source) {
            return new Request(source);
        }

        public Request[] newArray(int size) {
            return new Request[size];
        }
    };
}
