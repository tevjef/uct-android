package com.tevinjeffrey.rutgersct.data.uctapi.model.extensions;

import com.tevinjeffrey.rutgersct.data.uctapi.model.Course;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Instructor;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Section;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Semester;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

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

    public static String toString(List<Instructor> instructorList) {
      String str = "";
      for (int i = 0; i < instructorList.size(); i++) {
        Instructor instructor = instructorList.get(i);
        str += instructor.name;
        if (i != instructorList.size() - 1) {
          str += " | ";
        }
      }
      return str;
    }

    public static String getToStringInstructors(List<Instructor> instructorList, String joiner) {
      List<String> str = new ArrayList<>();
      for (Instructor i : instructorList) {
        str.add(i.name);
      }
      return StringUtils.join(str, joiner);
    }
  }

  public static class CourseUtils {
    public static int getOpenSections(Course course) {
      int open = 0;
      for (Section s : course.sections) {
        if (s.status.equals("Open")) {
          open++;
        }
      }
      return open;
    }
  }

  public static class SemesterUtils {
    public static String readableString(Semester semester) {
      return WordUtils.capitalize(semester.season) + " " + semester.year;
    }
  }
}
