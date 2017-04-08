package com.tevinjeffrey.rutgersct.data.rutgersapi.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.tevinjeffrey.rutgersct.data.rutgersapi.utils.SectionUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

//This class is a bit complicated. It's a POJO for the GSON library to quickly serialize json data
// into java objects. It is also a parcelable object which means it allows the android system to serialize this object.
// Which in turn allows me to pss this object between Activities and Fragments.
public class Course implements Comparable, Parcelable {

  public static final Creator<Course> CREATOR = new Creator<Course>() {
    public Course createFromParcel(Parcel source) {
      return new Course(source);
    }

    public Course[] newArray(int size) {
      return new Course[size];
    }
  };
  private String title;
  private String subject;
  private double credits;
  private String courseNumber;
  private String subjectNotes;
  private String courseNotes;
  private String preReqNotes;
  private String offeringUnitCode;
  private int openSections;
  private List<Section> sections = new ArrayList<>();
  private String expandedTitle;
  private Subject mEnclosingSubject;
  private Request mRequest;

  public Course() {
  }

  public Course(Course course) {
    this.title = course.title;
    this.subject = course.subject;
    this.credits = course.credits;
    this.courseNumber = course.courseNumber;
    this.subjectNotes = course.subjectNotes;
    this.courseNotes = course.courseNotes;
    this.preReqNotes = course.preReqNotes;
    this.offeringUnitCode = course.offeringUnitCode;
    this.openSections = course.openSections;
    this.expandedTitle = course.expandedTitle;
    this.mEnclosingSubject = course.mEnclosingSubject;
    this.mRequest = course.mRequest;
  }

  protected Course(Parcel in) {
    this.title = in.readString();
    this.subject = in.readString();
    this.credits = in.readDouble();
    this.courseNumber = in.readString();
    this.subjectNotes = in.readString();
    this.courseNotes = in.readString();
    this.preReqNotes = in.readString();
    this.offeringUnitCode = in.readString();
    this.openSections = in.readInt();
    this.sections = in.createTypedArrayList(Section.CREATOR);
    this.expandedTitle = in.readString();
    this.mEnclosingSubject = in.readParcelable(getClass().getClassLoader());
    this.mRequest = in.readParcelable(getClass().getClassLoader());
  }

  @Override
  public int compareTo(@NonNull Object another) {
    Course b = (Course) another;
    int result;

    result = new SubjectNumberComparator().compare(this.getSubject(), b.getSubject());
    if (result == 0) {
      return new SubjectNumberComparator().compare(this.getCourseNumber(), b.getCourseNumber());
    }
    return result;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public String toString() {
    return "Course" + getTrueTitle();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.title);
    dest.writeString(this.subject);
    dest.writeDouble(this.credits);
    dest.writeString(this.courseNumber);
    dest.writeString(this.subjectNotes);
    dest.writeString(this.courseNotes);
    dest.writeString(this.preReqNotes);
    dest.writeString(this.offeringUnitCode);
    dest.writeInt(this.openSections);
    dest.writeTypedList(sections);
    dest.writeString(this.expandedTitle);
    dest.writeParcelable(this.mEnclosingSubject, 0);
    dest.writeParcelable(this.mRequest, 0);
  }

  public String getCourseNotes() {
    return courseNotes;
  }

  public String getCourseNumber() {
    return courseNumber;
  }

  public String getCredits() {
    if (credits % 1 == 0) {
      return String.valueOf((int) credits);
    } else {
      return String.valueOf(credits);
    }
  }

  public Subject getEnclosingSubject() {
    return mEnclosingSubject;
  }

  public void setEnclosingSubject(Subject mEnclosingSubject) {
    this.mEnclosingSubject = mEnclosingSubject;
  }

  public int getNumberOfNoPrintSections() {
    int num = 0;
    for (Section s : getSections()) {
      if (!s.isPrinted()) {
        ++num;
      }
    }
    return num;
  }

  public int getNumberOfOpenNoPrintSections() {
    int num = 0;
    for (Section s : getSections()) {
      if (!s.isPrinted() && s.openStatus) {
        ++num;
      }
    }
    return num;
  }

  public String getOfferingUnitCode() {
    return offeringUnitCode;
  }

  public int getOpenSections() {
    return openSections - getNumberOfOpenNoPrintSections();
  }

  public String getPreReqNotes() {
    return preReqNotes;
  }

  public Request getRequest() {
    return mRequest;
  }

  public void setRequest(Request mRequest) {
    this.mRequest = mRequest;
  }

  public List<Section> getSections() {
    return sections;
  }

  public void setSections(List<Section> sections) {
    this.sections = sections;
  }

  public int getSectionsTotal() {
    return getSections().size() - getNumberOfNoPrintSections();
  }

  public String getSubject() {
    return subject;
  }

  public String getSubjectNotes() {
    return subjectNotes;
  }

  public String getTrueTitle() {
    return (getExpandedTitle() == null || getExpandedTitle().length() <= 0) ? getTitle() :
           getExpandedTitle();
  }

  String getExpandedTitle() {
    if (expandedTitle != null) {
      return expandedTitle.replaceAll("\\s+", " ").trim();
    }
    return null;
  }

  String getTitle() {
    if (title != null) {
      return title.replaceAll("\\s+", " ").trim();
    }
    return null;
  }

  public static class Section implements Comparable, Parcelable {

    public static final Creator<Section> CREATOR = new Creator<Section>() {
      public Section createFromParcel(Parcel source) {
        return new Section(source);
      }

      public Section[] newArray(int size) {
        return new Section[size];
      }
    };
    public List<Instructors> instructors = new ArrayList<>();
    public List<MeetingTimes> meetingTimes = new ArrayList<>();
    public List<CrossListedSections> crossListedSections = new ArrayList<>();
    public List<Majors> majors = new ArrayList<>();
    public List<Comments> comments = new ArrayList<>();
    public String subtitle;
    public String index;
    public String specialPermissionAddCodeDescription;
    public String specialPermissionAddCode;
    public String specialPermissionDropCode;
    public String offeringUnitCode;
    public String synopsisUrl;
    public String examCode;
    public String sectionNotes;
    public String number;
    public String campusCode;
    public String printed;
    public Request request;
    public Course course;
    int stopPoint;
    boolean openStatus;

    public Section() {

    }

    public Section(Section dummy) {
      this.instructors = dummy.instructors;
      this.meetingTimes = dummy.meetingTimes;
      this.crossListedSections = dummy.crossListedSections;
      this.majors = dummy.majors;
      this.comments = dummy.comments;
      this.subtitle = dummy.subtitle;
      this.index = dummy.index;
      this.specialPermissionAddCodeDescription = dummy.specialPermissionAddCodeDescription;
      this.specialPermissionAddCode = dummy.specialPermissionAddCode;
      this.specialPermissionDropCode = dummy.specialPermissionDropCode;
      this.offeringUnitCode = dummy.offeringUnitCode;
      this.synopsisUrl = dummy.synopsisUrl;
      this.examCode = dummy.examCode;
      this.sectionNotes = dummy.sectionNotes;
      this.number = dummy.number;
      this.campusCode = dummy.campusCode;
      this.printed = dummy.printed;
      this.stopPoint = dummy.stopPoint;
      this.openStatus = dummy.openStatus;
    }
    protected Section(Parcel in) {
      this.instructors = in.createTypedArrayList(Instructors.CREATOR);
      this.meetingTimes = in.createTypedArrayList(MeetingTimes.CREATOR);
      this.crossListedSections = in.createTypedArrayList(CrossListedSections.CREATOR);
      this.majors = in.createTypedArrayList(Majors.CREATOR);
      this.comments = in.createTypedArrayList(Comments.CREATOR);
      this.subtitle = in.readString();
      this.index = in.readString();
      this.specialPermissionAddCodeDescription = in.readString();
      this.specialPermissionAddCode = in.readString();
      this.specialPermissionDropCode = in.readString();
      this.offeringUnitCode = in.readString();
      this.synopsisUrl = in.readString();
      this.examCode = in.readString();
      this.sectionNotes = in.readString();
      this.number = in.readString();
      this.campusCode = in.readString();
      this.printed = in.readString();
      this.request = in.readParcelable(getClass().getClassLoader());
      this.course = in.readParcelable(getClass().getClassLoader());
      this.stopPoint = in.readInt();
      this.openStatus = in.readByte() != 0;
    }

    @Override
    public int compareTo(@NonNull Object another) {
      Section anotherSection = (Section) another;

      int result = 0;
      if (this.getCourse() != null && anotherSection.getCourse() != null) {
        result = this.getCourse().compareTo(anotherSection.getCourse());
        if (result == 0) {
          return new SectionComparator().compare(this, anotherSection);
        }
      }
      return result;
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public String toString() {
      return "Section #" + getNumber();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeTypedList(instructors);
      dest.writeTypedList(meetingTimes);
      dest.writeTypedList(crossListedSections);
      dest.writeTypedList(majors);
      dest.writeTypedList(comments);
      dest.writeString(this.subtitle);
      dest.writeString(this.index);
      dest.writeString(this.specialPermissionAddCodeDescription);
      dest.writeString(this.specialPermissionAddCode);
      dest.writeString(this.specialPermissionDropCode);
      dest.writeString(this.offeringUnitCode);
      dest.writeString(this.synopsisUrl);
      dest.writeString(this.examCode);
      dest.writeString(this.sectionNotes);
      dest.writeString(this.number);
      dest.writeString(this.campusCode);
      dest.writeString(this.printed);
      dest.writeParcelable(this.request, 0);
      dest.writeParcelable(this.course, 0);
      dest.writeInt(this.stopPoint);
      dest.writeByte(openStatus ? (byte) 1 : (byte) 0);
    }

    public String getCampusCode() {
      return campusCode;
    }

    public List<Comments> getComments() {
      return comments;
    }

    public Course getCourse() {
      return course;
    }

    public void setCourse(Course course) {
      this.course = course;
    }

    public List<CrossListedSections> getCrossListedSections() {
      return crossListedSections;
    }

    public String getExamCode() {
      return examCode;
    }

    public String getIndex() {
      return index;
    }

    public List<Instructors> getInstructors() {
      return instructors;
    }

    public List<Majors> getMajors() {
      return majors;
    }

    public List<MeetingTimes> getMeetingTimes() {
      return meetingTimes;
    }

    public String getNumber() {
      return number;
    }

    public String getOfferingUnitCode() {
      return offeringUnitCode;
    }

    public String getPrinted() {
      return printed;
    }

    public Request getRequest() {
      return request;
    }

    public void setRequest(Request mRequest) {
      this.request = mRequest;
    }

    public String getSectionNotes() {
      return sectionNotes;
    }

    public void setSectionNotes(String sectionNotes) {
      this.sectionNotes = sectionNotes;
    }

    public String getSpecialPermissionAddCode() {
      return specialPermissionAddCode;
    }

    public String getSpecialPermissionAddCodeDescription() {
      return specialPermissionAddCodeDescription;
    }

    public String getSpecialPermissionDropCode() {
      return specialPermissionDropCode;
    }

    public int getStopPoint() {
      return stopPoint;
    }

    public String getSubtitle() {
      return subtitle;
    }

    public String getSynopsisUrl() {
      return synopsisUrl;
    }

    public String getToStringComments(String joiner) {
      return StringUtils.join(comments, joiner);
    }

    public String getToStringCrossListedSections(String joiner) {
      return StringUtils.join(instructors, joiner);
    }

    public String getToStringInstructors(String joiner) {
      return StringUtils.join(instructors, joiner);
    }

    public String getToStringMajors(String joiner) {
      return StringUtils.join(majors, joiner);
    }

    public String getToStringMeetingTimes(String joiner) {
      return StringUtils.join(meetingTimes, joiner);
    }

    public boolean hasComments() {
      return getComments().size() > 0;
    }

    public boolean hasCrossListed() {
      return getCrossListedSections().size() > 0;
    }

    public boolean hasMajors() {
      return getMajors().size() > 0;
    }

    public boolean hasMetaData() {
      return hasCrossListed() ||
          hasSpecialPermission() ||
          hasSubtitle() ||
          hasMajors() ||
          hasComments() ||
          hasNotes();
    }

    public boolean hasNotes() {
      return getSectionNotes() != null;
    }

    public boolean hasSpecialPermission() {
      return getSpecialPermissionAddCode() != null;
    }

    public boolean hasSubtitle() {
      return getSubtitle() != null;
    }

    public boolean isOpenStatus() {
      return openStatus;
    }

    public boolean isPrinted() {
      return getPrinted().equals("Y");
    }

    public static class Comments implements Parcelable {

      public static final Creator<Comments> CREATOR = new Creator<Comments>() {
        public Comments createFromParcel(Parcel source) {
          return new Comments(source);
        }

        public Comments[] newArray(int size) {
          return new Comments[size];
        }
      };
      String description;

      public Comments() {

      }

      protected Comments(Parcel in) {
        this.description = in.readString();
      }

      @Override
      public int describeContents() {
        return 0;
      }

      @Override
      public String toString() {
        return getDescription();
      }

      @Override
      public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.description);
      }

      public String getDescription() {
        return description;
      }
    }

    public static class CrossListedSections implements Parcelable {

      public static final Creator<CrossListedSections> CREATOR =
          new Creator<CrossListedSections>() {
            public CrossListedSections createFromParcel(Parcel source) {
              return new CrossListedSections(source);
            }

            public CrossListedSections[] newArray(int size) {
              return new CrossListedSections[size];
            }
          };
      String sectionNumber;
      String offeringUnitCode;
      String subjectCode;
      String courseNumber;

      public CrossListedSections() {

      }

      protected CrossListedSections(Parcel in) {
        this.sectionNumber = in.readString();
        this.offeringUnitCode = in.readString();
        this.subjectCode = in.readString();
        this.courseNumber = in.readString();
      }

      @Override
      public int describeContents() {
        return 0;
      }

      @Override
      public String toString() {
        return getFullCrossListedSection();
      }

      @Override
      public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sectionNumber);
        dest.writeString(this.offeringUnitCode);
        dest.writeString(this.subjectCode);
        dest.writeString(this.courseNumber);
      }

      public String getCourseNumber() {
        return courseNumber;
      }

      public String getFullCrossListedSection() {
        return getOfferingUnitCode() + ":" + getSubjectCode() + ":"
            + getCourseNumber() + ":" + getSectionNumber();
      }

      public String getOfferingUnitCode() {
        return offeringUnitCode;
      }

      public String getSectionNumber() {
        return sectionNumber;
      }

      public String getSubjectCode() {
        return subjectCode;
      }
    }

    public static class Instructors implements Parcelable {

      public static final Creator<Instructors> CREATOR = new Creator<Instructors>() {
        public Instructors createFromParcel(Parcel source) {
          return new Instructors(source);
        }

        public Instructors[] newArray(int size) {
          return new Instructors[size];
        }
      };
      String name;

      public Instructors(String name) {
        this.name = name;
      }

      protected Instructors(Parcel in) {
        this.name = in.readString();
      }

      @Override
      public int describeContents() {
        return 0;
      }

      @Override
      public String toString() {
        return getName();
      }

      @Override
      public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
      }

      public String getFirstName() {
        String firstName = StringUtils.substringAfter(name, ",");
        if (name.equals(firstName)) {
          return "";
        } else {
          return firstName.trim();
        }
      }

      public String getLastName() {
        return StringUtils.substringBefore(name, ",");
      }

      public String getName() {
        return name.replaceAll("\\s+", " ").trim();
      }
    }

    public static class Majors implements Parcelable {

      public static final Creator<Majors> CREATOR = new Creator<Majors>() {
        public Majors createFromParcel(Parcel source) {
          return new Majors(source);
        }

        public Majors[] newArray(int size) {
          return new Majors[size];
        }
      };
      boolean isMajorCode;
      boolean isUnitCode;
      String code;

      public Majors() {
      }

      protected Majors(Parcel in) {
        this.isMajorCode = in.readByte() != 0;
        this.isUnitCode = in.readByte() != 0;
        this.code = in.readString();
      }

      @Override
      public int describeContents() {
        return 0;
      }

      @Override
      public String toString() {
        return getCode();
      }

      @Override
      public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(isMajorCode ? (byte) 1 : (byte) 0);
        dest.writeByte(isUnitCode ? (byte) 1 : (byte) 0);
        dest.writeString(this.code);
      }

      public String getCode() {
        return code;
      }

      public boolean isMajorCode() {
        return isMajorCode;
      }

      public boolean isUnitCode() {
        return isUnitCode;
      }
    }

    public static class MeetingTimes implements Comparable, Parcelable {

      public static final Creator<MeetingTimes> CREATOR = new Creator<MeetingTimes>() {
        public MeetingTimes createFromParcel(Parcel source) {
          return new MeetingTimes(source);
        }

        public MeetingTimes[] newArray(int size) {
          return new MeetingTimes[size];
        }
      };
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

      public MeetingTimes() {

      }

      protected MeetingTimes(Parcel in) {
        this.meetingDay = in.readString();
        this.roomNumber = in.readString();
        this.pmCode = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.buildingCode = in.readString();
        this.meetingModeDesc = in.readString();
        this.meetingModeCode = in.readString();
        this.campusAbbrev = in.readString();
        this.baClassHours = in.readString();
      }

      @Override
      public int compareTo(@NonNull Object time) {
        MeetingTimes b = (MeetingTimes) time;
        int result = new ClassRankComparator().compare(this, b);
        if (result == 0) {
          result = new TimeRankComparator().compare(this, b);
        }
        return result;
      }

      @Override
      public int describeContents() {
        return 0;
      }

      @Override
      public String toString() {
        return SectionUtils.getMeetingDayName(this) + " " + SectionUtils.getMeetingHours(this) + " "
            + SectionUtils.getClassLocation(this);
      }

      @Override
      public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.meetingDay);
        dest.writeString(this.roomNumber);
        dest.writeString(this.pmCode);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.buildingCode);
        dest.writeString(this.meetingModeDesc);
        dest.writeString(this.meetingModeCode);
        dest.writeString(this.campusAbbrev);
        dest.writeString(this.baClassHours);
      }

      public String getBaClassHours() {
        return baClassHours;
      }

      public String getBuildingCode() {
        return buildingCode;
      }

      public String getCampus() {
        String campus;

        if (getCampusAbbrev() != null) {
          switch (getCampusAbbrev()) {
            case "NWK":
              campus = "Newark";
              break;
            case "CAM":
              campus = "Camden";
              break;
            default:
              campus = "New Brunswick";
          }
          return campus;
        }
        return null;
      }

      public String getCampusAbbrev() {
        return campusAbbrev;
      }

      public String getEndTime() {
        return endTime;
      }

      public String getMeetingDay() {
        return meetingDay;
      }

      public String getMeetingModeCode() {
        return meetingModeCode;
      }

      public String getMeetingModeDesc() {
        return meetingModeDesc;
      }

      public String getPmCode() {
        return pmCode;
      }

      public String getRoomNumber() {
        return roomNumber;
      }

      public String getStartTime() {
        return startTime;
      }

      public boolean isByArrangement() {
        return getBaClassHours() != null && getBaClassHours().equals("B");
      }

      public boolean isLab() {
        return getMeetingModeCode() != null && getMeetingModeCode().equals("05");
      }

      public boolean isLecture() {
        return getMeetingModeCode() != null && getMeetingModeCode().equals("02");
      }

      public boolean isRecitation() {
        return getMeetingModeCode() != null && getMeetingModeCode().equals("03");
      }

      public boolean isStudio() {
        return getMeetingModeCode() != null && getMeetingModeCode().equals("07");
      }

      private class ClassRankComparator implements Comparator<MeetingTimes> {
        @Override
        public int compare(MeetingTimes lhs, MeetingTimes rhs) {
          if (SectionUtils.getClassRank(lhs) < SectionUtils.getClassRank(rhs)) {
            return 1;
          } else if (SectionUtils.getClassRank(lhs) > SectionUtils.getClassRank(rhs)) {
            return -1;
          }
          return 0;
        }
      }

      private class TimeRankComparator implements Comparator<MeetingTimes> {
        @Override
        public int compare(MeetingTimes lhs, MeetingTimes rhs) {
          if (SectionUtils.getTimeRank(lhs) < SectionUtils.getTimeRank(rhs)) {
            return 1;
          } else if (SectionUtils.getTimeRank(lhs) > SectionUtils.getTimeRank(rhs)) {
            return -1;
          } else {
            return 0;
          }
        }
      }
    }

    private class SectionComparator implements Comparator<Section> {

      @Override
      public int compare(Section lhs, Section rhs) {
        return lhs.getIndex().compareTo(rhs.getIndex());
      }
    }
  }

  private class SubjectNumberComparator implements Comparator<String> {
    @Override
    public int compare(String lhs, String rhs) {
      if (Integer.valueOf(lhs) > Integer.valueOf(rhs)) {
        return 1;
      } else if (Integer.valueOf(lhs) < Integer.valueOf(rhs)) {
        return -1;
      } else {
        return 0;
      }
    }
  }
}
