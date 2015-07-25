package com.tevinjeffrey.rutgersct.rutgersapi.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.text.WordUtils;

public class Subject implements Parcelable {

    private String description;
    private String code;
    private String modifiedDescription;

    public Subject() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return WordUtils.capitalize(description.toLowerCase());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getModifiedDescription() {
        return modifiedDescription;
    }

    public void setModifiedDescription(String modifiedDescription) {
        this.modifiedDescription = modifiedDescription;
    }

    @Override
    public String toString() {
        return getCode() + " : " + getDescription();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.description);
        dest.writeString(this.code);
        dest.writeString(this.modifiedDescription);
    }

    protected Subject(Parcel in) {
        this.description = in.readString();
        this.code = in.readString();
        this.modifiedDescription = in.readString();
    }

    public static final Parcelable.Creator<Subject> CREATOR = new Parcelable.Creator<Subject>() {
        public Subject createFromParcel(Parcel source) {
            return new Subject(source);
        }

        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };
}
