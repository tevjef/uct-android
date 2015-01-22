package com.tevinjeffrey.rutgerssoc.adapters;

import android.content.Context;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.model.Course;
import com.tevinjeffrey.rutgerssoc.utils.CourseUtils;
import com.tevinjeffrey.rutgerssoc.utils.SectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by Tevin on 1/14/2015.
 */
public class SectionInfoAdapter {

    private Context context;
    private Course course;
    private View rowView;



    private TextView preReqNotes;
    private View subjectNotesContainer;
    private View courseNotesContainer;
    private View preReqNotesContainer;
    private LinearLayout sectionContainer;

    public SectionInfoAdapter(Context context, Course item, View rowView) {
        this.context = context;
        this.course = item;
        this.rowView = rowView;

        init();
    }

    private void init() {
        courseTitle = (TextView) rowView.findViewById(R.id.sectionText_text);
        shortenedCourseInfo = (TextView) rowView.findViewById(R.id.sectionText_title);
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
        this.courseTitle.setText(CourseUtils.getTitle(course));
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
            this.courseNotes.setMovementMethod(new ScrollingMovementMethod());
        }
    }

    public void setSubjectNotes(Course course) {
        if (course.getSubjectNotes() == null) {
            subjectNotesContainer.setVisibility(View.GONE);
        } else {
            this.subjectNotes.setText(Html.fromHtml(course.getSubjectNotes()));
            this.subjectNotes.setMovementMethod(new ScrollingMovementMethod());
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
            this.preReqNotes.setMovementMethod(new ScrollingMovementMethod());
        }
    }

    public void setSections(List<Course.Sections> sections) {
        LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // remove classes with a stopPint of 0. These represent some kind of hidden class taught
        // by STAFF. Though the obvious solution is to loop through the list and on some condition,
        // remove the class, this results in a ConcurrentModificationException.
        // Update: Uncommented because it produced unexpected behaviour in the types of classes.
        /*List<Course.Sections> toRemove = new ArrayList<>();
        for(Course.Sections s: sections) {
            if(s.getStopPoint() == 0) {
                toRemove.add(s);
            }
        }
        sections.removeAll(toRemove);*/

        for (Course.Sections s : sections) {
            View section  = inflater.inflate(R.layout.sections, null);
            TextView sectionNumber = (TextView) section.findViewById(R.id.section_number);
            TextView instructors = (TextView) section.findViewById(R.id.prof_text);
            LinearLayout sectionTimeContainer = (LinearLayout) section.findViewById(R.id.section_time_container);

            TextView sectionTitle = (TextView) section.findViewById(R.id.section_number);

            if(s.isOpenStatus()) {
                sectionTitle.setBackgroundColor(context.getResources().getColor(R.color.open_course));
            } else {
                sectionTitle.setBackgroundColor(context.getResources().getColor(R.color.closed_course));

            }
            //setSectionNumber
            sectionNumber.setText(s.getNumber());

            //setInstructors
            StringBuilder sb= new StringBuilder();
            for(Course.Sections.Instructors i : s.getInstructors()) {
                if(s.getInstructors().size() > 1) {
                    sb.append(i.getName());
                    sb.append("; ");
                } else {
                    sb.append(i.getName());
                }
            }
            if(s.getInstructors().size() > 1) sb = new StringBuilder(sb.substring(0, sb.length() -1));
            instructors.setText(sb.toString());

            //setMeetingTimes
            //sort times so that Monday > Tuesday and Lecture > Recitation
            Collections.sort(s.getMeetingTimes());
            for(Course.Sections.MeetingTimes time : s.getMeetingTimes()) {
                View timeLayout  = inflater.inflate(R.layout.time, null);

                TextView dayText = (TextView) timeLayout.findViewById(R.id.day_text);
                TextView timeText = (TextView) timeLayout.findViewById(R.id.time_text);
                TextView locationText = (TextView) timeLayout.findViewById(R.id.sectionLocation_text);

                if(time.isByArrangement() ) {
                    dayText.setText(Html.fromHtml(SectionUtils.getMeetingDayName(time)));
                    timeText.setText(Html.fromHtml(SectionUtils.getMeetingHours(time)));
                    locationText.setText(Html.fromHtml(""));
                } else {
                    dayText.setText(Html.fromHtml(SectionUtils.getMeetingDayName(time)));
                    timeText.setText(Html.fromHtml(SectionUtils.getMeetingHours(time)));
                    locationText.setText(Html.fromHtml(SectionUtils.getClassLocation(time)));
                }

                sectionTimeContainer.addView(timeLayout);
            }
            sectionContainer.addView(section);


            section.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //TODO worked out a way to get the data on a clocked section by actually using
                    // the setTag() method to give the view some meaningful information for later use.
                    // When on click is called just use v.getTag() and it will return a custom object.
                    // I suggest using a Course object. Of course you'll now have to modify the
                    // Course object to hold semester, level and campus information.

                }
            });
        }
    }
}
