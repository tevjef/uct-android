package com.tevinjeffrey.rutgerssoc.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.model.Request;
import com.tevinjeffrey.rutgerssoc.model.Course;
import com.tevinjeffrey.rutgerssoc.utils.CourseUtils;
import com.tevinjeffrey.rutgerssoc.utils.SectionUtils;

import java.util.Collections;
import java.util.List;

public class SectionInfoAdapter {

    private Activity context;
    private Request request;
    private List<Course> courses;
    private Course courseData;
    private Course.Sections sectionData;
    private View rowView;

    private Toolbar toolBar;
    private TextView sectionNumber;
    private TextView sectionIndex;
    private TextView sectionCredits;
    private TextView courseTitle;

    private TextView sectionNotes;
    private TextView sectionComments;
    private TextView sectionOpenTo;
    private TextView sectionPermission;
    private TextView sectionCrossList;
    private TextView sectionSubtitle;
    private TextView sectionSize;
    private TextView instructors;


    private View sectionNotesContainer;
    private View sectionCommentsContainer;
    private View sectionOpenToContainer;
    private View sectionCrossListContainer;
    private View sectionSubtitleContainer;
    private View sectionPermissionContainer;

    private LinearLayout sectionTimeContainer;

    private SectionInfoAdapter(Activity context, Request item, View rowView) {
        this.context = context;
        this.request = item;
        this.rowView = rowView;
    }
    
    public SectionInfoAdapter(Activity context, Request item, View rowView, List<Course> courses) {
        this(context,item, rowView);
        this.courses = courses;
    }

    public void init() {
        toolBar = (Toolbar) rowView.findViewById(R.id.toolbar_header_info);
        sectionNumber = (TextView) rowView.findViewById(R.id.sectionNumber_text);
        sectionIndex = (TextView) rowView.findViewById(R.id.indexNumber_text);
        sectionCredits = (TextView) rowView.findViewById(R.id.credits_text);
        courseTitle = (TextView) rowView.findViewById(R.id.courseTitle_text);
        sectionNotes = (TextView) rowView.findViewById(R.id.sectionNotes_text);
        sectionComments = (TextView) rowView.findViewById(R.id.sectionComments_text);
        sectionOpenTo = (TextView) rowView.findViewById(R.id.sectionOpenTo_text);
        sectionPermission = (TextView) rowView.findViewById(R.id.sectionPermission_text);
        sectionCrossList = (TextView) rowView.findViewById(R.id.sectionCrossList_text);
        sectionSubtitle = (TextView) rowView.findViewById(R.id.sectionSubtitle_text);
        sectionSize = (TextView) rowView.findViewById(R.id.classSize_text);
        instructors = (TextView) rowView.findViewById(R.id.instructors_text);

        sectionCrossListContainer = rowView.findViewById(R.id.sectionCrossList_container);
        sectionSubtitleContainer = rowView.findViewById(R.id.sectionSubtitle_container);
        sectionPermissionContainer = rowView.findViewById(R.id.sectionPermisision_container);

        sectionNotesContainer = rowView.findViewById(R.id.sectionNotes_container);
        sectionCommentsContainer = rowView.findViewById(R.id.sectionComments_container);
        sectionOpenToContainer = rowView.findViewById(R.id.sectionOpenTo_container);

        sectionTimeContainer = (LinearLayout) rowView.findViewById(R.id.section_time_container);

        for (Course c : courses) {
            for (Course.Sections s : c.getSections())
                if (s.getIndex().equals(request.getIndex())) {
                    sectionData = s;
                    courseData = c;
                }
        }

        setData();
    }

    private void setData(){
        setToolBarColor(sectionData);
        setSectionNumber(sectionData);
        setSectionIndex(sectionData);
        setSectionCredits(courseData);
        setCourseTitle(courseData);
        setSectionNotes(sectionData);
        setSectionNotes(sectionData);
        setSectionComments(sectionData);
        setSectionOpenTo(sectionData);
        setSectionPermission(sectionData);
        setSectionCrossList(sectionData);
        setSectionSubtitle(sectionData);
        setSectionSize(sectionData);
        setTimes(sectionData);
        setInstructors(sectionData);
    }



    public void setToolBarColor(Course.Sections section) {
        if(section.isOpenStatus()) {
            toolBar.setBackgroundColor(context.getResources().getColor(R.color.green));
            setGreenWindow();
        } else {
            toolBar.setBackgroundColor(context.getResources().getColor(R.color.red));
            setPrimaryWindow();
        }
    }
    public void setGreenWindow() {
        Window window = context.getWindow();
        window.setStatusBarColor(context.getResources().getColor(R.color.green_dark));
        window.setNavigationBarColor(context.getResources().getColor(R.color.green_dark));
    }
    public void setPrimaryWindow() {
        Window window = context.getWindow();
        window.setStatusBarColor(context.getResources().getColor(R.color.primary_dark));
        window.setNavigationBarColor(context.getResources().getColor(R.color.primary_dark));
    }

    public void setSectionNumber(Course.Sections section) {
        sectionNumber.setText(section.getNumber());
    }

    public void setSectionIndex(Course.Sections section) {
        sectionIndex.setText(section.getIndex());
    }

    public void setSectionCredits(Course course) {
        sectionCredits.setText(String.valueOf(course.getCredits()));
    }

    public void setCourseTitle(Course course) {
        this.courseTitle.setText(CourseUtils.getTitle(course));
    }

    public void setSectionNotes(Course.Sections section) {
        if (section.getSectionNotes() == null) {
            sectionNotesContainer.setVisibility(View.GONE);
        } else {
            this.sectionNotes.setText(Html.fromHtml(section.getSectionNotes()));
        }
    }

    public void setSectionComments(Course.Sections section) {
        if (section.getComments().size() == 0) {
            sectionCommentsContainer.setVisibility(View.GONE);
        } else {
            StringBuilder sb= new StringBuilder();
            for(Course.Sections.Comments i : section.getComments()) {
                    sb.append(i.getDescription());
                    sb.append(", ");
            }
            this.sectionComments.setText(Html.fromHtml(sb.toString()));
        }
    }

    public void setSectionOpenTo(Course.Sections section) {
        if (section.getMajors().size() == 0) {
            sectionOpenToContainer.setVisibility(View.GONE);
        } else {
            boolean isMajorHeaderSet = false;
            boolean isUnitHeaderSet = false;
            StringBuilder sb= new StringBuilder();
            for(Course.Sections.Majors i : section.getMajors()) {
                if(i.isMajorCode()) {
                    if(!isMajorHeaderSet) {
                        isMajorHeaderSet = true;
                        sb.append("MAJORS: ");
                    }
                    sb.append(i.getCode());
                    sb.append(", ");
                } else if(i.isUnitCode()) {
                    if(!isUnitHeaderSet) {
                        isUnitHeaderSet = true;
                        sb.append("SCHOOLS: ");
                    }
                    sb.append(i.getCode());
                    sb.append(", ");
                }
            }
            this.sectionOpenTo.setText(Html.fromHtml(sb.toString()));
        }
    }

    public void setSectionPermission(Course.Sections section) {
        if (section.getSpecialPermissionAddCodeDescription() == null) {
            sectionPermissionContainer.setVisibility(View.GONE);
        } else {
            this.sectionPermission.setText(Html.fromHtml(section.getSpecialPermissionAddCodeDescription()));
        }
    }

    public void setSectionCrossList(Course.Sections section) {
        if (section.getCrossListedSections().size() == 0) {
            sectionCrossListContainer.setVisibility(View.GONE);
        } else {
            StringBuilder sb= new StringBuilder();
            for(Course.Sections.CrossListedSections i : section.getCrossListedSections()) {
                sb.append(i.getFullCrossListedSection());
                sb.append(", ");
            }
            this.sectionCrossList.setText(Html.fromHtml(sb.toString()));
        }
    }

    public void setSectionSubtitle(Course.Sections section) {
        if (section.getSubtitle() == null) {
            sectionSubtitleContainer.setVisibility(View.GONE);
        } else {
            this.sectionSubtitle.setText(Html.fromHtml(section.getSubtitle()));
        }
    }

    public void setSectionSize(Course.Sections section) {
            this.sectionSize.setText(String.valueOf(section.getStopPoint()));
    }

    public void setTimes(Course.Sections s) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //sort times so that Monday > Tuesday and Lecture > Recitation
        Collections.sort(s.getMeetingTimes());
        for (Course.Sections.MeetingTimes time : s.getMeetingTimes()) {

            Log.d("tag", "adding time to section " + s.getNumber());

            View timeLayout = inflater.inflate(R.layout.section_info_time, null);

            TextView dayText = (TextView) timeLayout.findViewById(R.id.day_text);
            TextView timeText = (TextView) timeLayout.findViewById(R.id.time_text);
            TextView locationText = (TextView) timeLayout.findViewById(R.id.sectionLocation_text);
            TextView meetingTimeText = (TextView) timeLayout.findViewById(R.id.meetingType);


            dayText.setText(Html.fromHtml(SectionUtils.getMeetingDayName(time)));
            timeText.setText(Html.fromHtml(SectionUtils.getMeetingHours(time)));
            locationText.setText(Html.fromHtml(SectionUtils.getClassLocation(time)));
            meetingTimeText.setText(time.getMeetingModeDesc());

            sectionTimeContainer.addView(timeLayout);
        }
    }

    public void setInstructors(Course.Sections s) {
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
}
