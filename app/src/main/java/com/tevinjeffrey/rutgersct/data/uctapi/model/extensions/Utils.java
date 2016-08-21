package com.tevinjeffrey.rutgersct.data.uctapi.model.extensions;

import com.tevinjeffrey.rutgersct.data.uctapi.model.Instructor;

import org.apache.commons.lang3.StringUtils;

public class Utils {
    public static class InstructorUtils {
        public static String getName(Instructor instructor) {
            if (instructor.name != null) {
                return instructor.name.replaceAll("\\s+", " ").trim();
            }
            return "";
        }

        public static String getFirstName(Instructor instructor) {
            String firstName = StringUtils.substringAfter(instructor.name, ",");
            if (instructor.name != null) {
                if (instructor.name.equals(firstName)) {
                    return "";
                } else {
                    return firstName.trim();
                }
            }
            return "";
        }

        public static String getLastName(Instructor instructor) {
            return StringUtils.substringBefore(instructor.name, ",");
        }

    }
}
