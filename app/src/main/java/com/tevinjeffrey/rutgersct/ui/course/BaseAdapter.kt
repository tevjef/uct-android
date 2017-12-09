package com.tevinjeffrey.rutgersct.ui.course

import android.support.v7.widget.RecyclerView

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder>
  : RecyclerView.Adapter<VH>() {

  private val items: MutableList<T> = mutableListOf()

  fun getItems(): List<T> {
    return items
  }

  override fun getItemCount(): Int {
    return items.size
  }

  fun swapData(data: List<T>) {
    items.clear()
    items.addAll(data)
    notifyDataSetChanged()
  }
}
