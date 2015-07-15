package com.tevinjeffrey.rutgersct.animator;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Path;
import android.os.Build;
import android.transition.PathMotion;
import android.util.AttributeSet;

import timber.log.Timber;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class SectionPath extends PathMotion {
    public SectionPath(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public Path getPath(float startX, float startY, float endX, float endY) {
        Timber.d("startX: %s, startY: %s, endX: %s, endY: %s", startX, startY, endX, endY);
        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo((float) ((endX - startX) * .2), (float) ((endY - startY ) * .8), endX, endY);
        return path;
    }
}
