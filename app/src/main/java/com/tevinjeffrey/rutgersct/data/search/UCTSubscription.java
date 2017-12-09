package com.tevinjeffrey.rutgersct.data.search;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.support.annotation.NonNull;

import com.tevinjeffrey.rutgersct.data.model.Course;
import com.tevinjeffrey.rutgersct.data.model.Section;
import com.tevinjeffrey.rutgersct.data.model.Semester;
import com.tevinjeffrey.rutgersct.data.model.Subject;
import com.tevinjeffrey.rutgersct.data.model.University;

@Entity(tableName = "uct_subscription")
public class UCTSubscription implements Comparable {

  @PrimaryKey
  @ColumnInfo(name = "section_topic_name")
  @NonNull
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
