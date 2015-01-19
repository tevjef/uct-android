package com.tevinjeffrey.rutgerssoc.model;

import com.tevinjeffrey.rutgerssoc.utils.SectionUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tevin on 1/14/2015.
 */
public class Course {

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getSubjectNotes() {
        return subjectNotes;
    }

    public void setSubjectNotes(String subjectNotes) {
        this.subjectNotes = subjectNotes;
    }

    public String getCourseNotes() {
        return courseNotes;
    }

    public void setCourseNotes(String courseNotes) {
        this.courseNotes = courseNotes;
    }

    public String getPreReqNotes() {
        return preReqNotes;
    }

    public void setPreReqNotes(String preReqNotes) {
        this.preReqNotes = preReqNotes;
    }

    public String getOfferingUnitCode() {
        return offeringUnitCode;
    }

    public void setOfferingUnitCode(String offeringUnitCode) {
        this.offeringUnitCode = offeringUnitCode;
    }

    public int getOpenSections() {
        return openSections;
    }

    public void setOpenSections(int openSections) {
        this.openSections = openSections;
    }
    public int getSectionsTotal() {
        return getSections().size();
    }

    String subject;
    double credits;
    String courseNumber;
    String subjectNotes;
    String courseNotes;
    String preReqNotes;
    String offeringUnitCode;
    int openSections;


    public  String title;

    public String getExpandedTitle() {
        return expandedTitle;
    }

    public void setExpandedTitle(String expendedTitle) {
        this.expandedTitle = StringUtils.capitalize(expendedTitle.toLowerCase());;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = StringUtils.capitalize(title.toLowerCase());
    }


    public List<Sections> getSections() {
        return sections;
    }

    public void setSections(List<Sections> sections) {
        this.sections = sections;
    }

    List<Sections> sections = new ArrayList<>();
    String expandedTitle;

    public class Sections {

        public List<MeetingTimes> getMeetingTimes() {
            return meetingTimes;
        }

        public void setMeetingTimes(List<MeetingTimes> meetingTimes) {
            this.meetingTimes = meetingTimes;
        }

        public List<Instructors> getInstructors() {
            return instructors;
        }
        public void setInstructors(List<Instructors> instructors) {
            this.instructors = instructors;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getSpecialPermissionAddCodeDescription() {
            return specialPermissionAddCodeDescription;
        }

        public void setSpecialPermissionAddCodeDescription(String specialPermissionAddCodeDescription) {
            this.specialPermissionAddCodeDescription = specialPermissionAddCodeDescription;
        }

        public String getExamCode() {
            return examCode;
        }

        public void setExamCode(String examCode) {
            this.examCode = examCode;
        }

        public String getSectionNotes() {
            return sectionNotes;
        }

        public void setSectionNotes(String sectionNotes) {
            this.sectionNotes = sectionNotes;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getCampusCode() {
            return campusCode;
        }

        public void setCampusCode(String campusCode) {
            this.campusCode = campusCode;
        }

        public boolean isOpenStatus() {
            return openStatus;
        }

        public void setOpenStatus(boolean openStatus) {
            this.openStatus = openStatus;
        }

        List<Instructors> instructors = new ArrayList<Instructors>();
        List<MeetingTimes> meetingTimes = new ArrayList<MeetingTimes>();
        String index;
        String specialPermissionAddCodeDescription;
        String examCode;
        String sectionNotes;
        String number;
        String campusCode;
        boolean openStatus;

        public class MeetingTimes implements Comparable {

            public boolean isLecture() {
                return getMeetingModeCode().equals("02");
            }
            public boolean isRecitation() {
                return getMeetingModeCode().equals("03");
            }
            public boolean isLab() {
                return getMeetingModeCode().equals("05");
            }
            public boolean isByArrangement() {
                return getBaClassHours() != null && getBaClassHours().equals("B");
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getBuildingCode() {
                return buildingCode;
            }

            public void setBuildingCode(String buildingCode) {
                this.buildingCode = buildingCode;
            }

            public String getMeetingModeDesc() {
                return meetingModeDesc;
            }

            public void setMeetingModeDesc(String meetingModeDesc) {
                this.meetingModeDesc = meetingModeDesc;
            }

            public String getMeetingModeCode() {
                return meetingModeCode;
            }

            public void setMeetingModeCode(String meetingModeCode) {
                this.meetingModeCode = meetingModeCode;
            }

            public String getCampusAbbrev() {
                return campusAbbrev;
            }

            public void setCampusAbbrev(String campusAbbrev) {
                this.campusAbbrev = campusAbbrev;
            }

            public String getBaClassHours() {
                return baClassHours;
            }

            public void setBaClassHours(String baClassHours) {
                this.baClassHours = baClassHours;
            }

            public String getMeetingDay() {
                return meetingDay;
            }

            public void setMeetingDay(String meetingDay) {
                this.meetingDay = meetingDay;
            }

            public String getRoomNumber() {
                return roomNumber;
            }

            public void setRoomNumber(String roomNumber) {
                this.roomNumber = roomNumber;
            }

            public String getPmCode() {
                return pmCode;
            }

            public void setPmCode(String pmCode) {
                this.pmCode = pmCode;
            }

            String meetingDay;
            String roomNumber;
            String pmCode;
            String startTime;
            String endTime;
            String buildingCode;
            String meetingModeDesc;
            String meetingModeCode;
            String campusAbbrev;
            String baClassHours;

            @Override
            public int compareTo(Object time) {
                MeetingTimes b = (MeetingTimes) time;
                if(b != null) {
                    if(b.isLecture() && this.isLecture()) {
                        if(SectionUtils.getTimeRank(this) < SectionUtils.getTimeRank(b)) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else if (!b.isLecture()) {
                        if (SectionUtils.getClassRank(this) < SectionUtils.getClassRank(b)) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                }
                return 0;
            }
        }


        public class Instructors {
            public String getName() {
                return name;
            }
            public void setName(String name) {
                this.name = name;
            }

            String name;
        }
    }
}
