package com.tevinjeffrey.rutgersct.data.uctapi.search;

import android.os.Parcel;
import android.os.Parcelable;

import com.tevinjeffrey.rutgersct.data.uctapi.model.Course;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Section;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Subject;
import com.tevinjeffrey.rutgersct.data.uctapi.model.University;

public class UCTSubscription implements Parcelable {

    public static final String SUBSCRIPTION = "UCTSubscription";

    String sectionTopicName;
    University university;

    public UCTSubscription(String sectionTopicName) {
        this.sectionTopicName = sectionTopicName;
    }

    public String getSectionTopicName() {
        return sectionTopicName;
    }

    public University getUniversity() {
        return university;
    }

    public Subject getSubject() {
        return getUniversity().subjects.get(0);
    }

    public Course getCourse() {
        return getSubject().courses.get(0);
    }

    public Section getSection() {
        return getCourse().sections.get(0);
    }

    public UCTSubscription updateSection(Section section) {
        Course.Builder courseBuilder = getCourse().newBuilder();
        courseBuilder.sections.clear();
        courseBuilder.sections.add(section);

        Course newCourse = courseBuilder.build();

        Subject.Builder subjectBuilder = getSubject().newBuilder();
        subjectBuilder.courses.clear();
        subjectBuilder.courses.add(newCourse);

        Subject newSubject = subjectBuilder.build();

        University.Builder universityBuilder = getUniversity().newBuilder();
        universityBuilder.subjects.clear();
        universityBuilder.subjects.add(newSubject);

        university = universityBuilder.build();

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UCTSubscription that = (UCTSubscription) o;

        return sectionTopicName.equals(that.sectionTopicName);

    }

    @Override
    public int hashCode() {
        return sectionTopicName.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sectionTopicName);
        dest.writeParcelable(this.university, flags);
    }

    protected UCTSubscription(Parcel in) {
        this.sectionTopicName = in.readString();
        this.university = in.readParcelable(University.class.getClassLoader());
    }

    public static final Parcelable.Creator<UCTSubscription> CREATOR = new Parcelable.Creator<UCTSubscription>() {
        @Override
        public UCTSubscription createFromParcel(Parcel source) {
            return new UCTSubscription(source);
        }

        @Override
        public UCTSubscription[] newArray(int size) {
            return new UCTSubscription[size];
        }
    };
}
