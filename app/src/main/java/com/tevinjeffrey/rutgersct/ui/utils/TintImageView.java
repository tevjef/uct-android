package com.tevinjeffrey.rutgersct.ui.utils;/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.tevinjeffrey.rutgersct.R;

public class TintImageView extends ImageView {
    private static final int[] TINT_ATTRS = {
            android.R.attr.background,
            android.R.attr.src,
            android.R.attr.tint,
    };
    @ColorInt
    private int color;

    public TintImageView(Context context) {
        this(context, null);
    }

    public TintImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TintImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = getContext().obtainStyledAttributes(attrs, TINT_ATTRS,
                defStyleAttr, 0);

        color = fetchAccentColor();

        Drawable drawable;
        if (a.length() > 0) {
            if (a.hasValue(2)) {
                color = a.getColor(2, 0);
            }
            if (a.hasValue(0)) {
                drawable = a.getDrawable(0);
                tintDrawable(drawable, color);
                setBackgroundDrawable(drawable);
            }
            if (a.hasValue(1)) {
                drawable = a.getDrawable(1);
                tintDrawable(drawable, color);
                setImageDrawable(drawable);
            }
        }
        a.recycle();
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        setBackgroundDrawable(background, color);
    }

    public void setBackgroundDrawable(Drawable background, int color) {
        setImageDrawable(background, color);
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        Drawable drawable = getDrawable(resId);
        setImageDrawable(drawable, color);
    }

    public void setImageResource(@DrawableRes int resId, @ColorInt int color) {
        Drawable drawable = getDrawable(resId);
        setImageDrawable(drawable, color);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        setImageDrawable(drawable, color);
    }

    public void setImageDrawable(Drawable drawable, @ColorInt int color) {
        tintDrawable(drawable, color);
        super.setImageDrawable(drawable);
    }

    private Drawable getDrawable(int resId) {
        return ContextCompat.getDrawable(getContext(), resId);
    }

    public Drawable tintDrawable(Drawable drawable, @ColorInt int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable, color);
        DrawableCompat.setTintMode(wrappedDrawable, PorterDuff.Mode.SRC_IN);
        return wrappedDrawable;
    }

    private int fetchAccentColor() {
        int color = Color.DKGRAY;
        try {
            TypedValue typedValue = new TypedValue();
            TypedArray a = getContext().obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorAccent});
            color = a.getColor(0, 0);
            a.recycle();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        return color;
    }

}