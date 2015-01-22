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

    public double getCredits() {
        return credits;
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

    public class Sections {

        public List<MeetingTimes> getMeetingTimes() {
            return meetingTimes;
        }

        public List<Instructors> getInstructors() {
            return instructors;
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

        List<Instructors> instructors = new ArrayList<>();
        List<MeetingTimes> meetingTimes = new ArrayList<>();
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


        public class Instructors {
            public String getName() {
                return name;
            }

            String name;
        }
    }


}
