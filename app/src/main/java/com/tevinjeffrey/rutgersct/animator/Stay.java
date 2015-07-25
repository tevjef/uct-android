package com.tevinjeffrey.rutgersct.animator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class Stay extends Visibility {
    public Stay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Stay() {
    }

    @Override
    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        return createScaleAnimator(view, 1, 1);
    }


    @Override
    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        return createScaleAnimator(view, 1, 1);
    }


    public Animator createScaleAnimator(final View view, float startScale, float endScale) {
        PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat("scaleX", startScale, endScale);
        PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("scaleY", startScale, endScale);

        return ObjectAnimator.ofPropertyValuesHolder(view, holderX, holderY);
    }
}