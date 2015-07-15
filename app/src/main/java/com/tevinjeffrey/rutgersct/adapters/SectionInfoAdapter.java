package com.tevinjeffrey.rutgersct.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.tevinjeffrey.rmp.RMP;
import com.tevinjeffrey.rmp.professor.Professor;
import com.tevinjeffrey.rmp.search.Decider;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.animator.EaseOutQuint;
import com.tevinjeffrey.rutgersct.animator.SectionInfoAnimator;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.rutgersapi.RutgersApiImpl;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Subject;
import com.tevinjeffrey.rutgersct.rutgersapi.utils.SectionUtils;
import com.tevinjeffrey.rutgersct.ui.MainActivity;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SectionInfoAdapter {

    private final Activity context;
    private final Request request;
    private final View rowView;

    @InjectView(R.id.prof_ratings_root)
    ViewGroup mRMPRoot;


    @InjectView(R.id.semesterText)
    TextView mSemesterText;

    @InjectView(R.id.course_title_text)
    TextView mCourseTitleText;

    @InjectView(R.id.sectionNumber_text)
    TextView mSectionNumberText;

    @InjectView(R.id.indexNumber_text)
    TextView mIndexNumberText;


    @InjectView(R.id.subtitle)
    TextView mCreditsText;


    @InjectView(R.id.instructors_text)
    TextView mInstructorsText;


    @InjectView(R.id.exam_code_text)
    TextView mExamCodeText;


    @InjectView(R.id.toolbar)
    Toolbar mToolbar;


    @InjectView(R.id.section_notes_text)
    TextView mSectionNotesText;


    @InjectView(R.id.section_notes_layout)
    RelativeLayout mSectionNotesLayout;


    @InjectView(R.id.section_comments_text)
    TextView mSectionCommentsText;


    @InjectView(R.id.section_comments_layout)
    RelativeLayout mSectionCommentsLayout;


    @InjectView(R.id.section_crosslist_text)
    TextView mSectionCrossListText;


    @InjectView(R.id.section_crosslist_layout)
    RelativeLayout mSectionCrossListLayout;


    @InjectView(R.id.section_subtitle_text)
    TextView mSectionSubtitleText;


    @InjectView(R.id.section_subtitle_layout)
    RelativeLayout mSectionSubtitleLayout;


    @InjectView(R.id.section_permission_text)
    TextView mSectionPermissionText;


    @InjectView(R.id.section_permission_layout)
    RelativeLayout mSectionPermissionLayout;


    @InjectView(R.id.section_open_to_text)
    TextView mSectionOpenToText;


    @InjectView(R.id.section_open_to_layout)
    RelativeLayout mSectionOpenLayout;


    @InjectView(R.id.section_times_container)
    LinearLayout mSectionTimeContainer;


    @InjectView(R.id.add_courses_fab)
    FloatingActionButton mFab;

    @InjectView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;


    private List<Course> courses;
    private Course courseData;
    private Course.Sections sectionData;

    private SectionInfoAdapter(Activity context, Request item, View rowView) {
        this.context = context;
        this.request = item;
        this.rowView = rowView;
    }

    public SectionInfoAdapter(Activity context, View rowView, Request item, List<Course> courses) {
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
        //setSearch(rmpSearch, googleSearch);

        setRMP(courseData, sectionData);
        setSemester(request);

        new SectionInfoAnimator(rowView).init();


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFab();
            }

            private void toggleFab() {
                if (DatabaseHandler.isSectionTracked(request)) {
                    ViewCompat.animate(mFab).setDuration(500).setInterpolator(new EaseOutQuint()).rotation(0);
                    ValueAnimator colorAnim = ObjectAnimator.ofInt(this, "backgroundColor",
                            context.getResources().getColor(R.color.accent_dark),
                            context.getResources().getColor(R.color.accent));
                    colorAnim.setDuration(500);
                    colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mFab.setBackgroundTintList(ColorStateList.valueOf((Integer)animation.getAnimatedValue()));
                        }
                    });
                    colorAnim.setEvaluator(new ArgbEvaluator());
                    colorAnim.start();

                    DatabaseHandler.getInstance().removeSectionFromDb(request);

                } else {
                    ViewCompat.animate(mFab).setDuration(500).setInterpolator(new EaseOutQuint()).rotation(225);

                    ValueAnimator colorAnim = ObjectAnimator.ofInt(this, "backgroundColor",
                            context.getResources().getColor(R.color.accent),
                            context.getResources().getColor(R.color.accent_dark));
                    colorAnim.setDuration(500);
                    colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mFab.setBackgroundTintList(ColorStateList.valueOf((Integer) animation.getAnimatedValue()));
                        }
                    });
                    colorAnim.setEvaluator(new ArgbEvaluator());
                    colorAnim.start();

                    DatabaseHandler.getInstance().addSectionToDb(request);
                }
            }
        });
    }

    private void setRMP(final Course courseData, final Course.Sections sectionData) {
        final RMP rmp = new RMP(RutgersCTApp.getClient());

        final List<Course.Sections.Instructors> professorsNotFound = new ArrayList<>(sectionData.getInstructors());

        Observable.from(sectionData.getInstructors())
                .filter(new Func1<Course.Sections.Instructors, Boolean>() {
                    @Override
                    public Boolean call(Course.Sections.Instructors instructors) {
                        return !instructors.getLastName().equals("STAFF");
                    }
                })
                .flatMap(new Func1<Course.Sections.Instructors, Observable<Professor>>() {
                    @Override
                    public Observable<Professor> call(Course.Sections.Instructors instructor) {
                        String campus = sectionData.getMeetingTimes().get(0).getCampus();
                        if (campus == null) {
                            campus = request.getLocations().get(0);
                        }

                        List<Subject> subjectsList = RutgersApiImpl.getSubjectsFromJson();
                        Subject matchingSubject = null;
                        for (Subject s : subjectsList) {
                            if (s.getCode().equals(courseData.getSubject())) {
                                matchingSubject = s;
                            }
                        }

                        final Decider.Parameter params = new Decider.Parameter("rutgers", matchingSubject.getDescription(),
                                campus, new Professor.Name(instructor.getFirstName(), instructor.getLastName()));


                        return rmp.findBestProfessor(params);
                    }
                })
                .doOnNext(new Action1<Professor>() {
                    @Override
                    public void call(Professor professor) {
                        for(final Iterator<Course.Sections.Instructors> iterator = professorsNotFound.iterator(); iterator.hasNext();) {
                            Course.Sections.Instructors i = iterator.next();
                            if(StringUtils.getJaroWinklerDistance(i.getLastName(), professor.getFullName().getLast()) > .70
                                    || StringUtils.getJaroWinklerDistance(i.getLastName(), professor.getFullName().getFirst()) > .70) {
                                iterator.remove();
                            }
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Professor>() {
                    @Override
                    public void onCompleted() {
                        for (Course.Sections.Instructors i : professorsNotFound) {
                            addRMPProfessor(RatingLayoutInflater.getErrorLayout(context, i.getName(), sectionData));
                        }
                        //mProgressBar.setVisibility(View.INVISIBLE);
                        //set indeterminte progress
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Professor professor) {
                        RatingLayoutInflater ratingLayoutInflater =
                                new RatingLayoutInflater(context, professor);

                        addRMPProfessor(ratingLayoutInflater.getLayout());
                    }
                });

    }


    private void addRMPProfessor(ViewGroup viewGroup) {
        ViewGroup container = ButterKnife.findById(mRMPRoot, R.id.prof_ratings_container);

        //Onclick intercepts vertical scroll
        /*viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object url =  v.getTag();
                if (url != null) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse((String) url));
                    context.startActivity(i);
                }
            }
        });*/
        container.addView(viewGroup);
    }

    private void setSemester(Request request) {
        mSemesterText.setText(request.getSemester().toString());
    }

    private void setActionButton(final FloatingActionButton fab) {
        if (DatabaseHandler.isSectionTracked(request)) {
            fab.post(new Runnable() {
                @Override
                public void run() {
                    ViewHelper.setRotation(fab, 225);
                    fab.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.accent_dark)));
                }
            });

        }
    }


    void setToolBarColor(Course.Sections section) {
        if (section.isOpenStatus()) {
            mCollapsingToolbar.setBackgroundColor(context.getResources().getColor(R.color.green));
            mCollapsingToolbar.setContentScrimColor(context.getResources().getColor(R.color.green));

            MainActivity.setGreenWindow(context);
        } else {
                //mToolbar.setBackgroundColor(context.getResources().getColor(R.color.red));
            mCollapsingToolbar.setBackgroundColor(context.getResources().getColor(R.color.red));
            mCollapsingToolbar.setContentScrimColor(context.getResources().getColor(R.color.red));


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
        mToolbar.setTitle("");
        mCourseTitleText.setText(course.getTrueTitle());
    }

    void setSectionNotes(Course.Sections section) {
        if (!section.hasNotes()) {
            mSectionNotesLayout.setVisibility(View.GONE);
        } else {
            mSectionNotesLayout.setVisibility(View.VISIBLE);
            mSectionNotesText.setText(Html.fromHtml(section.getSectionNotes()));
            mSectionNotesText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    void setSectionComments(Course.Sections section) {
        if (!section.hasComments()) {
            mSectionCommentsLayout.setVisibility(View.GONE);
        } else {
            mSectionCommentsLayout.setVisibility(View.VISIBLE);
            mSectionCommentsText.setText(Html.fromHtml(StringUtils.join(section.getComments(), ", ")));
            mSectionCommentsText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    void setSectionOpenTo(Course.Sections section) {
        if (!section.hasMajors()) {
            mSectionOpenLayout.setVisibility(View.GONE);
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
            mSectionOpenLayout.setVisibility(View.VISIBLE);
            mSectionOpenToText.setText(Html.fromHtml(sb.toString()));
        }
    }

    void setSectionPermission(Course.Sections section) {
        if (!section.hasSpecialPermission()) {
            mSectionPermissionLayout.setVisibility(View.GONE);
        } else {
            mSectionPermissionLayout.setVisibility(View.VISIBLE);
            mSectionPermissionText.setText(Html.fromHtml(section.getSpecialPermissionAddCodeDescription()));
            mSectionPermissionText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    void setSectionCrossList(Course.Sections section) {
        if (!section.hasCrossListed()) {
            mSectionCrossListLayout.setVisibility(View.GONE);
        } else {
            StringBuilder sb = new StringBuilder();
            for (Course.Sections.CrossListedSections i : section.getCrossListedSections()) {
                sb.append(i.getFullCrossListedSection());
                sb.append(", ");
            }
            mSectionCrossListLayout.setVisibility(View.VISIBLE);
            mSectionCrossListText.setText(Html.fromHtml(sb.toString()));
            mSectionCrossListText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    void setSectionSubtitle(Course.Sections section) {
        if (!section.hasSubtitle()) {
            mSectionSubtitleLayout.setVisibility(View.GONE);
        } else {
            mSectionSubtitleLayout.setVisibility(View.VISIBLE);
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

            View timeLayout = inflater.inflate(R.layout.section_item_time, mSectionTimeContainer, false);

            TextView dayText = ButterKnife.findById(timeLayout, R.id.section_item_time_info_day_text);
            TextView timeText = ButterKnife.findById(timeLayout, R.id.section_item_time_info_meeting_time_text);
            TextView locationText = ButterKnife.findById(timeLayout, R.id.section_item_time_info_location_text);
            TextView meetingTimeText = ButterKnife.findById(timeLayout, R.id.section_item_time_info_meeting_type);

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
