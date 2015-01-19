package com.tevinjeffrey.rutgerssoc.utils;

import com.tevinjeffrey.rutgerssoc.model.Course;

public class SectionUtils {

    public SectionUtils() {

    }

    private static String formatMeetingHours(String time) {
        if (time.substring(0,1) == "0") {
            time = time.substring(1,1) + ":" + time.substring(2);
        }
        return time.substring(0, 2) + ":" + time.substring(2);
    }

    public static String getMeetingHoursEnd(Course.Sections.MeetingTimes time) {
        String meridian;
        String starttime = time.getStartTime();
        String endtime = time.getEndTime();
        String pmcode = time.getPmCode();
        // check pm code
        if (pmcode != "A") meridian = "PM";
            // check like 1pm after 11am
        else if (Integer.valueOf(endtime.substring(0,2)) >
                Integer.valueOf(starttime.substring(0,2))) meridian = "PM";
            // check 12pm
        else if (endtime.substring(0, 2).equals("12")) meridian = "PM";
            // else am
        else meridian = "AM";

        return formatMeetingHours(time.getEndTime()) + " " + meridian;
    }

    public static String getMeetingHoursBegin(Course.Sections.MeetingTimes time) {
        String meridian = time.getPmCode().equals("A") ? "AM" : "PM";
        return formatMeetingHours(time.getStartTime()) + " " + meridian;
    }

    public static String getMeetingDayName(Course.Sections.MeetingTimes time) {
        if(time.getMeetingDay().equals("M")) {
            return "Monday";
        } else if(time.getMeetingDay().equals("T")) {
            return "Tuesday";
        } else if(time.getMeetingDay().equals("W")) {
            return "Wednesday";
        } else if(time.getMeetingDay().equals("TH")) {
            return "Thursday";
        } else if(time.getMeetingDay().equals("F")) {
            return "Friday";
        } else if(time.getMeetingDay().equals("S")) {
            return "Saturday";
        } else if(time.getMeetingDay().equals("U")) {
            return "Sunday";
        } else {
            return "";
        }
    }
    public static String getMeetingHours(Course.Sections.MeetingTimes time) {
        return SectionUtils.getMeetingHoursBegin(time) + " - " +
                        SectionUtils.getMeetingHoursEnd(time);
    }
    public static String getClassLocation(Course.Sections.MeetingTimes time) {
        return (time.getBuildingCode() == null && time.getRoomNumber() == null )? "" :
                time.getBuildingCode() + "-" + time.getRoomNumber();
    }


}
