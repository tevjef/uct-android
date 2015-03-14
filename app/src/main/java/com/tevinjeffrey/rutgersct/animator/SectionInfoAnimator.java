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

public class SectionInfoAnimator {

    private final View rootView;
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
