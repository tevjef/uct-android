package com.tevinjeffrey.rutgerssoc.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tevinjeffrey.rutgerssoc.utils.UrlUtils;

import java.util.ArrayList;

public class Request implements Parcelable {

    String subject;
    String semester;
    ArrayList<String> locations;
    ArrayList<String> levels;
    String index;

    public Request() {
    }

    public Request(String subject, String semester, ArrayList<String> locations, ArrayList<String> levels) {
        this.subject = subject;
        this.semester = semester;
        this.locations = locations;
        this.levels = levels;
    }

    public Request(String subject, String semester, ArrayList<String> locations, ArrayList<String> levels, String index) {
        this(subject, semester, locations, levels);
        this.index = index;
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

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public ArrayList<String> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<String> locations) {
        this.locations = locations;
    }

    public ArrayList<String> getLevels() {
        return levels;
    }

    public void setLevels(ArrayList<String> levels) {
        this.levels = levels;
    }
    public boolean isCourseRequest() {
        return getSubject() != null;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.subject);
        dest.writeString(this.semester);
        dest.writeSerializable(this.locations);
        dest.writeSerializable(this.levels);
    }

    private Request(Parcel in) {
        this.subject = in.readString();
        this.semester = in.readString();
        this.locations = (ArrayList<String>) in.readSerializable();
        this.levels = (ArrayList<String>) in.readSerializable();
    }

    public static final Parcelable.Creator<Request> CREATOR = new Parcelable.Creator<Request>() {
        public Request createFromParcel(Parcel source) {
            return new Request(source);
        }

        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    public static String toStringList(ArrayList<String> strings) {
        StringBuilder sb = new StringBuilder();
        for(String s :strings) {
            sb.append(s);
            sb.append(", ");
        }
        return UrlUtils.trimTrailingChar(sb.toString());
    }
    @Override
    public String toString() {
        return UrlUtils.buildParamUrl(this);
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
}
