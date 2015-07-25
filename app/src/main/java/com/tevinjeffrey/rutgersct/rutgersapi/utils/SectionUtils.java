package com.tevinjeffrey.rutgersct.rutgersapi.utils;

import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course.Section.MeetingTimes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SectionUtils {

    public SectionUtils() {

    }

    private static String formatMeetingHours(String time) {
        if (time.substring(0, 1).equals("0")) {
            return time.substring(1, 2) + ":" + time.substring(2);
        }
        return time.substring(0, 2) + ":" + time.substring(2);
    }

    private static String getMeetingHoursEnd(MeetingTimes time) {
        String meridian;
        String starttime = time.getStartTime();
        String endtime = time.getEndTime();
        String pmcode = time.getPmCode();

        int e = Integer.valueOf(endtime.substring(0, 2));
        int s = Integer.valueOf(starttime.substring(0, 2));

        // check pm code
        if (!pmcode.equals("A")) {
            meridian = "PM";
        }
        // check like 1pm after 11am
        else if (e < s) {
            meridian = "PM";
        }
        // check 12pm
        else if (endtime.substring(0, 2).equals("12")) {
            meridian = "PM";
        }
        // else am
        else meridian = "AM";

        return formatMeetingHours(time.getEndTime()) + " " + meridian;
    }

    private static String getMeetingHoursBegin(MeetingTimes time) {
        String meridian;
        if (time.getPmCode() != null) {
            meridian = time.getPmCode().equals("A") ? "AM" : "PM";
            return formatMeetingHours(time.getStartTime()) + " " + meridian;
        }
        return "";
    }

    public static String getMeetingDayName(MeetingTimes time) {
        if (time.getMeetingDay() != null) {
            String formattedDay;
            switch (time.getMeetingDay()) {
                case "M":
                    formattedDay = "Monday";
                    break;
                case "T":
                    formattedDay = "Tuesday";
                    break;
                case "W":
                    formattedDay = "Wednesday";
                    break;
                case "TH":
                    formattedDay = "Thursday";
                    break;
                case "F":
                    formattedDay = "Friday";
                    break;
                case "S":
                    formattedDay = "Saturday";
                    break;
                case "U":
                    formattedDay = "Sunday";
                    break;
                default:
                    return "";
            }
            return formattedDay;
        }
        return "";
    }

    public static String getMeetingHours(MeetingTimes time) {
        if (time.getStartTime() != null || time.getEndTime() != null) {
            return SectionUtils.getMeetingHoursBegin(time) + " - " +
                    SectionUtils.getMeetingHoursEnd(time);
        } else if (time.isByArrangement()) {
            return "Hours By Arrangement";
        }
        return time.getMeetingModeDesc();
    }

    public static String getClassLocation(MeetingTimes time) {
        StringBuilder meetingLocation = new StringBuilder();
        if (time.getBuildingCode() != null) {
            meetingLocation.append(time.getBuildingCode());
        }
        if (time.getRoomNumber() != null) {
            meetingLocation.append("-");
            meetingLocation.append(time.getRoomNumber());
        }
        if (time.getCampusAbbrev() != null) {
            meetingLocation.append("   ");
            meetingLocation.append(time.getCampusAbbrev());
        }
        return meetingLocation.toString();
    }

    public static int getTimeRank(MeetingTimes time) {
        if (time.getMeetingDay() != null) {
            switch (time.getMeetingDay()) {
                case "M":
                    return 10;
                case "T":
                    return 9;
                case "W":
                    return 8;
                case "TH":
                    return 7;
                case "F":
                    return 6;
                case "S":
                    return 5;
                case "U":
                    return 4;
            }
        }
        return -1;
    }

    public static int getClassRank(MeetingTimes time) {
        if (time.isLecture()) {
            return 6;
        } else if (time.isRecitation()) {
            return 5;
        } else if (time.isByArrangement()) {
            return 4;
        } else if (time.isLab()) {
            return 3;
        } else if (time.isStudio()) {
            return 2;
        }
        return -1;
    }

    public static void scrubSectionList(List<Course.Section> sectionData) {
        Collection<Course.Section> toRemove = new ArrayList<>();
        Course.Section temp;
        for (int i = 0, size = sectionData.size(); i < size; i++) {
            temp = sectionData.get(i);
            if (!temp.isPrinted()) {
                toRemove.add(temp);
            }
        }
        sectionData.removeAll(toRemove);
    }
}
