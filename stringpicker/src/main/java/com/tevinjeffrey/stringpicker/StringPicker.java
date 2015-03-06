package com.tevinjeffrey.stringpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static com.tevinjeffrey.stringpicker.ThemeUtils.ThemeColors;
import static com.tevinjeffrey.stringpicker.ThemeUtils.resolveColors;


public class StringPicker extends LinearLayout {

    private Object mInstance;

    private Class<?> mClazz;

    private String[] mValues;

    private static final int SDK_VERSION;

    private static final String PICKER_CLASS;

    static {
        SDK_VERSION = Build.VERSION.SDK_INT;
        PICKER_CLASS = SDK_VERSION < Build.VERSION_CODES.FROYO ?
                "com.android.internal.widget.NumberPicker" : "android.widget.NumberPicker";
    }

    private int mDividerHeight;
    private ThemeColors themeColors;
    private int mDividerColor;

    public StringPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void setCurrent(final int current) {
        String methodName = isUnderHoneyComb() ? "setCurrent" : "setValue";
        try {
            Method method = mClazz.getMethod(methodName, int.class);
            method.invoke(mInstance, current);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getCurrent() {
        String methodName = isUnderHoneyComb() ? "getCurrent" : "getValue";
        try {
            Method method = mClazz.getMethod(methodName);
            return (Integer) method.invoke(mInstance);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getCurrentValue() {
        return mValues[getCurrent()];
    }

    public void setValues(final String[] values) {
        mValues = values;
        if (isUnderHoneyComb()) {
            try {
                Method method = mClazz.getMethod("setRange", int.class, int.class, String[].class);
                method.invoke(mInstance, 0, values.length - 1, values);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                mClazz.getMethod("setMaxValue", int.class).invoke(mInstance, values.length - 1);
                mClazz.getMethod("setMinValue", int.class).invoke(mInstance, 0);
                mClazz.getMethod("setDisplayedValues", String[].class).invoke(mInstance, new Object[]{values});
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setValues(final List<String> values) {
        mValues = values.toArray(new String[values.size()]);
        setValues(mValues);
    }

    public void setDivider(@ColorRes int mDividerColor) {
        ColorDrawable cd = new ColorDrawable(mDividerColor);
        setDivider(cd);
    }


    public void setDivider(Drawable drawable) {
        try {
            Field selectionDivider = mClazz.getDeclaredField("mSelectionDivider");
            selectionDivider.setAccessible(true);
            selectionDivider.set(mInstance, drawable);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        invalidate();
    }

    public void setDividerHeight(int dividerHeight) {
        try {
            Field selectionDividerHeight = mClazz.getDeclaredField("mSelectionDividerHeight");
            selectionDividerHeight.setAccessible(true);
            selectionDividerHeight.set(mInstance, dividerHeight);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        invalidate();
    }



    private void init(final Context context, final AttributeSet attrs) {
        try {
            Class<?> clazz = context.getClassLoader().loadClass(PICKER_CLASS);
            Constructor<?> constructor = clazz.getConstructor(Context.class, AttributeSet.class);
            mInstance = constructor.newInstance(context, attrs);
            mClazz = mInstance.getClass();

            String methodName = "setDescendantFocusability";
            Method method = mClazz.getMethod(methodName, int.class);
            method.invoke(mInstance, NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        initAttributes(context, attrs);

        if(mDividerColor != 0) {
            setDivider(mDividerColor);
        }
        setDividerHeight(mDividerHeight);

        addView((View) mInstance);
        setOrientation(VERTICAL);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        themeColors = resolveColors(context);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StringPicker,
                0, 0);

        try {
            mDividerColor = a.getInt(R.styleable.StringPicker_sp__dividerColor, themeColors.accentColor);

            //http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/5.0.2_r1/android/widget/NumberPicker.java#NumberPicker.0UNSCALED_DEFAULT_SELECTION_DIVIDER_HEIGHT
            mDividerHeight = a.getDimensionPixelSize(R.styleable.StringPicker_sp__dividerHeight, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));

        } finally {
            a.recycle();
        }
    }

    private static boolean isUnderHoneyComb() {
        return SDK_VERSION < Build.VERSION_CODES.HONEYCOMB;
    }

}