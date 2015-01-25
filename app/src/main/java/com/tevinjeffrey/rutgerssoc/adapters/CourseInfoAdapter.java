package com.tevinjeffrey.rutgerssoc.adapters;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.model.Request;
import com.tevinjeffrey.rutgerssoc.model.Course;
import com.tevinjeffrey.rutgerssoc.ui.SectionInfoFragment;
import com.tevinjeffrey.rutgerssoc.utils.CourseUtils;
import com.tevinjeffrey.rutgerssoc.utils.SectionUtils;

import java.util.Collections;
import java.util.List;

public class CourseInfoAdapter {

    private final Activity context;
    private final Course course;
    private final View rowView;
    private Request request;

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

    private LayoutInflater inflater;

    private CourseInfoAdapter(Activity context, Course item, View rowView) {
        this.context = context;
        this.course = item;
        this.rowView = rowView;
    }

    public CourseInfoAdapter(Activity context, Course course, View rowView, Request request) {
        this(context, course, rowView);
        this.request = request;
    }

    public CourseInfoAdapter init() {
        courseTitle = (TextView) rowView.findViewById(R.id.sectionNumber_text);
        shortenedCourseInfo = (TextView) rowView.findViewById(R.id.sectionNumber_title);
        credits = (TextView) rowView.findViewById(R.id.credits_text);
        courseNotes = (TextView) rowView.findViewById(R.id.courseNotes_text);
        subjectNotes = (TextView) rowView.findViewById(R.id.subjectNotes_text);
        courseNotesContainer = rowView.findViewById(R.id.sectionNotes_container);
        subjectNotesContainer = rowView.findViewById(R.id.sectionComments_container);
        preReqNotesContainer = rowView.findViewById(R.id.prereq_container);
        sectionContainer = (LinearLayout) rowView.findViewById(R.id.sections_container);
        openSections = (TextView) rowView.findViewById(R.id.openSections_text);
        totalSections = (TextView) rowView.findViewById(R.id.totalSections_text);
        preReqNotes = (TextView) rowView.findViewById(R.id.prereq_text);

        setData();

        return this;
    }

    private void setData() {
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

    void setCourseTitle(Course course) {
        this.courseTitle.setText(CourseUtils.getTitle(course));
    }

    void setShortenedCourseInfo(Course course) {
        this.shortenedCourseInfo.setText(formatShortenedCourseInfo(course));
    }

    String formatShortenedCourseInfo(Course course) {
        String offeringUnitCode = course.getOfferingUnitCode();
        String subject = course.getSubject();
        String courseNumber = course.getCourseNumber();
        return offeringUnitCode + ":" + subject + ":" + courseNumber;
    }

    void setCredits(Course course) {
        this.credits.setText(String.valueOf(course.getCredits()));
    }

    void setCourseNotes(Course course) {
        if (course.getCourseNotes() == null) {
            courseNotesContainer.setVisibility(View.GONE);
        } else {
            this.courseNotes.setText(Html.fromHtml(course.getCourseNotes()));
        }
    }

    void setSubjectNotes(Course course) {
        if (course.getSubjectNotes() == null) {
            subjectNotesContainer.setVisibility(View.GONE);
        } else {
            this.subjectNotes.setText(Html.fromHtml(course.getSubjectNotes()));
        }
    }
    void setOpenSections(Course course) {
        this.openSections.setText(String.valueOf(course.getOpenSections()));
    }

    void setTotalSections(Course course) {
        this.totalSections.setText(String.valueOf(course.getSectionsTotal()));
    }

    void setPreReqNotes(Course course) {
        if (course.getPreReqNotes() == null) {
            preReqNotesContainer.setVisibility(View.GONE);
        } else {
            this.preReqNotes.setText(Html.fromHtml(course.getPreReqNotes()));
            this.preReqNotes.setMovementMethod(new ScrollingMovementMethod());
        }
    }

    void setSections(List<Course.Sections> sections) {

        new SectionAdapter(sectionContainer, sections).init();
    }

    public class SectionAdapter {

        private View section;
        private TextView sectionNumber;
        private TextView instructors;
        private LinearLayout sectionTimeContainer;
        private final List<Course.Sections> sectionData;


        public SectionAdapter(View sectionContainer, List<Course.Sections> sectionData) {
            this.sectionData = sectionData;
        }

        public void init() {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            for(Course.Sections s: sectionData) {
                Log.d("tag", "adding section " + s.getNumber());
                setData(s);
            }

        }

        public void setData(Course.Sections s) {


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
            section  = inflater.inflate(R.layout.sections, null);
            sectionNumber = (TextView) section.findViewById(R.id.timeLocation_title);
            instructors = (TextView) section.findViewById(R.id.prof_text);
            sectionTimeContainer = (LinearLayout) section.findViewById(R.id.section_time_container);

            setOpenStatus(s);
            setSectionNumber(s);
            setInstructors(s);
            setTimes(s);
            setOnClickSectionClickListener();

            Request trackableRequest = new Request(request.getSubject(), request.getSemester(),
                    request.getLocations(), request.getLevels(), s.getIndex());


            section.setTag(trackableRequest);
            sectionContainer.addView(section);

        }

        private void setOnClickSectionClickListener() {
            section.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Request t = (Request) v.getTag();
                    Log.d("tag", "section clicked, index " + t.getIndex() );

                    createFragment(createArgs(t));
                    //TODO worked out a way to get the data on a clocked section by actually using
                    // the setTag() method to give the view some meaningful information for later use.
                    // When on click is called just use v.getTag() and it will return a custom object.
                    // I suggest using a Course object. Of course you'll now have to modify the
                    // Course object to hold semester, level and campus information.
                }
            });
        }

        private void createFragment(Bundle b) {
            Fragment sectionInfoFragment = new SectionInfoFragment();
            sectionInfoFragment.setArguments(b);
            context.getFragmentManager().beginTransaction()
                    .replace(R.id.container, sectionInfoFragment).addToBackStack(null)
                    .commit();
        }

        private Bundle createArgs(Parcelable parcelable) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("request", parcelable);
            return bundle;
        }

        public void setTimes(Course.Sections s) {
            //sort times so that Monday > Tuesday and Lecture > Recitation
            Collections.sort(s.getMeetingTimes());
            for (Course.Sections.MeetingTimes time : s.getMeetingTimes()) {

                Log.d("tag", "adding time to section " + s.getNumber());

                View timeLayout = inflater.inflate(R.layout.time, null);

                TextView dayText = (TextView) timeLayout.findViewById(R.id.day_text);
                TextView timeText = (TextView) timeLayout.findViewById(R.id.time_text);
                TextView locationText = (TextView) timeLayout.findViewById(R.id.sectionLocation_text);

                if (time.isByArrangement()) {
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
        }
        private void setSectionNumber(Course.Sections s) {
            Log.d("tag", "adding section number " + s.getNumber());

            sectionNumber.setText(s.getNumber());
        }
        private void setInstructors(Course.Sections s) {
            Log.d("tag", "adding instructors to section " + s.getNumber());

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
        }

        public void setOpenStatus(Course.Sections s) {
            Log.d("tag", "setting status to section " + s.getNumber());

            if(s.isOpenStatus()) {
                sectionNumber.setBackgroundColor(context.getResources().getColor(R.color.green));
            } else {
                sectionNumber.setBackgroundColor(context.getResources().getColor(R.color.red));
            }
        }
    }
}
