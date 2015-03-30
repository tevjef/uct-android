package com.tevinjeffrey.rutgersct.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.animator.EaseOutQuint;
import com.tevinjeffrey.rutgersct.animator.SectionInfoAnimator;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.model.Course;
import com.tevinjeffrey.rutgersct.model.Request;
import com.tevinjeffrey.rutgersct.ui.MainActivity;
import com.tevinjeffrey.rutgersct.utils.SectionUtils;
import com.tevinjeffrey.rutgersct.utils.UrlUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

public class SectionInfoAdapter {

    private final Activity context;
    private final Request request;
    private final View rowView;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.semesterText)
    TextView mSemesterText;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.courseTitle_text)
    TextView mCourseTitleText;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.sectionNumber_text)
    TextView mSectionNumberText;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.indexNumber_text)
    TextView mIndexNumberText;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.subtitle)
    TextView mCreditsText;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.instructors_text)
    TextView mInstructorsText;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.exam_code_text)
    TextView mExamCodeText;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.sectionNotes_text)
    TextView mSectionNotesText;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.courseNotesContainer)
    RelativeLayout mSectionNotesContainer;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.sectionComments_text)
    TextView mSectionCommentsText;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.subjectNotesContainer)
    RelativeLayout mSectionCommentsContainer;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.sectionCrossList_text)
    TextView mSectionCrossListText;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.sectionCrossList_container)
    RelativeLayout mSectionCrossListContainer;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.sectionSubtitle_text)
    TextView mSectionSubtitleText;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.sectionSubtitle_container)
    RelativeLayout mSectionSubtitleContainer;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.sectionPermission_text)
    TextView mSectionPermissionText;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.sectionPermisision_container)
    RelativeLayout mSectionPermisisionContainer;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.sectionOpenTo_text)
    TextView mSectionOpenToText;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.sectionOpenTo_container)
    RelativeLayout mSectionOpenToContainer;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.sectionTimeContainer)
    LinearLayout mSectionTimeContainer;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.sectionRoot)
    RelativeLayout mSectionRoot;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.fab)
    FloatingActionButton mFab;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.prof_rmp_search)
    Button rmpSearch;

    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.prof_google_search)
    Button googleSearch;

    private List<Course> courses;
    private Course courseData;
    private Course.Sections sectionData;

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
            for (Course.Sections s : c.getSections()) {
                //Get the correct section from the list.
                if (s.getIndex().equals(request.getIndex())) {
                    sectionData = s;
                    courseData = c;
                }
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
        setSectionComments(sectionData);
        setSectionOpenTo(sectionData);
        setSectionPermission(sectionData);
        setSectionCrossList(sectionData);
        setSectionSubtitle(sectionData);
        setExamCode(sectionData);
        setTimes(sectionData);
        setInstructors(sectionData);
        setActionButton(mFab);
        setSearch(rmpSearch, googleSearch);
        setSemester(request);

        if (context.getFragmentManager().getBackStackEntryCount() > 1) {
            new SectionInfoAnimator(rowView).init();
        }

        mFab.setColorNormalResId(R.color.accent);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFab();
            }

            private void toggleFab() {
                if (DatabaseHandler.isSectionTracked(request)) {
                    animate(mFab).setDuration(500).setInterpolator(new EaseOutQuint()).rotation(0);
                    DatabaseHandler.removeSectionFromDb(request);
                    ValueAnimator colorAnim = ObjectAnimator.ofInt(this, "backgroundColor",
                            context.getResources().getColor(R.color.accent_dark),
                            context.getResources().getColor(R.color.accent));
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
                    DatabaseHandler.addSectionToDb(request);
                    ValueAnimator colorAnim = ObjectAnimator.ofInt(this, "backgroundColor",
                            context.getResources().getColor(R.color.accent),
                            context.getResources().getColor(R.color.accent_dark));
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

    private void setSemester(Request request) {
        mSemesterText.setText(request.getSemester().toString());
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

    void launchRmpSearch() {
        String url = "http://www.google.com/#q=" + UrlUtils.getRmpUrl(sectionData);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    void launchGoogleSearch() {
        String url = "http://www.google.com/#q=" + UrlUtils.getGoogleUrl(sectionData);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    private void setActionButton(final FloatingActionButton fab) {
        if (DatabaseHandler.isSectionTracked(request)) {
            fab.post(new Runnable() {
                @Override
                public void run() {
                    fab.setRotation(225);
                    fab.setColorNormalResId(R.color.accent_dark);
                }
            });

        }
    }


    void setToolBarColor(Course.Sections section) {
        if (section.isOpenStatus()) {
            if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) {
                mToolbar.setBackgroundColor(context.getResources().getColor(R.color.green));
            } else {
                mToolbar.setBackgroundResource(android.R.color.transparent);
            }
            MainActivity.setGreenWindow(context);
        } else {
            if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) {
                mToolbar.setBackgroundColor(context.getResources().getColor(R.color.red));
            } else {
                mToolbar.setBackgroundResource(android.R.color.transparent);
            }
            MainActivity.setPrimaryWindow(context);
        }
    }

    void setSectionNumber(Course.Sections section) {
        mSectionNumberText.setText(section.getNumber());
    }

    void setSectionIndex(Course.Sections section) {
        mIndexNumberText.setText(section.getIndex());
    }

    void setSectionCredits(Course course) {
        mCreditsText.setText(String.valueOf(course.getCredits()));
    }

    void setCourseTitle(Course course) {
        mCourseTitleText.setText(course.getTrueTitle());
    }

    void setSectionNotes(Course.Sections section) {
        if (!section.hasNotes()) {
            mSectionNotesContainer.setVisibility(View.GONE);
        } else {
            mSectionNotesText.setText(Html.fromHtml(section.getSectionNotes()));
            mSectionNotesText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    void setSectionComments(Course.Sections section) {
        if (!section.hasComments()) {
            mSectionCommentsContainer.setVisibility(View.GONE);
        } else {
            mSectionCommentsText.setText(Html.fromHtml(StringUtils.join(section.getComments(), ", ")));
            mSectionCommentsText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    void setSectionOpenTo(Course.Sections section) {
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

    void setSectionPermission(Course.Sections section) {
        if (!section.hasSpecialPermission()) {
            mSectionPermisisionContainer.setVisibility(View.GONE);
        } else {
            mSectionPermissionText.setText(Html.fromHtml(section.getSpecialPermissionAddCodeDescription()));
            mSectionPermissionText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    void setSectionCrossList(Course.Sections section) {
        if (!section.hasCrossListed()) {
            mSectionCrossListContainer.setVisibility(View.GONE);
        } else {
            StringBuilder sb = new StringBuilder();
            for (Course.Sections.CrossListedSections i : section.getCrossListedSections()) {
                sb.append(i.getFullCrossListedSection());
                sb.append(", ");
            }
            mSectionCrossListText.setText(Html.fromHtml(sb.toString()));
            mSectionCrossListText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    void setSectionSubtitle(Course.Sections section) {
        if (!section.hasSubtitle()) {
            mSectionSubtitleContainer.setVisibility(View.GONE);
        } else {
            mSectionSubtitleText.setText(Html.fromHtml(section.getSubtitle()));
            mSectionSubtitleText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    void setExamCode(Course.Sections section) {
        mExamCodeText.setText(String.valueOf(section.getExamCode()));
    }

    void setTimes(Course.Sections s) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //sort times so that Monday > Tuesday and Lecture > Recitation
        Collections.sort(s.getMeetingTimes());
        for (Course.Sections.MeetingTimes time : s.getMeetingTimes()) {

            View timeLayout = inflater.inflate(R.layout.section_info_time, mSectionTimeContainer, false);

            TextView dayText = ButterKnife.findById(timeLayout, R.id.day_text);
            TextView timeText = ButterKnife.findById(timeLayout, R.id.time_text);
            TextView locationText = ButterKnife.findById(timeLayout, R.id.sectionLocation_text);
            TextView meetingTimeText = ButterKnife.findById(timeLayout, R.id.meetingType);

            dayText.setText(SectionUtils.getMeetingDayName(time));
            timeText.setText(SectionUtils.getMeetingHours(time));
            locationText.setText(SectionUtils.getClassLocation(time));
            meetingTimeText.setText(time.getMeetingModeDesc());

            mSectionTimeContainer.addView(timeLayout);
        }
    }

    void setInstructors(Course.Sections s) {
        mInstructorsText.setText(s.getToStringInstructors(" | "));
    }
}
