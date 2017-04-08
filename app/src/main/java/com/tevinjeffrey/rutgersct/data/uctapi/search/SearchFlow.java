package com.tevinjeffrey.rutgersct.data.uctapi.search;

import android.os.Parcel;
import android.os.Parcelable;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Course;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Section;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Semester;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Subject;
import com.tevinjeffrey.rutgersct.data.uctapi.model.University;

public class SearchFlow implements Parcelable {
  public static final Parcelable.Creator<SearchFlow> CREATOR =
      new Parcelable.Creator<SearchFlow>() {
        @Override
        public SearchFlow createFromParcel(Parcel source) {
          return new SearchFlow(source);
        }

        @Override
        public SearchFlow[] newArray(int size) {
          return new SearchFlow[size];
        }
      };
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

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public String toString() {
    return "SearchFlow{" +
        "university=" + university +
        ", semester=" + semester +
        ", subject=" + subject +
        ", course=" + course +
        ", section=" + section +
        '}';
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.university, flags);
    dest.writeParcelable(this.semester, flags);
    dest.writeParcelable(this.subject, flags);
    dest.writeParcelable(this.course, flags);
    dest.writeParcelable(this.section, flags);
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
