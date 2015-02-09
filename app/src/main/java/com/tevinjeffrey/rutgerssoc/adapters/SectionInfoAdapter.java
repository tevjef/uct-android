package com.tevinjeffrey.rutgerssoc.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.splunk.mint.Mint;
import com.splunk.mint.MintLogLevel;
import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.animator.EaseOutQuint;
import com.tevinjeffrey.rutgerssoc.animator.SectionInfoAnimator;
import com.tevinjeffrey.rutgerssoc.model.Course;
import com.tevinjeffrey.rutgerssoc.model.Request;
import com.tevinjeffrey.rutgerssoc.model.TrackedSections;
import com.tevinjeffrey.rutgerssoc.utils.CourseUtils;
import com.tevinjeffrey.rutgerssoc.utils.SectionUtils;
import com.tevinjeffrey.rutgerssoc.utils.UrlUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

public class SectionInfoAdapter {

    @InjectView(R.id.courseTitle_text)
    TextView mCourseTitleText;
    @InjectView(R.id.sectionNumber_text)
    TextView mSectionNumberText;
    @InjectView(R.id.shortenedCourseInfo)
    TextView mSectionNumberTitle;
    @InjectView(R.id.indexNumber_title)
    TextView mIndexNumberTitle;
    @InjectView(R.id.indexNumber_text)
    TextView mIndexNumberText;
    @InjectView(R.id.credits_title)
    TextView mCreditsTitle;
    @InjectView(R.id.subtitle)
    TextView mCreditsText;
    @InjectView(R.id.instructors_title)
    TextView mInstructorsTitle;
    @InjectView(R.id.instructors_text)
    TextView mInstructorsText;
    @InjectView(R.id.instructors_container)
    LinearLayout mInstructorsContainer;
    @InjectView(R.id.classSize_title)
    TextView mClassSizeTitle;
    @InjectView(R.id.classSize_text)
    TextView mClassSizeText;
    @InjectView(R.id.course_header_container)
    RelativeLayout mCourseHeaderContainer;
    @InjectView(R.id.toolbar_header_info)
    Toolbar mToolbarHeaderInfo;
    @InjectView(R.id.sectionNotes_title)
    TextView mSectionNotesTitle;
    @InjectView(R.id.sectionNotes_text)
    TextView mSectionNotesText;
    @InjectView(R.id.courseNotesContainer)
    RelativeLayout mSectionNotesContainer;
    @InjectView(R.id.sectionComments_title)
    TextView mSectionCommentsTitle;
    @InjectView(R.id.sectionComments_text)
    TextView mSectionCommentsText;
    @InjectView(R.id.subjectNotesContainer)
    RelativeLayout mSectionCommentsContainer;
    @InjectView(R.id.sectionCrossList_title)
    TextView mSectionCrossListTitle;
    @InjectView(R.id.sectionCrossList_text)
    TextView mSectionCrossListText;
    @InjectView(R.id.sectionCrossList_container)
    RelativeLayout mSectionCrossListContainer;
    @InjectView(R.id.sectionSubtitle_title)
    TextView mSectionSubtitleTitle;
    @InjectView(R.id.sectionSubtitle_text)
    TextView mSectionSubtitleText;
    @InjectView(R.id.sectionSubtitle_container)
    RelativeLayout mSectionSubtitleContainer;
    @InjectView(R.id.sectionPermission_title)
    TextView mSectionPermissionTitle;
    @InjectView(R.id.sectionPermission_text)
    TextView mSectionPermissionText;
    @InjectView(R.id.sectionPermisision_container)
    RelativeLayout mSectionPermisisionContainer;
    @InjectView(R.id.sectionOpenTo_title)
    TextView mSectionOpenToTitle;
    @InjectView(R.id.sectionOpenTo_text)
    TextView mSectionOpenToText;
    @InjectView(R.id.sectionOpenTo_container)
    RelativeLayout mSectionOpenToContainer;
    @InjectView(R.id.course_metadata)
    RelativeLayout mCourseMetadata;
    @InjectView(R.id.sectionNumber)
    TextView mTimeLocationTitle;
    @InjectView(R.id.sectionTimeContainer)
    LinearLayout mSectionTimeContainer;
    @InjectView(R.id.section_details)
    RelativeLayout mSectionDetails;
    @InjectView(R.id.sectionRoot)
    RelativeLayout mSectionRoot;
    @InjectView(R.id.sections_container)
    LinearLayout mSectionsContainer;
    @InjectView(R.id.bottomHalf)
    RelativeLayout mBottomHalf;
    @InjectView(R.id.fab)
    FloatingActionButton mFab;
    @InjectView(R.id.prof_rmp_search)
    Button rmpSearch;
    @InjectView(R.id.prof_google_search)
    Button googleSearch;
    private Activity context;
    private Request request;
    private List<Course> courses;
    private Course courseData;
    private Course.Sections sectionData;
    private View rowView;

    private SectionInfoAdapter(Activity context, Request item, View rowView) {
        this.context = context;
        this.request = item;
        this.rowView = rowView;
    }

    public SectionInfoAdapter(Activity context, Request item, View rowView, List<Course> courses) {
        this(context, item, rowView);
        this.courses = courses;

    }

    public void init() {
        ButterKnife.inject(this, rowView);

        for (Course c : courses) {
            for (Course.Sections s : c.getSections())
                if (s.getIndex().equals(request.getIndex())) {
                    sectionData = s;
                    courseData = c;
                }
        }
        setData();
    }

    private void setData() {
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
        setActionButton(mFab);
        setSearch(rmpSearch, googleSearch);

        new SectionInfoAnimator(rowView).init();

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFab();

            }

            private void toggleFab() {
                if (isSectionTracked()) {
                    animate(mFab).setDuration(500).setInterpolator(new EaseOutQuint()).rotation(0);
                    removeSectionFromDb();
                    Toast.makeText(context, "Stopped tracking",
                            Toast.LENGTH_SHORT).show();

                    ValueAnimator colorAnim = ObjectAnimator.ofInt(this, "backgroundColor", /*Red*/0xFF455A64, /*Blue*/0xFF607D8B);
                    colorAnim.setDuration(250);
                    colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mFab.setColorNormal((Integer) animation.getAnimatedValue());
                        }
                    });
                    colorAnim.setEvaluator(new ArgbEvaluator());
                    colorAnim.start();

                } else {
                    animate(mFab).setDuration(500).setInterpolator(new EaseOutQuint()).rotation(225);
                    addSectionToDb(request);
                    Toast.makeText(context, "Started tracking",
                            Toast.LENGTH_SHORT).show();
                    ValueAnimator colorAnim = ObjectAnimator.ofInt(this, "backgroundColor", /*Red*/0xFF607D8B, /*Blue*/0xFF455A64);
                    colorAnim.setDuration(250);
                    colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mFab.setColorNormal((Integer) animation.getAnimatedValue());
                        }
                    });
                    colorAnim.setEvaluator(new ArgbEvaluator());
                    colorAnim.start();
                }
            }
        });
    }

    private void setSearch(TextView rmpSearch, TextView googleSearch) {
        String str = StringUtils.join(sectionData.getInstructors(), " and ");

        googleSearch.setText(str);
        rmpSearch.setText(str);

        rmpSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchRmpSearch();
            }
        });

        googleSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGoogleSearch();
            }
        });
    }

    public void launchRmpSearch() {
        String url = "http://www.google.com/#q=" + UrlUtils.getRmpUrl(courseData, sectionData);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    public void launchGoogleSearch() {
        String url = "http://www.google.com/#q=" + UrlUtils.getGoogleUrl(courseData, sectionData);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    private void setActionButton(final FloatingActionButton fab) {
        if (isSectionTracked()) {
                /**/
            fab.post(new Runnable() {
                @Override
                public void run() {
                    fab.setRotation(225);
                    fab.setColorNormalResId(R.color.accent_dark);
                }
            });

        }
    }

    private void removeSectionFromDb() {
        List<TrackedSections> trackedSections = TrackedSections.find(TrackedSections.class,
                "INDEX_NUMBER = ?", sectionData.getIndex());
        for (TrackedSections ts : trackedSections) {
            ts.delete();
        }
    }

    private boolean isSectionTracked() {
        List<TrackedSections> trackedSections = TrackedSections.find(TrackedSections.class,
                "INDEX_NUMBER = ?", sectionData.getIndex());

        for (TrackedSections ts : trackedSections) {
            if (request.getIndex().equals(ts.getIndexNumber())) {
                return true;
            }
        }
        return false;
    }

    private void addSectionToDb(Request request) {
        Mint.logEvent("Sections Tracked", MintLogLevel.Info);

        TrackedSections trackedSections = new TrackedSections(request.getSubject(),
                request.getSemester(), Request.toStringList(request.getLocations()),
                Request.toStringList(request.getLevels()), request.getIndex());
        trackedSections.save();
    }


    public void setToolBarColor(Course.Sections section) {
        if (section.isOpenStatus()) {
            mToolbarHeaderInfo.setBackgroundColor(context.getResources().getColor(R.color.green));
            setGreenWindow();
        } else {
            mToolbarHeaderInfo.setBackgroundColor(context.getResources().getColor(R.color.red));
            setPrimaryWindow();
        }
    }

    public void setGreenWindow() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            Window window = context.getWindow();
            window.setStatusBarColor(context.getResources().getColor(R.color.green_dark));
            window.setNavigationBarColor(context.getResources().getColor(R.color.green_dark));
        }
    }

    public void setPrimaryWindow() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            Window window = context.getWindow();
            window.setStatusBarColor(context.getResources().getColor(R.color.primary_dark));
            window.setNavigationBarColor(context.getResources().getColor(R.color.primary_dark));
        }
    }

    public void setSectionNumber(Course.Sections section) {
        mSectionNumberText.setText(section.getNumber());
    }

    public void setSectionIndex(Course.Sections section) {
        mIndexNumberText.setText(section.getIndex());
    }

    public void setSectionCredits(Course course) {
        mCreditsText.setText(String.valueOf(course.getCredits()));
    }

    public void setCourseTitle(Course course) {
        mCourseTitleText.setText(CourseUtils.getTitle(course));
    }

    public void setSectionNotes(Course.Sections section) {
        if (!section.hasNotes()) {
            mSectionNotesContainer.setVisibility(View.GONE);
        } else {
            mSectionNotesText.setText(Html.fromHtml(section.getSectionNotes()));
        }
    }

    public void setSectionComments(Course.Sections section) {
        if (!section.hasComments()) {
            mSectionCommentsContainer.setVisibility(View.GONE);
        } else {
            StringBuilder sb = new StringBuilder();
            for (Course.Sections.Comments i : section.getComments()) {
                sb.append(i.getDescription());
                sb.append(", ");
            }
            mSectionCommentsText.setText(Html.fromHtml(sb.toString()));
        }
    }

    public void setSectionOpenTo(Course.Sections section) {
        if (!section.hasMajors()) {
            mSectionOpenToContainer.setVisibility(View.GONE);
        } else {
            boolean isMajorHeaderSet = false;
            boolean isUnitHeaderSet = false;
            StringBuilder sb = new StringBuilder();
            for (Course.Sections.Majors i : section.getMajors()) {
                if (i.isMajorCode()) {
                    if (!isMajorHeaderSet) {
                        isMajorHeaderSet = true;
                        sb.append("MAJORS: ");
                    }
                    sb.append(i.getCode());
                    sb.append(", ");
                } else if (i.isUnitCode()) {
                    if (!isUnitHeaderSet) {
                        isUnitHeaderSet = true;
                        sb.append("SCHOOLS: ");
                    }
                    sb.append(i.getCode());
                    sb.append(", ");
                }
            }
            mSectionOpenToText.setText(Html.fromHtml(sb.toString()));
        }
    }

    public void setSectionPermission(Course.Sections section) {
        if (!section.hasSpecialPermission()) {
            mSectionPermisisionContainer.setVisibility(View.GONE);
        } else {
            mSectionPermissionText.setText(Html.fromHtml(section.getSpecialPermissionAddCodeDescription()));
        }
    }

    public void setSectionCrossList(Course.Sections section) {
        if (!section.hasCrossListed()) {
            mSectionCrossListContainer.setVisibility(View.GONE);
        } else {
            StringBuilder sb = new StringBuilder();
            for (Course.Sections.CrossListedSections i : section.getCrossListedSections()) {
                sb.append(i.getFullCrossListedSection());
                sb.append(", ");
            }
            mSectionCrossListText.setText(Html.fromHtml(sb.toString()));
        }
    }

    public void setSectionSubtitle(Course.Sections section) {
        if (!section.hasSubtitle()) {
            mSectionSubtitleContainer.setVisibility(View.GONE);
        } else {
            mSectionSubtitleText.setText(Html.fromHtml(section.getSubtitle()));
        }
    }

    public void setSectionSize(Course.Sections section) {
        mClassSizeText.setText(String.valueOf(section.getStopPoint()));
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

            mSectionTimeContainer.addView(timeLayout);
        }
    }

    public void setInstructors(Course.Sections s) {
        StringBuilder sb = new StringBuilder();
        for (Course.Sections.Instructors i : s.getInstructors()) {
            sb.append(i.getName());
            sb.append(" | ");
        }
        mInstructorsText.setText(UrlUtils.trimTrailingChar(sb.toString(), '|'));
    }
}
