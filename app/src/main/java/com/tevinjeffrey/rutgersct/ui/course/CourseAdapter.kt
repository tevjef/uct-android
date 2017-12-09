package com.tevinjeffrey.rutgersct.ui.course

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.data.model.Course
import com.tevinjeffrey.rutgersct.ui.base.BaseAdapter
import com.tevinjeffrey.rutgersct.ui.utils.ItemClickListener
import java.math.BigInteger

class CourseAdapter constructor(
    private val itemClickListener: ItemClickListener<Course, View>)
  : BaseAdapter<Course, CourseViewHolder>() {

  init {
    setHasStableIds(true)
  }

  override fun getItemId(position: Int): Long {
    return BigInteger(getItems()[position].topic_id).toLong()
  }

  override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
    val course = getItems()[position]
    holder.setCourseTitle(course)
    holder.setSectionsInfo(course)
    holder.setOnClickListener(View.OnClickListener { v ->
      itemClickListener.onItemClicked(course, v)
    })
  }

  override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CourseViewHolder {
    val context = viewGroup.context
    val parent = LayoutInflater
        .from(context)
        .inflate(R.layout.course_list_item, viewGroup, false)

    return CourseViewHolder.newInstance(parent)
  }
}
