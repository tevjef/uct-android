package com.tevinjeffrey.rutgerssoc.utils;

import com.tevinjeffrey.rutgerssoc.model.Course;

public class SectionUtils {

    public SectionUtils() {

    }

    private static String formatMeetingHours(String time) {
        String s;
        if (time.substring(0,1).equals("0")) {
            return time.substring(1,2) + ":" + time.substring(2);
        }
        return time.substring(0, 2) + ":" + time.substring(2);
    }

    public static String getMeetingHoursEnd(Course.Sections.MeetingTimes time) {
        String meridian;
        String starttime = time.getStartTime();
        String endtime = time.getEndTime();
        String pmcode = time.getPmCode();

        int e = Integer.valueOf(endtime.substring(0,2));
        int s = Integer.valueOf(starttime.substring(0,2));

        // check pm code
        if (!pmcode.equals("A")) meridian = "PM";
            // check like 1pm after 11am
        else if (e < s)  {
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

    public static String getMeetingHoursBegin(Course.Sections.MeetingTimes time) {
        String meridian;
        if(time.getPmCode() != null) {
             meridian = time.getPmCode().equals("A") ? "AM" : "PM";
            return formatMeetingHours(time.getStartTime()) + " " + meridian;
        }
        return "";
    }

    public static String getMeetingDayName(Course.Sections.MeetingTimes time) {
        if(time.getMeetingDay() != null) {
            String formattedDay;
            if (time.getMeetingDay().equals("M")) {
                 formattedDay = "Monday";
            } else if (time.getMeetingDay().equals("T")) {
                 formattedDay = "Tuesday";
            } else if (time.getMeetingDay().equals("W")) {
                 formattedDay = "Wednesday";
            } else if (time.getMeetingDay().equals("TH")) {
                 formattedDay = "Thursday";
            } else if (time.getMeetingDay().equals("F")) {
                 formattedDay = "Friday";
            } else if (time.getMeetingDay().equals("S")) {
                 formattedDay = "Saturday";
            } else if (time.getMeetingDay().equals("U")) {
                 formattedDay = "Sunday";
            } else {
                return "";
            }
            return !time.isLecture()? "<i>" + formattedDay + "</i>": formattedDay;
        }
        return "";
    }

    public static String getMeetingHours(Course.Sections.MeetingTimes time) {
        if(time.getStartTime() != null || time.getEndTime() != null) {
            String fullTime = SectionUtils.getMeetingHoursBegin(time) + " - " +
                    SectionUtils.getMeetingHoursEnd(time);
            if (time.isRecitation()) {
                return "<i>" + fullTime + "</i>";
            } else {
                return fullTime;
            }

        } else if (time.isLab()) {
            return time.getMeetingModeDesc();
        } else if (time.isByArrangement()){
            return  "<i>Hours By Arrangement</i>";
        }
        return time.getMeetingModeDesc();
    }
    public static String getClassLocation(Course.Sections.MeetingTimes time) {
        return (time.getBuildingCode() == null && time.getRoomNumber() == null )? "" :
                time.getBuildingCode() + "-" + time.getRoomNumber() + " " + time.getCampusAbbrev();
    }

    public static int getTimeRank(Course.Sections.MeetingTimes time) {
        if(time.getMeetingDay().equals("M")) {
            return 100;
        } else if(time.getMeetingDay().equals("T")) {
            return 90;
        } else if(time.getMeetingDay().equals("W")) {
            return 80;
        } else if(time.getMeetingDay().equals("TH")) {
            return 70;
        } else if(time.getMeetingDay().equals("F")) {
            return 60;
        } else if(time.getMeetingDay().equals("S")) {
            return 50;
        } else if(time.getMeetingDay().equals("U")) {
            return 40;
        }
        return -1;
    }
    public static int getClassRank(Course.Sections.MeetingTimes time) {
        if(time.isLecture()) {
            return 100;
        } else if(time.isRecitation()) {
            return 90;
        } else if(time.isByArrangement()) {
            return 80;
        } else if(time.isLab()) {
            return 70;
        }
        return -1;
    }

}
