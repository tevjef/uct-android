package com.tevinjeffrey.rutgersct.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.StringUtils;

public class Subject implements Parcelable {

    //All code below allows the android system to serialize this object.
    // It's actually quite faster than serialization.
    public static final Parcelable.Creator<Subject> CREATOR = new Parcelable.Creator<Subject>() {
        public Subject createFromParcel(Parcel source) {
            return new Subject(source);
        }

        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };
    private String description;
    private int code;
    private String modifiedDescription;

    public Subject() {
    }

    private Subject(Parcel in) {
        this.description = in.readString();
        this.code = in.readInt();
        this.modifiedDescription = in.readString();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {

        this.description = StringUtils.capitalize(description.toLowerCase());
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
        dest.writeInt(this.code);
        dest.writeString(this.modifiedDescription);
    }
}
