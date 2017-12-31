package com.tevinjeffrey.rutgersct.ui.trackedsections

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.data.database.entities.UCTSubscription
import com.tevinjeffrey.rutgersct.ui.base.BaseAdapter
import com.tevinjeffrey.rutgersct.ui.utils.ItemClickListener
import java.math.BigInteger

class TrackedSectionsAdapter internal constructor(
    private val itemClickListener: ItemClickListener<UCTSubscription, View>)
  : BaseAdapter<UCTSubscription, TrackedSectionViewHolder>() {

  init {
    setHasStableIds(true)
  }

  override fun getItemId(position: Int): Long {
    return BigInteger(getItems()[position].section.topic_id).toLong()
  }

  override fun onBindViewHolder(holder: TrackedSectionViewHolder, position: Int) {

    val subscription = getItems()[position]

    holder.setCourseTitle(subscription.subject.number!!, subscription.course.name!!)
    holder.setOpenStatus(subscription.section)
    holder.setSectionNumber(subscription.section)
    holder.setInstructors(subscription.section)
    holder.setTimes(subscription.section)

    holder.setOnClickListener(View.OnClickListener { v -> itemClickListener.onItemClicked(subscription, v) })
  }

  override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TrackedSectionViewHolder {
    val context = viewGroup.context
    val parent = LayoutInflater.from(context).inflate(R.layout.section_layout_with_title, viewGroup, false)

    return TrackedSectionViewHolder.newInstance(parent)
  }
}
