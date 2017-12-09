package com.tevinjeffrey.rutgersct.data.search;

import android.os.Parcel;
import android.os.Parcelable;
import com.tevinjeffrey.rutgersct.data.model.Course;
import com.tevinjeffrey.rutgersct.data.model.Section;
import com.tevinjeffrey.rutgersct.data.model.Semester;
import com.tevinjeffrey.rutgersct.data.model.Subject;
import com.tevinjeffrey.rutgersct.data.model.University;

public class SearchFlow {
  public University university;
  public Semester semester;
  public Subject subject;
  public Course course;
  public Section section;

  public SearchFlow() {
  }

  protected SearchFlow(Parcel in) {
    this.university = in.readParcelable(University.class.getClassLoader());
    this.semester = in.readParcelable(Semester.class.getClassLoader());
    this.subject = in.readParcelable(Subject.class.getClassLoader());
    this.course = in.readParcelable(Course.class.getClassLoader());
    this.section = in.readParcelable(Section.class.getClassLoader());
  }

  public UCTSubscription buildSubscription() {
    UCTSubscription uctSubscription = new UCTSubscription(section.topic_name);

    Course.Builder courseBuilder = getCourse().newBuilder();
    courseBuilder.sections.clear();
    courseBuilder.sections.add(getSection());

    Course newCourse = courseBuilder.build();

    Subject.Builder subjectBuilder = getSubject().newBuilder();
    subjectBuilder.courses.clear();
    subjectBuilder.courses.add(newCourse);

    Subject newSubject = subjectBuilder.build();

    University.Builder universityBuilder = getUniversity().newBuilder();
    universityBuilder.subjects.clear();
    universityBuilder.subjects.add(newSubject);

    universityBuilder.available_semesters.clear();
    universityBuilder.available_semesters.add(getSemester());

    uctSubscription.setUniversity(universityBuilder.build());
    return uctSubscription;
  }

  public Course getCourse() {
    return course;
  }

  public void setCourse(Course course) {
    this.course = course;
  }

  public Section getSection() {
    return section;
  }

  public void setSection(Section section) {
    this.section = section;
  }

  public Semester getSemester() {
    return semester;
  }

  public void setSemester(Semester semester) {
    this.semester = semester;
  }

  public Subject getSubject() {
    return subject;
  }

  public void setSubject(Subject subject) {
    this.subject = subject;
  }

  public University getUniversity() {
    return university;
  }

  public void setUniversity(University university) {
    this.university = university;
  }

  public static class Builder {
    SearchFlow searchFlow = new SearchFlow();

    public Builder() {
    }

    public SearchFlow compile() {
      return searchFlow;
    }

    public Builder course(Course course) {
      searchFlow.course = course.newBuilder().build();
      return this;
    }

    public Builder section(Section section) {
      searchFlow.section = section.newBuilder().build();
      return this;
    }

    public Builder semester(Semester semester) {
      searchFlow.semester = semester.newBuilder().build();
      return this;
    }

    public Builder subject(Subject subject) {
      searchFlow.subject = subject.newBuilder().build();
      return this;
    }

    public Builder university(University university) {
      searchFlow.university = university.newBuilder().build();
      return this;
    }
  }
}
