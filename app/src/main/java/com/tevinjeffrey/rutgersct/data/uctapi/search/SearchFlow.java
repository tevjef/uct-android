package com.tevinjeffrey.rutgersct.data.uctapi.search;

import com.tevinjeffrey.rutgersct.data.uctapi.model.Course;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Section;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Semester;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Subject;
import com.tevinjeffrey.rutgersct.data.uctapi.model.University;

public class SearchFlow {
    public University university;
    public Semester semester;
    public Subject subject;
    public Course course;
    public Section section;

    public SearchFlow() {
    }

    public University getUniversity() {
        return university;
    }

    public Semester getSemester() {
        return semester;
    }

    public Subject getSubject() {
        return subject;
    }

    public Course getCourse() {
        return course;
    }

    public Section getSection() {
        return section;
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

    public static class Builder {
        SearchFlow searchFlow = new SearchFlow();

        public Builder() {
        }

        public Builder university(University university) {
            searchFlow.university = university;
            return this;
        }

        public Builder subject(Subject subject) {
            searchFlow.subject = subject;
            return this;
        }

        public Builder semester(Semester semester) {
            searchFlow.semester = semester;
            return this;
        }

        public Builder course(Course course) {
            searchFlow.course = course;
            return this;
        }

        public Builder section(Section section) {
            searchFlow.section = section;
            return this;
        }


        public SearchFlow compile() {
            return searchFlow;
        }
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
}
