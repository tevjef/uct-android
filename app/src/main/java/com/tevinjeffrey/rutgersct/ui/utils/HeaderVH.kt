package com.tevinjeffrey.rutgersct.ui.utils

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

class HeaderVH private constructor(private val parent: ViewGroup) : RecyclerView.ViewHolder(parent) {

  fun setHeaders(headers: Iterable<View>) {
    headers
        .filter { it.parent == null }
        .forEach { parent.addView(it) }
  }

  companion object {

    fun newInstance(parent: ViewGroup): HeaderVH {
      return HeaderVH(parent)
    }
  }
}
