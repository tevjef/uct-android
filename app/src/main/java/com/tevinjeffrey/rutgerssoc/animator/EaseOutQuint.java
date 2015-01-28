package com.tevinjeffrey.rutgerssoc.animator;

import android.view.animation.Interpolator;

/**
 * Created by Tevin on 7/1/2014.
 */
public class EaseOutQuint implements Interpolator{
    @Override
    public float getInterpolation(float input) {
        float x = input - 1;
        return (float)Math.pow(x,5) + 1;

    }
}
