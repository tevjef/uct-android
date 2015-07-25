package com.tevinjeffrey.rutgersct.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.StyleRes;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

import com.tevinjeffrey.rutgersct.R;

public class Utils {
    private static void setWindowColor(int colorDark, Activity context) {
        Window window = context.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(colorDark);
            window.setNavigationBarColor(colorDark);
        }
    }

    public static int fetchPrimaryColor(Context context) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorPrimary});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    public static int fetchPrimaryDarkColor(Resources.Theme context) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorPrimaryDark});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    public static Context wrapContextTheme(Activity activity, @StyleRes int styleRes) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(activity, styleRes);
        setWindowColor(fetchPrimaryDarkColor(contextThemeWrapper.getTheme()), activity);
        return contextThemeWrapper;
    }

}
