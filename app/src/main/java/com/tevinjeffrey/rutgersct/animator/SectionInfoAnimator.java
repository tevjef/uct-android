package com.tevinjeffrey.rutgersct.animator;

import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.tevinjeffrey.rutgersct.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SectionInfoAnimator {

    private final View rootView;
    @SuppressWarnings("WeakerAccess")
    @InjectView(R.id.add_courses_fab)
    FloatingActionButton mFab;

    public SectionInfoAnimator(View rootView) {
        this.rootView = rootView;
    }

    public void init() {

        ButterKnife.inject(this, rootView);

        AnimatorSet set7 = new AnimatorSet();
        set7.playTogether(
                //ObjectAnimator.ofFloat(mFab, "translationX", mFab.getX() + 200, mFab.getX()),

                ObjectAnimator.ofFloat(mFab, "scaleX", 0, 1),
                ObjectAnimator.ofFloat(mFab, "scaleY", 0, 1),
                ObjectAnimator.ofFloat(mFab, "alpha", 0, 1)

        );
        set7.setInterpolator(new EaseOutQuint());
        set7.setStartDelay(600);
        set7.setDuration(300).start();
    }
}
