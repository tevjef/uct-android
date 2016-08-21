package com.tevinjeffrey.rutgersct.data.uctapi.search;

public class SearchFlow {
    private String universityTopic;
    private String season;
    private int year;
    private String subjectTopic;
    private String courseTopic;
    private String sectionTopic;

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
