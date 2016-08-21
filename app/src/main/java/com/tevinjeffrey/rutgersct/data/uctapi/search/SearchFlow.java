package com.tevinjeffrey.rutgersct.data.uctapi.search;

import com.tevinjeffrey.rutgersct.data.uctapi.model.Course;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Section;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Semester;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Subject;
import com.tevinjeffrey.rutgersct.data.uctapi.model.University;

public class SearchFlow {
    private String universityTopic;
    private String season;
    private int year;
    private String subjectTopic;
    private String courseTopic;
    private String sectionTopic;

    public University university;
    public Semester semester;
    public Subject subject;
    public Course course;
    public Section section;

    private SearchFlow() {
        // no op
    }

    private SearchFlow(SearchFlow searchFlow) {
        universityTopic = searchFlow.universityTopic;
        season = searchFlow.season;
        year = searchFlow.year;
        subjectTopic = searchFlow.subjectTopic;
        courseTopic = searchFlow.courseTopic;
        sectionTopic = searchFlow.sectionTopic;
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

    public String getUniversityTopic() {
        return universityTopic;
    }

    public String getSeason() {
        return season;
    }

    public int getYear() {
        return year;
    }

    public String getSubjectTopic() {
        return subjectTopic;
    }

    public String getCourseTopic() {
        return courseTopic;
    }

    public String getSectionTopic() {
        return sectionTopic;
    }

    public UCTSubscription buildSubscription() {
        UCTSubscription UCTSubscription = new UCTSubscription(sectionTopic);

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

        UCTSubscription.university = universityBuilder.build();
        return UCTSubscription;
    }

    public static class Builder {
        SearchFlow searchFlow = new SearchFlow();

        public Builder() {
        }

        public Builder university(String universityTopic) {
            searchFlow.universityTopic = universityTopic;
            return this;
        }

        public Builder subject(String subjectTopic) {
            searchFlow.subjectTopic = subjectTopic;
            return this;
        }

        public Builder season(String season) {
            searchFlow.season = season;
            return this;
        }

        public Builder year(int year) {
            searchFlow.year = year;
            return this;
        }

        public Builder course(String courseTopic) {
            searchFlow.courseTopic = courseTopic;
            return this;
        }

        public SearchFlow compile() {
            return new SearchFlow(searchFlow);
        }
    }
}
