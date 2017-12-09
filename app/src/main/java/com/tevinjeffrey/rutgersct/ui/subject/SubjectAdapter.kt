package com.tevinjeffrey.rutgersct.ui.subject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.data.model.Subject
import com.tevinjeffrey.rutgersct.ui.base.BaseAdapter
import com.tevinjeffrey.rutgersct.ui.utils.ItemClickListener
import java.math.BigInteger

class SubjectAdapter internal constructor(
    private val itemClickListener: ItemClickListener<Subject, View>)
  : BaseAdapter<Subject, SubjectViewHolder>() {

  init {
    setHasStableIds(true)
  }

  override fun getItemId(position: Int): Long {
    return BigInteger(getItems()[position].topic_id).toLong()
  }

  override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {

    val subject = getItems()[position]

    holder.setSubjectTitle(subject)
    holder.setOnClickListener { v -> itemClickListener.onItemClicked(subject, v) }
  }

  override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SubjectViewHolder {
    val context = viewGroup.context
    val parent = LayoutInflater.from(context).inflate(R.layout.subject_list_item, viewGroup, false)

    return SubjectViewHolder.newInstance(parent)
  }
}
