package com.tevinjeffrey.rutgersct.animator;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.tevinjeffrey.rutgersct.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

public class SectionInfoAnimator {

    private final View rootView;
    @SuppressWarnings("WeakerAccess")
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
    @SuppressWarnings("WeakerAccess")
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
    @InjectView(R.id.toolbar)
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
    RelativeLayout mSectionPermissionContainer;
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
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.fab)
    FloatingActionButton mFab;

    public SectionInfoAnimator(View rootView) {
        this.rootView = rootView;
    }

    public void init() {

        ButterKnife.inject(this, rootView);

        mFab.setVisibility(View.VISIBLE);
        AnimatorSet set7 = new AnimatorSet();
        set7.playTogether(
                //ObjectAnimator.ofFloat(mFab, "translationX", mFab.getX() + 200, mFab.getX()),
                ObjectAnimator.ofFloat(mFab, "scaleX", 0, 1),
                ObjectAnimator.ofFloat(mFab, "scaleY", 0, 1),
                ObjectAnimator.ofFloat(mFab, "alpha", 0, 1)

        );
        set7.setInterpolator(new EaseOutQuint());
        set7.setStartDelay(200);
        set7.setDuration(250).start();

    }
}
