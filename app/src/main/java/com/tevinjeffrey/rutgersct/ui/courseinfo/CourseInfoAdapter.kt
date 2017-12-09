package com.tevinjeffrey.rutgersct.ui.courseinfo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.data.model.Section
import com.tevinjeffrey.rutgersct.ui.base.BaseAdapter
import com.tevinjeffrey.rutgersct.ui.utils.HeaderVH
import com.tevinjeffrey.rutgersct.ui.utils.ItemClickListener
import java.math.BigInteger

class CourseInfoAdapter(
    private val headers: List<View>,
    private val sectionList: List<Section>,
    private val itemClickListener: ItemClickListener<Section, View>)
  : BaseAdapter<Section, RecyclerView.ViewHolder>() {

  init {
    setHasStableIds(true)
  }

  override fun getItemCount(): Int {
    return sectionList.size + 1
  }

  override fun getItemId(position: Int): Long {
    return if (position != 0) {
      BigInteger(sectionList[position - 1].topic_id).toLong()
    } else {
      0
    }
  }

  override fun getItemViewType(position: Int): Int {
    return if (position == 0) {
      TYPE_HEADER
    } else {
      TYPE_ITEM
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    if (holder.itemViewType == TYPE_HEADER && position == 0) {
      val headerVH = holder as HeaderVH
      headerVH.setHeaders(headers)
    } else if (holder.itemViewType == TYPE_ITEM) {
      val courseInfoViewHolder = holder as CourseInfoViewHolder
      val section = sectionList[position - 1]

      courseInfoViewHolder.setOpenStatus(section)
      courseInfoViewHolder.setSectionNumber(section)
      courseInfoViewHolder.setInstructors(section)
      courseInfoViewHolder.setTimes(section)

      courseInfoViewHolder.setOnClickListener(
          View.OnClickListener { v -> itemClickListener.onItemClicked(section, v) }
      )
    }
  }

  override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
    val context = viewGroup.context

    if (viewType == TYPE_HEADER) {
      val parent = LayoutInflater
          .from(context)
          .inflate(R.layout.course_info_metadata_container, viewGroup, false) as LinearLayout
      return HeaderVH.newInstance(parent)
    } else if (viewType == TYPE_ITEM) {
      val parent = LayoutInflater.from(context).inflate(R.layout.section_layout, viewGroup, false)
      return CourseInfoViewHolder.newInstance(parent)
    }
    return null
  }

  companion object {
    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1
  }
}
