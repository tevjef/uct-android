package com.tevinjeffrey.rutgerssoc.model;

import com.tevinjeffrey.rutgerssoc.utils.SectionUtils;

import java.util.ArrayList;
import java.util.List;

public class Course {

    public String getSubject() {
        return subject;
    }

    public String getCredits() {
        if(credits % 1 == 0) {
            return String.valueOf((int)credits);
        } else {
            return String.valueOf(credits);
        }
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public String getSubjectNotes() {
        return subjectNotes;
    }

    public String getCourseNotes() {
        return courseNotes;
    }

    public String getPreReqNotes() {
        return preReqNotes;
    }

    public String getOfferingUnitCode() {
        return offeringUnitCode;
    }

    public int getOpenSections() {
        return openSections;
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

    public String getTitle() {
        return title;
    }


    public List<Sections> getSections() {
        return sections;
    }

    List<Sections> sections = new ArrayList<>();
    String expandedTitle;

    public static class Sections {

        public List<MeetingTimes> getMeetingTimes() {
            return meetingTimes;
        }

        public List<Instructors> getInstructors() {
            return instructors;
        }

        public List<Comments> getComments() {
            return comments;
        }

        public List<Majors> getMajors() {
            return majors;
        }

        public List<CrossListedSections> getCrossListedSections() {
            return crossListedSections;
        }

        public int getStopPoint() {
            return stopPoint;
        }

        public String getIndex() {
            return index;
        }

        public String getSpecialPermissionAddCodeDescription() {
            return specialPermissionAddCodeDescription;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public String getSpecialPermissionAddCode() {
            return specialPermissionAddCode;
        }

        public String getSpecialPermissionDropCode() {
            return specialPermissionDropCode;
        }

        public String getOfferingUnitCode() {
            return offeringUnitCode;
        }

        public String getSynopsisUrl() {
            return synopsisUrl;
        }

        public String getExamCode() {
            return examCode;
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

        public String getCampusCode() {
            return campusCode;
        }

        public boolean isOpenStatus() {
            return openStatus;
        }

        public boolean hasNotes() {
            return getSectionNotes() != null;
        }
        public boolean hasComments() {
            return getComments().size() > 0;
        }
        public boolean hasMajors() {
            return getMajors().size() > 0;
        }
        public boolean hasSpecialPermission() {
            return getSpecialPermissionAddCode() != null;
        }
        public boolean hasSubtitle() {
            return getSubtitle() != null;
        }
        public boolean hasCrossListed() {
            return getCrossListedSections().size() > 0;
        }
        public boolean hasMetaData() {
            return hasCrossListed() ||
                    hasSpecialPermission() ||
                    hasSubtitle() ||
                    hasMajors() ||
                    hasComments() ||
                    hasNotes();
        }

        List<Instructors> instructors = new ArrayList<>();
        List<MeetingTimes> meetingTimes = new ArrayList<>();
        List<CrossListedSections> crossListedSections = new ArrayList<>();
        List<Majors> majors = new ArrayList<>();
        List<Comments> comments = new ArrayList<>();
        String subtitle;
        String index;
        String specialPermissionAddCodeDescription;
        String specialPermissionAddCode;
        String specialPermissionDropCode;
        String offeringUnitCode;
        String synopsisUrl;
        String examCode;
        String sectionNotes;
        String number;
        String campusCode;
        int stopPoint;
        boolean openStatus;


        public static class MeetingTimes implements Comparable {

            public boolean isLecture() {
                return getMeetingModeCode() != null && getMeetingModeCode().equals("02");
            }
            public boolean isRecitation() {
                return getMeetingModeCode() != null && getMeetingModeCode().equals("03");
            }
            public boolean isLab() {
                return getMeetingModeCode() != null && getMeetingModeCode().equals("05");
            }
            public boolean isByArrangement() {
                return getBaClassHours() != null && getBaClassHours().equals("B");
            }

            public String getStartTime() {
                return startTime;
            }

            public String getEndTime() {
                return endTime;
            }
            public String getBuildingCode() {
                return buildingCode;
            }
            public String getMeetingModeDesc() {
                return meetingModeDesc;
            }
            public String getMeetingModeCode() {
                return meetingModeCode;
            }
            public String getCampusAbbrev() {
                return campusAbbrev;
            }
            public String getBaClassHours() {
                return baClassHours;
            }
            public String getMeetingDay() {
                return meetingDay;
            }
            public String getRoomNumber() {
                return roomNumber;
            }
            public String getPmCode() {
                return pmCode;
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

        public static class Instructors {
            public String getName() {
                return name;
            }

            String name;
        }

        public static class Comments {
            String description;

            public String getDescription() {
                return description;
            }
        }
        public static class CrossListedSections {
            String sectionNumber;
            String offeringUnitCode;
            String subjectCode;
            String courseNumber;

            public String getSectionNumber() {
                return sectionNumber;
            }

            public String getOfferingUnitCode() {
                return offeringUnitCode;
            }

            public String getSubjectCode() {
                return subjectCode;
            }

            public String getCourseNumber() {
                return courseNumber;
            }
            public String getFullCrossListedSection() {
                return getOfferingUnitCode() + ":" + getSubjectCode() + ":"
                        + getCourseNumber() + ":" + getSectionNumber();
            }
        }
        public static class Majors {
            boolean isMajorCode;
            boolean isUnitCode;
            String code;

            public boolean isMajorCode() {
                return isMajorCode;
            }

            public boolean isUnitCode() {
                return isUnitCode;
            }

            public String getCode() {
                return code;
            }
        }
    }
}
