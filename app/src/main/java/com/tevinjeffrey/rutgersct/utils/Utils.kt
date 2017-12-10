package com.tevinjeffrey.rutgersct.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.content.res.TypedArray
import android.net.Uri
import android.os.Build
import android.support.annotation.StyleRes
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.Window
import android.view.WindowManager

import com.tevinjeffrey.rutgersct.R
import okio.BufferedSource
import okio.Okio
import okio.Source

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringWriter
import java.io.Writer

object Utils {
  fun fetchPrimaryColor(context: Context): Int {
    val typedValue = TypedValue()
    val a = context.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorPrimary))
    val color = a.getColor(0, 0)
    a.recycle()
    return color
  }

  fun fetchPrimaryDarkColor(context: Resources.Theme): Int {
    val typedValue = TypedValue()
    val a = context.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorPrimaryDark))
    val color = a.getColor(0, 0)
    a.recycle()
    return color
  }

  fun openLink(context: Context, url: String) {
    val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    i.data = Uri.parse(url)
    context.startActivity(i)
  }

  @Throws(IOException::class)
  fun parseResource(context: Context, resource: Int): BufferedSource {
    val inputStream = context.resources.openRawResource(resource)

    return Okio.buffer(Okio.source(inputStream))
  }

  fun setWindowColor(colorDark: Int, context: Activity) {
    val window = context.window
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
      window.statusBarColor = colorDark
      window.navigationBarColor = colorDark
    }
  }

  fun wrapContextTheme(activity: Activity, @StyleRes styleRes: Int): Context {
    val contextThemeWrapper = ContextThemeWrapper(activity, styleRes)
    setWindowColor(fetchPrimaryDarkColor(contextThemeWrapper.theme), activity)
    return contextThemeWrapper
  }
}
