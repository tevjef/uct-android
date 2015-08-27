package com.tevinjeffrey.rutgersct.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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

@TargetApi(Build.VERSION_CODES.KITKAT)
public class Scale extends Visibility {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Scale(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        view.setAlpha(0f);
        return createScaleAnimator(view, 0, 1);
    }


    @Override
    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        return createScaleAnimator(view, 1, 0);
    }


    public Animator createScaleAnimator(final View view, float startScale, float endScale) {
        PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat("scaleX", startScale, endScale);
        PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("scaleY", startScale, endScale);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, holderX, holderY);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setAlpha(1f);
            }
        });
        return animator;
    }
}