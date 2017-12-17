package com.tevinjeffrey.rutgersct.ui.subject

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.data.model.Subject

class SubjectViewHolder(private val parent: View,
                        private val subjectTitle: TextView) : RecyclerView.ViewHolder(parent) {

  fun setOnClickListener(listener: View.OnClickListener) {
    parent.setOnClickListener(listener)
  }

  fun setSubjectTitle(subject: Subject) {
    val text = subject.number + ": " + subject.name
    subjectTitle.text = text
  }

  companion object {
    fun newInstance(parent: View): SubjectViewHolder {
      val subjectTitle = parent.findViewById<TextView>(R.id.list_item_title)
      return SubjectViewHolder(parent, subjectTitle)
    }
  }
}
