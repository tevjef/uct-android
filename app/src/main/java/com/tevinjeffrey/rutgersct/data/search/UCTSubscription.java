package com.tevinjeffrey.rutgersct.data.search;

import android.os.Parcel;
import android.os.Parcelable;
import com.tevinjeffrey.rutgersct.data.model.Course;
import com.tevinjeffrey.rutgersct.data.model.Section;
import com.tevinjeffrey.rutgersct.data.model.Semester;
import com.tevinjeffrey.rutgersct.data.model.Subject;
import com.tevinjeffrey.rutgersct.data.model.University;

public class UCTSubscription implements Parcelable, Comparable {

  public static final String SUBSCRIPTION = "UCTSubscription";
  public static final Creator<UCTSubscription> CREATOR = new Creator<UCTSubscription>() {
    @Override
    public UCTSubscription createFromParcel(Parcel source) {
      return new UCTSubscription(source);
    }

    @Override
    public UCTSubscription[] newArray(int size) {
      return new UCTSubscription[size];
    }
  };
  private String sectionTopicName;
  private University university;

  public UCTSubscription(String sectionTopicName) {
    this.sectionTopicName = sectionTopicName;
  }

  protected UCTSubscription(Parcel in) {
    this.sectionTopicName = in.readString();
    this.university = in.readParcelable(University.class.getClassLoader());
  }

  @Override
  public int compareTo(Object o) {
    if (o == null) {
      return 1;
    }
    UCTSubscription s = (UCTSubscription) o;

    Subject subjectLHS = getSubject();
    Course courseLHS = getCourse();
    Section sectionLHS = getSection();
    String compLHS = subjectLHS.name + courseLHS.number + sectionLHS.number;

    Subject subjectRHS = s.getSubject();
    Course courseRHS = s.getCourse();
    Section sectionRHS = s.getSection();
    String compRHS = subjectRHS.name + courseRHS.number + sectionRHS.number;

    return compLHS.compareTo(compRHS);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UCTSubscription that = (UCTSubscription) o;

    return sectionTopicName.equals(that.sectionTopicName);
  }

  @Override
  public int hashCode() {
    return sectionTopicName.hashCode();
  }

  @Override
  public String toString() {
    return "UCTSubscription{" + sectionTopicName +
        '}';
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.sectionTopicName);
    dest.writeParcelable(this.university, 0);
  }

  public Course getCourse() {
    return getSubject().courses.get(0);
  }

  public SearchFlow getSearchFlow() {
    SearchFlow.Builder searchFlowBuilder = new SearchFlow.Builder();
    searchFlowBuilder.university(getUniversity())
        .subject(getSubject())
        .course(getCourse())
        .section(getSection())
        .semester(getSemester());

    return searchFlowBuilder.compile();
  }

  public Section getSection() {
    return getCourse().sections.get(0);
  }

  public String getSectionTopicName() {
    return sectionTopicName;
  }

  public void setSectionTopicName(String sectionTopicName) {
    this.sectionTopicName = sectionTopicName;
  }

  public Semester getSemester() {
    return getUniversity().available_semesters.get(0);
  }

  public Subject getSubject() {
    return getUniversity().subjects.get(0);
  }

  public University getUniversity() {
    return university;
  }

  public void setUniversity(University university) {
    this.university = university;
  }

  public University updateSection(Section section) {
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

    this.university = universityBuilder.build();

    return university;
  }
}