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

        long base = 50;

        animate(mCreditsTitle).translationY(mCourseTitleText.getY()).setDuration(600).setStartDelay(1000).start();

        AnimatorSet setCTT = new AnimatorSet();
        setCTT.setDuration(200);
        setCTT.playTogether(
                ObjectAnimator.ofFloat(mCourseTitleText, "translationY", -mCourseTitleText.getY()),
                ObjectAnimator.ofFloat(mCourseTitleText, "alpha", 0, 1)

        );
        setCTT.setInterpolator(new EaseOutQuint());
        setCTT.setStartDelay(500);
        //setCTT.setDuration(500).start();

        /*AnimatorSet set1 = new AnimatorSet();
        set1.setDuration(200);
        set1.playTogether(
                ObjectAnimator.ofFloat(mSectionNumberTitle, "scaleX", 0, 1),
                ObjectAnimator.ofFloat(mSectionNumberTitle, "scaleY", 0, 1),
                ObjectAnimator.ofFloat(mSectionNumberTitle, "alpha", 0, 1)


        );
        set1.setInterpolator(new EaseOutQuint());
        set1.setStartDelay(100);
        set1.setDuration(500).start();

        AnimatorSet set2 = new AnimatorSet();
        set2.setDuration(200);
        set2.playTogether(
                ObjectAnimator.ofFloat(mSectionNumberText, "scaleX", 0, 1),
                ObjectAnimator.ofFloat(mSectionNumberText, "scaleY", 0, 1),
                ObjectAnimator.ofFloat(mSectionNumberText, "alpha", 0, 1)

        );
        set2.setInterpolator(new EaseOutQuint());
        set2.setStartDelay(150);
        set2.setDuration(500).start();

        AnimatorSet set3 = new AnimatorSet();
        set3.setDuration(200);
        set3.playTogether(
                ObjectAnimator.ofFloat(mIndexNumberTitle, "scaleX", 0, 1),
                ObjectAnimator.ofFloat(mIndexNumberTitle, "scaleY", 0, 1)
        );
        set3.setInterpolator(new EaseOutQuint());
        set3.setStartDelay(200);
        set3.setDuration(500).start();

        AnimatorSet set4 = new AnimatorSet();
        set4.setDuration(200);
        set4.playTogether(
                ObjectAnimator.ofFloat(mIndexNumberText, "scaleX", 0, 1),
                ObjectAnimator.ofFloat(mIndexNumberText, "scaleY", 0, 1)
        );
        set4.setInterpolator(new EaseOutQuint());
        set4.setStartDelay(250);
        set4.setDuration(500).start();

        AnimatorSet set5 = new AnimatorSet();
        set5.setDuration(200);
        set5.playTogether(
                ObjectAnimator.ofFloat(mCreditsTitle, "scaleX", 0, 1),
                ObjectAnimator.ofFloat(mCreditsTitle, "scaleY", 0, 1)
        );
        set5.setInterpolator(new EaseOutQuint());
        set5.setStartDelay(300);
        set5.setDuration(500).start();

        AnimatorSet set6 = new AnimatorSet();
        set6.setDuration(200);
        set6.playTogether(
                ObjectAnimator.ofFloat(mCreditsText, "scaleX", 0, 1),
                ObjectAnimator.ofFloat(mCreditsText, "scaleY", 0, 1)
        );
        set6.setInterpolator(new EaseOutQuint());
        set6.setStartDelay(350);
        set6.setDuration(500).start();
*/

        AnimatorSet set7 = new AnimatorSet();
        set7.playTogether(
                ObjectAnimator.ofFloat(mFab, "scaleX", 0, 1),
                ObjectAnimator.ofFloat(mFab, "scaleY", 0, 1),
                ObjectAnimator.ofFloat(mFab, "alpha", 0, 1)

        );
        set7.setInterpolator(new EaseOutQuint());
        set7.setStartDelay(200);
        set7.setDuration(250).start();
/*
        AnimatorSet set8 = new AnimatorSet();
        set8.setDuration(200);
        set8.playTogether(
                ObjectAnimator.ofFloat(mClassSizeTitle, "scaleX", 0, 1),
                ObjectAnimator.ofFloat(mClassSizeTitle, "scaleY", 0, 1)
        );
        set8.setInterpolator(new EaseOutQuint());
        set8.setStartDelay(450);
        set8.setDuration(500).start();

        AnimatorSet set9 = new AnimatorSet();
        set9.setDuration(200);
        set9.playTogether(
                ObjectAnimator.ofFloat(mClassSizeText, "scaleX", 0, 1),
                ObjectAnimator.ofFloat(mClassSizeText, "scaleY", 0, 1)
        );
        set9.setInterpolator(new EaseOutQuint());
        set9.setStartDelay(500);
        set9.setDuration(500).start();

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(mInstructorsContainer, "translationY", 0, mInstructorsContainer.getHeight()),
                ObjectAnimator.ofFloat(mInstructorsContainer, "alpha", 0, 1)
        );
        set.setStartDelay(550);
        set.setInterpolator(new EaseOutQuint());
        set.setDuration(500).start();*/
    }
}
