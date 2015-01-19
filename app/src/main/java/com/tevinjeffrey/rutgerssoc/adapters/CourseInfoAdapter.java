package com.tevinjeffrey.rutgerssoc.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.model.Course;
import com.tevinjeffrey.rutgerssoc.utils.SectionUtils;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tevin on 1/14/2015.
 */
public class CourseInfoAdapter  {

    private Context context;
    private Course course;
    private View rowView;


    //courseTitle
    private TextView courseTitle;
    //offeringUnitCode:subject:courseNumber
    private TextView shortenedCourseInfo;
    //credits
    private TextView credits;
    //courseNotes
    private TextView courseNotes;
    //subjectNotes
    private TextView subjectNotes;
    //openSections
    private TextView openSections;
    //totalSections
    private TextView totalSections;
    //preReqNotes
    private TextView preReqNotes;
    private View subjectNotesContainer;
    private View courseNotesContainer;
    private View preReqNotesContainer;
    private LinearLayout sectionContainer;

    public CourseInfoAdapter(Context context, Course item, View rowView ) {
        this.context = context;
        this.course = item;
        this.rowView = rowView;

        init();
    }

    private void init() {
        courseTitle = (TextView) rowView.findViewById(R.id.course_title);
        shortenedCourseInfo = (TextView) rowView.findViewById(R.id.shortened_course_info);
        credits = (TextView) rowView.findViewById(R.id.credits_text);
        courseNotes = (TextView) rowView.findViewById(R.id.courseNotes_text);
        subjectNotes = (TextView) rowView.findViewById(R.id.subjectNotes_text);
        courseNotesContainer = rowView.findViewById(R.id.courseNotes_container);
        subjectNotesContainer = rowView.findViewById(R.id.subjectNotes_container);
        preReqNotesContainer = rowView.findViewById(R.id.prereq_container);
        sectionContainer = (LinearLayout) rowView.findViewById(R.id.sections_container);
        openSections = (TextView) rowView.findViewById(R.id.openSections_text);
        totalSections = (TextView) rowView.findViewById(R.id.totalSections_text);
        preReqNotes = (TextView) rowView.findViewById(R.id.prereq_text);

    }

    public void setData() {
        setCourseNotes(course);
        setCourseTitle(course);
        setCredits(course);
        setSubjectNotes(course);
        setShortenedCourseInfo(course);
        setOpenSections(course);
        setTotalSections(course);
        setPreReqNotes(course);
        setSections(course.getSections());
    }

    public void setCourseTitle(Course course) {
        this.courseTitle.setText(course.getTitle());
    }

    public void setShortenedCourseInfo(Course course) {
        this.shortenedCourseInfo.setText(formatShortenedCourseInfo(course));
    }

    public String formatShortenedCourseInfo(Course string) {
        String offeringUnitCode = course.getOfferingUnitCode();
        String subject = course.getSubject();
        String courseNumber = course.getCourseNumber();
        return offeringUnitCode + ":" + subject + ":" + courseNumber;
    }

    public void setCredits(Course course) {
        this.credits.setText(String.valueOf(course.getCredits()));
    }

    public void setCourseNotes(Course course) {
        if (course.getCourseNotes() == null) {
            courseNotesContainer.setVisibility(View.GONE);
        } else {
            this.courseNotes.setText(Html.fromHtml(course.getCourseNotes()));
        }
    }

    public void setSubjectNotes(Course course) {
        if (course.getSubjectNotes() == null) {
            subjectNotesContainer.setVisibility(View.GONE);
        } else {
            this.subjectNotes.setText(Html.fromHtml(course.getSubjectNotes()));
        }
    }
    public void setOpenSections(Course course) {
            this.openSections.setText(String.valueOf(course.getOpenSections()));
    }

    public void setTotalSections(Course course) {
        this.totalSections.setText(String.valueOf(course.getSectionsTotal()));
    }

    public void setPreReqNotes(Course course) {
        if (course.getPreReqNotes() == null) {
            preReqNotesContainer.setVisibility(View.GONE);
        } else {
            this.preReqNotes.setText(Html.fromHtml(course.getPreReqNotes()));
        }
    }

    public void setSections(List<Course.Sections> sections) {
        LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (Course.Sections s : sections) {
            View section  = inflater.inflate(R.layout.sections, null);
            TextView sectionNumber = (TextView) section.findViewById(R.id.section_number);
            TextView instructors = (TextView) section.findViewById(R.id.prof_text);
            LinearLayout sectionTimeContainer = (LinearLayout) section.findViewById(R.id.section_time_container);

            RelativeLayout sectionRoot = (RelativeLayout) section.findViewById(R.id.sectionRoot);

            if(s.isOpenStatus()) {
                sectionRoot.setBackgroundColor(context.getResources().getColor(R.color.open_course));
            } else {
                sectionRoot.setBackgroundColor(context.getResources().getColor(R.color.closed_course));

            }
            //setSectionNumber
            sectionNumber.setText(s.getNumber());

            //setInstructors
            StringBuilder sb= new StringBuilder();
            for(Course.Sections.Instructors i : s.getInstructors()) {
                sb.append(i.getName());
                sb.append(", ");
            }
            instructors.setText(sb.toString());

            //setMeetingTimes
            for(Course.Sections.MeetingTimes time : s.getMeetingTimes()) {
                View timeLayout  = inflater.inflate(R.layout.time, null);

                TextView timeText = (TextView) timeLayout.findViewById(R.id.time_text);
                TextView locationText = (TextView) timeLayout.findViewById(R.id.sectionLocation_text);

                if(time.isByArrangement() ) {
                    timeText.setText(Html.fromHtml("<i>Hours By Arrangement</i>"));
                    locationText.setText("");
                } else {
                    timeText.setText(SectionUtils.getMeetingDayName(time) + "  "
                            + SectionUtils.getMeetingHours(time));
                    locationText.setText(SectionUtils.getClassLocation(time));
                }


                sectionTimeContainer.addView(timeLayout);
            }

            sectionContainer.addView(section);

        }
    }
}
